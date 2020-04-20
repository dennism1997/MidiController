package com.moumou.midicontroller.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.moumou.midicontroller.R
import com.moumou.midicontroller.databinding.LaunchpadFragmentBinding
import com.moumou.midicontroller.midi.MidiController
import com.moumou.midicontroller.midi.MidiException
import com.moumou.midicontroller.ui.main.midi.LaunchButtons
import kotlinx.android.synthetic.main.launchpad_fragment.*


@ExperimentalUnsignedTypes
class LaunchPadFragment : Fragment() {

    companion object {
        fun newInstance() = LaunchPadFragment().apply {
            arguments = bundleOf(
            )
        }
    }

    private lateinit var upButton: AppCompatButton
    private lateinit var downButton: AppCompatButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<LaunchpadFragmentBinding>(
            inflater,
            R.layout.launchpad_fragment,
            container,
            false
        )

        upButton = binding.upButton
        downButton = binding.downButton

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val buttons = ArrayList<AppCompatButton>()
        buttons.addAll(this.buttonContainer.children as Sequence<MaterialButton>)
        LaunchButtons.setButtons(buttons)
        MidiController.addSubscriber(LaunchButtons)

        buttons.forEachIndexed { index, button ->
            val note = MidiController.Notes.launchPadNotes[index]
            button.setOnClickListener {
                try {
                    MidiController.sendNoteOnMessage(MidiController.Notes.channel, note, 127)
                } catch (e: MidiException) {
                    Log.e("MIDI", e.toString())
                    Toast.makeText(context!!, e.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        val notes = MidiController.Notes.upDownButtonNotes
        upButton.setOnClickListener {
            try {
                MidiController.sendNoteOnMessage(MidiController.Notes.channel, notes.first, 127)
            } catch (e: MidiException) {
                Toast.makeText(context!!, e.toString(), Toast.LENGTH_SHORT).show()
            }

        }
        downButton.setOnClickListener {
            try {
                MidiController.sendNoteOnMessage(MidiController.Notes.channel, notes.second, 127)
            } catch (e: MidiException) {
                Toast.makeText(context!!, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MidiController.removeSubscriber(LaunchButtons)
    }
}
