package com.moumou.midicontroller

import abak.tr.com.boxedverticalseekbar.BoxedVertical
import android.widget.TextView
import com.moumou.midicontroller.midi.MidiController


/**
 * Created by MouMou on 04-04-20.
 */

class Fader(
    val boxedVertical: BoxedVertical,
    private val textView: TextView,
    val channel: Int,
    val note: Byte
) {

    fun setText(text: String) {
        textView.text = text
    }
}