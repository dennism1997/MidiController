package com.moumou.midicontroller.ui.main.midi

import android.content.Context
import android.view.View
import com.moumou.midicontroller.midi.MidiController

/**
 * Created by MouMou on 29-03-20.
 */
abstract class MidiElement(
    protected val controller: MidiController,
    context: Context
) : View(context)