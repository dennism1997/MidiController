package com.moumou.midicontroller.ui.main

import android.content.Context
import android.media.midi.MidiDevice
import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.moumou.midicontroller.R
import com.moumou.midicontroller.databinding.LaunchpadFragmentBinding
import com.moumou.midicontroller.midi.MidiController
import com.moumou.midicontroller.ui.main.midi.LaunchButtons
import kotlinx.android.synthetic.main.launchpad_fragment.*


class LaunchPadFragment : Fragment(), MidiSelectDialog.Listener {

    companion object {
        fun newInstance() = LaunchPadFragment()
        const val AMOUNT_BUTTONS = 20
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var launchButtons: LaunchButtons
    private lateinit var midiManager: MidiManager
    private lateinit var midiController: MidiController
    private var midiDevice: MidiDevice? = null

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
        super.onViewCreated(view, savedInstanceState)
        midiManager = context!!.getSystemService(Context.MIDI_SERVICE) as MidiManager
    }

    override fun onResume() {
        super.onResume()
        if (midiDevice == null || midiController.needsRefresh()) {
            if (midiManager.devices.size == 1) {
                openMidiDevice(midiManager.devices[0])
            } else if (midiManager.devices.size > 1) {
                openMidiDeviceSelectDialog(midiManager.devices)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = MainViewModel()
    }

    private fun openMidiDeviceSelectDialog(deviceInfos: Array<MidiDeviceInfo>) {
        val dialog = MidiSelectDialog(deviceInfos)
        dialog.setTargetFragment(this, 0)
        dialog.show(this.parentFragmentManager, "MidiDeviceDialog")
    }

    override fun openMidiDevice(deviceInfo: MidiDeviceInfo) {
        val buttons = ArrayList<AppCompatButton>()
        buttons.addAll(this.buttonContainer.children as Sequence<MaterialButton>)
        midiManager.openDevice(deviceInfo, {
            if (it != null) {
                midiController = MidiController(it)
                launchButtons = LaunchButtons(buttons, midiController, context!!)
                midiController.addSubscriber(launchButtons)
                Toast.makeText(context, "Connected to ${deviceInfo.id}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Could not open MIDI device", Toast.LENGTH_SHORT).show()
            }
        }, Handler(Looper.getMainLooper()))
    }


}
