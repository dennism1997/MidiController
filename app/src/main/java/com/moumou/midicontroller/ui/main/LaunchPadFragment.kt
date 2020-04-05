package com.moumou.midicontroller.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.moumou.midicontroller.R
import com.moumou.midicontroller.databinding.LaunchpadFragmentBinding
import com.moumou.midicontroller.midi.MidiController
import com.moumou.midicontroller.ui.main.midi.LaunchButtons
import kotlinx.android.synthetic.main.launchpad_fragment.*


class LaunchPadFragment() : Fragment() {

    companion object {
        fun newInstance() = LaunchPadFragment().apply {
            arguments = bundleOf(
            )
        }
    }

    private lateinit var launchButtons: LaunchButtons

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val buttons = ArrayList<AppCompatButton>()
        buttons.addAll(this.buttonContainer.children as Sequence<MaterialButton>)
        launchButtons = LaunchButtons(buttons, MidiController, context!!)
        MidiController.addSubscriber(launchButtons)
    }

    override fun onDestroy() {
        super.onDestroy()
        MidiController.removeSubscriber(launchButtons)
    }
}
