package com.moumou.midicontroller.midi

import android.media.midi.*
import android.util.Log
import com.moumou.midicontroller.ui.main.fader.FaderFragment
import com.moumou.midicontroller.ui.main.midi.LaunchButtons
import java.io.Serializable


/**
 * Created by MouMou on 29-03-20.
 */
object MidiController : MidiReceiver(), Serializable {

    private var inputPort: MidiInputPort? = null
    private var outputPort: MidiOutputPort? = null
    private val subscribers: ArrayList<MidiReceiverSubscriber> = ArrayList()

    val launchPadNotes: Array<Int> = Array(LaunchButtons.AMOUNT_BUTTONS) {
        getNextNote()
    }

    val faderNotes: Array<Int> = Array(FaderFragment.AMOUNT_FADERS) {
        getNextNote()
    }

    val upDownButtonNotes: Pair<Int, Int> = Pair(getNextNote(), getNextNote())

    private var currentNote: Int = 0
    fun getNextNote(): Int {
        if (currentNote == 127) {
            channel++
            currentNote = 0
        } else {
            currentNote += 1
        }
        return currentNote
    }

    var channel: Int = 1
        private set

    private fun sendMidiMessage(byte1: Byte, byte2: Byte, byte3: Byte) {
        val byteArray = byteArrayOf(byte1, byte2, byte3)
        byteArray.forEachIndexed { index, byte ->
            if (byte < -127) {
                throw RuntimeException("Midi byte at index `$index` must be bigger than -127, but was `$byte`")
            }
            if (byte > 127) {
                throw RuntimeException("Midi byte at index `$index` must be smaller than 128, but was `$byte`")
            }
        }
        Log.d("MIDI Sent", byteArray.joinToString())
        inputPort!!.send(byteArray, 0, 3)
    }

    fun sendNoteOnMessage(channel: Int, note: Byte, value: Byte) {
        sendMidiMessage(MidiMessage.noteOn(channel), note, value)
    }

    fun sendNoteOnMessage(channel: Int, note: Int, value: Int) {
        sendMidiMessage(MidiMessage.noteOn(channel), note.toByte(), value.toByte())
    }


    fun sendControlChange(channel: Int, note: Byte, value: Byte) {
        sendMidiMessage(MidiMessage.controlChange(channel), note, value)
    }

    fun setDevice(device: MidiDevice) {
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
            Log.d(
                "MIDI Received",
                msg.copyOfRange(offset, offset + count).joinToString()
            )
            for (i in 0 until (count / 3)) {
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

    fun removeSubscriber(subscriber: MidiReceiverSubscriber) {
        this.subscribers.remove(subscriber)
    }
}