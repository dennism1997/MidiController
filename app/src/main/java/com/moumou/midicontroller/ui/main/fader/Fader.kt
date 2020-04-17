package com.moumou.midicontroller.ui.main.fader

import abak.tr.com.boxedverticalseekbar.BoxedVertical
import android.widget.TextView
import com.moumou.midicontroller.midi.MidiController


/**
 * Created by MouMou on 04-04-20.
 */

class Fader(
    val boxedVertical: BoxedVertical,
    private val textView: TextView
) {
    val channel: Int = MidiController.channel
    val note: Byte = MidiController.getNextNote().toByte()

    fun setText(text: String) {
        textView.text = text
    }
}