package com.core.service.socket

import com.core.utils.PreferenceManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketManager @Inject constructor(
    private val webSocketClient: WebSocketClient,
    private val preferenceManager: PreferenceManager
) {

    fun connect() {
        webSocketClient.connect()
    }

    fun checkUserPreferenceAndReconnect() {
        if (webSocketClient.isConnected().not()) {
            webSocketClient.connect()
        }
    }

    fun disconnect() {
        webSocketClient.disconnect()
    }

    fun sendMessage(

        signalMessage: SignalMessage,

        ) {

        webSocketClient.sendMessage(

            signalMessage
        )
    }

    fun clearPendingSignals() {
        webSocketClient.clearPendingSignal()
    }

    fun isConnected(): Boolean = webSocketClient.isConnected()

}