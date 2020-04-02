package com.moumou.midicontroller.midi

import android.graphics.Color
import android.util.Log

/**
 * Created by MouMou on 29-03-20.
 */
object MidiMessage {

    private var currentNote: Int = 0
    fun getNextNote(): Int {
        return currentNote++
    }

    fun noteOn(channel: Int): Byte {
        return ((9 shl 4) + channel).toByte()
    }

    const val NOTE_ON: Byte = 9
    const val NOTE_OFF: Byte = 9

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
    return this.toUByte().toInt().ushr(4).toByte() == MidiMessage.NOTE_ON
}

