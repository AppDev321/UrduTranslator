package com.core.service.socket

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.core.domain.NetworkConstants.HEADER_KEY_CONTENT_AUTHORIZATION
import com.core.extensions.TAG
import com.core.utils.AppLogger
import com.core.utils.NetworkStatusListener
import com.core.utils.PreferenceManager
import com.core.utils.Utils
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.TlsVersion
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.internal.immutableListOf
import java.util.concurrent.ExecutorService
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketClient @Inject constructor(
    val preferenceManager: PreferenceManager,
    @ApplicationContext context: Context
) : WebSocketListener() {

    val url = "wss://demo.piesocket.com/v3/channel_123?api_key=VCXCEuvhGcBDP7XhiJJUDvR1e1D3eiVjgZ9VRiaV&notify_self"
    private val startEndCallAckTimeout = 3000L
    private var webSocketClient: WebSocket? = null
    private var isSocketOpen = false
    private var networkRequest: NetworkRequest? = null
    private var isConnecting = false
    private var totalBytesReceived: Long = 0

    private val connectionSpec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
        .tlsVersions(TlsVersion.TLS_1_3)
        .cipherSuites(CipherSuite.TLS_AES_256_GCM_SHA384)
        .supportsTlsExtensions(false)
        .build()
    private val executorService: ExecutorService =
        ThreadPoolExecutor(0, Int.MAX_VALUE, 60, TimeUnit.SECONDS,
            SynchronousQueue(), ThreadFactory { runnable ->
                Thread(runnable, "app-socket-dispatcher").apply {
                    isDaemon = false
                    priority = Thread.MAX_PRIORITY
                }
            })
    val dispatcher = Dispatcher(executorService)
    val client = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .pingInterval(20, TimeUnit.SECONDS)
        .dispatcher(dispatcher)
        .connectionSpecs(
            if (Utils.androidQAndAbove)
                listOf(connectionSpec)
            else
                immutableListOf(
                    ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT
                )
        )
        .build()

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: android.net.Network) {
            // Internet connection is available
            tryReconnectIfClose()
        }
    }
    private var receivedPacketsIds = arrayListOf<String>()

    fun registerNetworkCallback() {
        if (networkRequest != null) return
        networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
        networkRequest?.let {
            connectivityManager.registerNetworkCallback(it, networkCallback)
        }
    }


    fun connect() = synchronized(this) {
        if (isSocketOpen || isConnecting)
            return

        isConnecting = true

        registerNetworkCallback()

        AppLogger.d(TAG, "[WS][CONN] starting connection ${this::class.java.name}")

        val request = Request.Builder().url(url)
            .addHeader(
                HEADER_KEY_CONTENT_AUTHORIZATION,
                "Tokern here"
            )//preferenceManager.getToken().safeGet())
        webSocketClient = client.newWebSocket(request.build(), this)

    }

    fun clearPendingSignal() {
        receivedPacketsIds.clear()
    }

    fun disconnect() {
        receivedPacketsIds.clear()
        webSocketClient?.close(1000, null)
        webSocketClient = null
    }

    fun sendMessage(
        signalMessage: SignalMessage,
    ) {

        send("ack:log", signalMessage.type)
    }


    private fun send(text: String, type: String): Boolean {
        val result = webSocketClient?.send(text) ?: false
        if (text.startsWith("ack:")) {
            AppLogger.d(TAG, "[WS][SEND] ack: $type")
        } else {
            AppLogger.d(TAG, "[WS][SEND] $type")
        }
        if (!result) {
            AppLogger.d(TAG, "[WS][SEND][ERR] type: $type")
        }
        return result
    }

    private fun generateChunks(packet: String): List<String> {
        val packetLength = packet.length
        val partSize = packetLength / 3
        return packet.chunked(partSize + packetLength % 3)
    }

    private fun tryReconnectIfClose() = synchronized(this) {
        if ((isSocketOpen.not() &&
                    NetworkStatusListener.hasInternetConnection() &&
                    isConnecting.not())
        ) {
            AppLogger.d(TAG, "[WS][RECONN] trying to reconnect...")
            client.dispatcher.cancelAll()
            webSocketClient?.cancel()
            webSocketClient?.close(1000, "forcefully closed.")
            webSocketClient = null
            connect()
        }
    }


    override fun onOpen(webSocket: WebSocket, response: Response) {
        // WebSocket connection is established
        isSocketOpen = true
        isConnecting = false
        totalBytesReceived = 0
        val protocol = response.protocol.name
//        restartPingPongTimer()
        this@WebSocketClient.webSocketClient = webSocket
        AppLogger.d(
            TAG,
            "[WS][OPEN][PROTO] W1 connection opened, protocol=$protocol"
        )
        //trySenPendingSignalMessages()
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        if (text.isEmpty()) return
        val bytesReceived = text.length
        totalBytesReceived += bytesReceived
        onRecvPacketLog(text, null)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        // WebSocket connection is closed
        isSocketOpen = false
        isConnecting = false
        totalBytesReceived = 0
        AppLogger.e(TAG, "[WS][CLOSED] W1 connection closed $code${reason}")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        AppLogger.e(TAG, "[WS][CLOSE] websocket closing $code${reason}")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        // WebSocket connection failure
        AppLogger.e(
            TAG,
            "[WS][FAILED] W1 connection failed ${response?.message} --- ${t.message}"
        )
        if (response?.code == 1000)
            return

        isSocketOpen = false
        isConnecting = false
        totalBytesReceived = 0
        tryReconnectIfClose()
    }

    private fun onRecvPacketLog(text: String, signal: String?) {
        val bytesReceived = text.length
        val bytesLogs = "$bytesReceived of $totalBytesReceived"
        val messageToLog: String
        if (text == "connected") {
            messageToLog = "[WS][RECV] connected, bytes: $bytesLogs"
        } else if (text == "USE_RELAY") {
            messageToLog = "[WS][RECV] USE_RELAY, bytes: $bytesLogs"
        } else if (text.startsWith("pck:")) {
            messageToLog = "[WS][RECV] pck: ${signal ?: "unk"}, bytes: $bytesLogs"
        } else if (text.startsWith("pckof")) {
            messageToLog = "[WS][RECV] OFFER, bytes: $bytesLogs"
        } else if (text.startsWith("pckan")) {
            messageToLog = "[WS][RECV] ANSWER, bytes: $bytesLogs"
        } else if (text.startsWith("ack")) {
            val message = text.replace("ack:", "")
            messageToLog = if (message.startsWith("pckof")) {
                "[WS][RECV] ack OFFER, bytes: $bytesLogs"
            } else if (message.startsWith("pckan")) {
                "[WS][RECV] ack ANSWER, bytes: $bytesLogs"
            } else {
                "[WS][RECV] ack, bytes: $bytesLogs"
            }
        } else {
            messageToLog = "[WS][RECV] unhandled: $text, bytes: $bytesLogs"
        }
        AppLogger.d(TAG, messageToLog)
    }


    fun isConnected() = isSocketOpen

}