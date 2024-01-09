package com.core.service.socket

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class BaseSignal internal constructor(
    @field:Expose @field:SerializedName("jid") val jid: String,
    @field:Expose @field:SerializedName("signalName") val signalName: String,
    @field:Expose @field:SerializedName("callType") val callType: String
)

class IceSignal(
    jid: String?,
    signalName: String?,
    @field:Expose @field:SerializedName("candidate") val candidate: String = "candidate:",
    @field:Expose @field:SerializedName("sdpMid") val sdpMid: String = "audio",
    @field:Expose @field:SerializedName("sdpMLineIndex") val sdpMLineIndex: Int,
    type: String?
) : BaseSignal(jid!!, signalName!!, type!!)


class SignalMessage(
    @field:Expose @field:SerializedName("type") var type: String,
)



