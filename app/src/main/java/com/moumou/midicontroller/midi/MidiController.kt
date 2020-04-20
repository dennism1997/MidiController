package com.moumou.midicontroller.midi

import android.media.midi.*
import android.util.Log
import com.moumou.midicontroller.ui.main.midi.LaunchButtons
import com.moumou.midicontroller.ui.main.mixer.MixerFragment
import com.moumou.midicontroller.ui.main.volumeFaders.VolumeFadersFragment
import java.io.IOException
import java.io.Serializable


/**
 * Created by MouMou on 29-03-20.
 */
object MidiController : MidiReceiver(), Serializable {

    private const val logTag = "MIDI"
    private var inputPort: MidiInputPort? = null
    private var outputPort: MidiOutputPort? = null
    private val subscribers: ArrayList<MidiReceiverSubscriber> = ArrayList()

    private fun sendMidiMessage(byte1: Byte, byte2: Byte, byte3: Byte) {
        if (inputPort == null) {
            throw MidiException("Input port null")
        }
        val byteArray = byteArrayOf(byte1, byte2, byte3)
        byteArray.forEachIndexed { index, byte ->
            if (byte < -127) {
                throw MidiException("Midi byte at index `$index` must be bigger than -127, but was `$byte`")
            }
            if (byte > 127) {
                throw MidiException("Midi byte at index `$index` must be smaller than 128, but was `$byte`")
            }
        }
        try {
            inputPort!!.send(byteArray, 0, 3)
            Log.d("MIDI Sent", byteArray.joinToString())
        } catch (e: IOException) {
            throw MidiException(e.toString())
        }
    }

    fun sendNoteOnMessage(channel: Int, note: Byte, value: Byte) {
        sendMidiMessage(MidiMessage.noteOn(channel), note, value)
    }

    fun sendNoteOnMessage(channel: Int, note: Int, value: Int) {
        sendNoteOnMessage(channel, note.toByte(), value.toByte())
    }

    fun sendControlChange(channel: Int, note: Byte, value: Byte) {
        sendMidiMessage(MidiMessage.controlChange(channel), note, value)
    }

    fun sendControlChange(channel: Int, note: Byte, value: Int) {
        sendControlChange(channel, note, value.toByte())
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
            throw MidiException("Input port null")
        }
        if (this.outputPort == null) {
            throw MidiException("Output port null")
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

    object Notes {
        val launchPadNotes: Array<Int> = Array(LaunchButtons.AMOUNT_BUTTONS) {
            getNextNote()
        }
        val volumeFadersNotes: Array<Int> = Array(VolumeFadersFragment.AMOUNT_FADERS) {
            getNextNote()
        }
        val upDownButtonNotes: Pair<Int, Int> = Pair(getNextNote(), getNextNote())
        val mixerNotes: Array<Int> = Array(MixerFragment.AMOUNT_FADERS) {
            getNextNote()
        }
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
    }
}