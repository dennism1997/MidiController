package com.moumou.midicontroller.ui.main.midi

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.widget.AppCompatButton
import com.moumou.midicontroller.midi.MidiController
import com.moumou.midicontroller.midi.MidiMessage
import com.moumou.midicontroller.midi.MidiReceiverSubscriber
import com.moumou.midicontroller.midi.isNoteOn

/**
 * Created by MouMou on 30-03-20.
 */
@ExperimentalUnsignedTypes
object LaunchButtons :
    MidiReceiverSubscriber {

    const val AMOUNT_BUTTONS = 25
    private val noteToButtonIndex = HashMap<Int, Int>()
    private lateinit var buttons: ArrayList<AppCompatButton>

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

    fun setButtons(buttons: ArrayList<AppCompatButton>) {
        this.buttons = buttons
        this.buttons.forEachIndexed { index, button ->
            val note = MidiController.launchPadNotes[index]
            noteToButtonIndex[note] = index

            button.setOnClickListener {
                MidiController.sendNoteOnMessage(MidiController.channel, note, 127)
            }
            button.background.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC)
        }
    }
}