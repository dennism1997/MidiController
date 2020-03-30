package com.moumou.midicontroller.midi

/**
 * Created by MouMou on 29-03-20.
 */
object MidiMessage {

    fun noteOn(channel: Int): Byte {
        return ((9 shl 4) + channel).toByte()
    }

    const val NOTE_ON: Byte = 9
    const val NOTE_OFF: Byte = 9

}