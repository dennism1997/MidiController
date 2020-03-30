package com.moumou.midicontroller.ui.main.midi

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import androidx.appcompat.widget.AppCompatButton
import com.moumou.midicontroller.midi.MidiController
import com.moumou.midicontroller.midi.MidiMessage
import com.moumou.midicontroller.midi.MidiReceiverSubscriber
import com.moumou.midicontroller.midi.isNoteOn

/**
 * Created by MouMou on 30-03-20.
 */
@ExperimentalUnsignedTypes
class LaunchButtons(
    private val buttons: ArrayList<AppCompatButton>,
    controller: MidiController,
    context: Context
) :
    MidiElement(controller, context), MidiReceiverSubscriber {

    private val noteToButtonIndex = HashMap<Int, Int>()

    init {
        buttons.forEachIndexed { index, button ->
            val note = MidiMessage.getNextNote()
            noteToButtonIndex[note] = index

            button.setOnClickListener {
                controller.send(MidiMessage.noteOn(1), note.toByte(), 127.toByte())
            }
            button.background.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC)
        }
    }

    override fun handle(byteArray: ByteArray) {
        if (byteArray[0].isNoteOn()) {
            val note = byteArray[1].toUByte().toInt()
            if (noteToButtonIndex.containsKey(note)) {
                val buttonIndex = noteToButtonIndex.getValue(note)
                val color = MidiMessage.getColor(byteArray[2].toUByte().toInt())
                buttons[buttonIndex].background.setColorFilter(color, PorterDuff.Mode.SRC)
            }
        }
    }
}