package com.moumou.midicontroller.ui.main.midi

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import androidx.appcompat.widget.AppCompatButton
import com.moumou.midicontroller.midi.MidiController
import com.moumou.midicontroller.midi.MidiMessage
import com.moumou.midicontroller.midi.MidiReceiverSubscriber

/**
 * Created by MouMou on 30-03-20.
 */
class LaunchButtons(
    private val buttons: ArrayList<AppCompatButton>,
    controller: MidiController,
    context: Context
) :
    MidiElement(controller, context), MidiReceiverSubscriber {

    init {
        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                controller.send(MidiMessage.noteOn(1), index.toByte(), 127.toByte())
            }
            button.background.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC)
        }
    }

    override fun handle(byteArray: ByteArray) {
        Log.i("MIDI", "change color")
        this.buttons[0].background.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC)
//        this.buttons[0].setBackgroundColor(
//            Color.BLUE
////            Color.rgb(
////                Random.nextInt(),
////                Random.nextInt(),
////                Random.nextInt()
////            )
//        )
    }
}