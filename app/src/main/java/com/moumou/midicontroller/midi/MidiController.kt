package com.moumou.midicontroller.midi

import android.media.midi.*
import android.util.Log


/**
 * Created by MouMou on 29-03-20.
 */
class MidiController(device: MidiDevice) : MidiReceiver() {
    companion object {
        const val PORT = 0
    }

    init {
        this.setDevice(device)
    }

    private var inputPort: MidiInputPort? = null
    private var outputPort: MidiOutputPort? = null
    private val subscribers: ArrayList<MidiReceiverSubscriber> = ArrayList()

    fun send(byte1: Byte, byte2: Byte, byte3: Byte) {
        val byteArrayOf = byteArrayOf(byte1, byte2, byte3)
        Log.i("MIDI Sent", byteArrayOf.joinToString())
        inputPort!!.send(byteArrayOf, 0, 3)
    }

    private fun setDevice(device: MidiDevice) {
        for (port in device.info.ports) {
            if (inputPort == null && port.type == MidiDeviceInfo.PortInfo.TYPE_INPUT) {
                val input = device.openInputPort(port.portNumber)
                if (input != null) {
                    this.inputPort = input
                }
            }
            if (outputPort == null && port.type == MidiDeviceInfo.PortInfo.TYPE_OUTPUT) {
                val output = device.openOutputPort(port.portNumber)
                if (output != null) {
                    output.connect(this)
                    this.outputPort = output
                }
            }
        }
        if (this.inputPort == null) {
            Log.e("MIDI", "input port null")
        }
        if (this.outputPort == null) {
            Log.e("MIDI", "output port null")
        }
    }

    fun addSubscriber(subscriber: MidiReceiverSubscriber) {
        this.subscribers.add(subscriber)
    }

    override fun onSend(msg: ByteArray?, offset: Int, count: Int, timestamp: Long) {
        if (msg != null) {
            Log.i(
                "MIDI Received",
                "${msg.copyOfRange(offset, offset + count).joinToString()}"
            )
            for (i in 0 until (count / 3)){
                for (subscriber in this.subscribers) {
                    val fromIndex = offset + i * 3
                    subscriber.handle(msg.copyOfRange(fromIndex, fromIndex + 3))
                }
            }

        } else {
            Log.e("MIDI Received", "null")
        }
    }

    fun needsRefresh(): Boolean {
        return this.inputPort == null || this.outputPort == null
    }
}