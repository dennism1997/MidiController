package com.moumou.midicontroller.midi

import android.graphics.Color
import android.util.Log

/**
 * Created by MouMou on 29-03-20.
 */
object MidiMessage {

    fun noteOn(channel: Int): Byte {
        return ((NOTE_ON shl 4) + channel).toByte()
    }

    fun controlChange(channel: Int): Byte {
        return ((CONTROL_CHANGE shl 4) + channel).toByte()
    }

    const val NOTE_OFF: Int = 8
    const val NOTE_ON: Int = 9
    const val CONTROL_CHANGE: Int = 11

    fun getColor(byte: Int): Int {
        return when (byte) {
            0 -> Color.rgb(0, 0, 0)
            1 -> Color.DKGRAY
            2 -> Color.GRAY
            122 -> Color.rgb(204, 204, 204)
            125 -> Color.rgb(0, 0, 255)
            126 -> Color.rgb(0, 255, 0)
            127 -> Color.rgb(255, 0, 0)
            else -> {
                Log.i("MIDI", "Color $byte unknown")
                Color.GRAY
            }
        }
    }
}

@ExperimentalUnsignedTypes
fun Byte.isNoteOn(): Boolean {
    return this.toUByte().toInt().ushr(4) == MidiMessage.NOTE_ON
}

@ExperimentalUnsignedTypes
fun Byte.isControlChange(): Boolean {
    return this.toUByte().toInt().ushr(4) == MidiMessage.CONTROL_CHANGE
}