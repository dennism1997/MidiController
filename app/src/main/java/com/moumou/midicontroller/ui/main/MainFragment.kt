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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.moumou.midicontroller.R
import com.moumou.midicontroller.databinding.MainFragmentBinding
import com.moumou.midicontroller.midi.MidiController
import com.moumou.midicontroller.ui.main.fader.FaderFragment
import com.moumou.midicontroller.ui.main.midi.MidiSelectDialog

/**
 * Created by MouMou on 03-04-20.
 */
class MainFragment : Fragment(), MidiSelectDialog.Listener {
    private lateinit var navigationBar: ChipNavigationBar
    private lateinit var viewPager: ViewPager2
    private lateinit var midiManager: MidiManager
    private var midiDevice: MidiDevice? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =
            DataBindingUtil.inflate<MainFragmentBinding>(
                inflater,
                R.layout.main_fragment,
                container,
                false
            )
        val view = binding.root
        navigationBar = binding.nav
        viewPager = binding.viewPager

        midiManager = context!!.getSystemService(Context.MIDI_SERVICE) as MidiManager
        assertValidMidiDevice()

        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> LaunchPadFragment.newInstance()
                    1 -> FaderFragment.newInstance()
                    else -> throw RuntimeException("invalid position $position")
                }
            }

            override fun getItemCount(): Int {
                return 2
            }
        }
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                val id = when (position) {
                    0 -> R.id.menu_item_a
                    1 -> R.id.menu_item_b
                    else -> throw RuntimeException("invalid position $position")
                }
                navigationBar.setItemSelected(id, false)
            }
        })

        navigationBar.setItemSelected(R.id.menu_item_a, false)
        navigationBar.setOnItemSelectedListener { id ->
            val position = when (id) {
                R.id.menu_item_a -> 0
                R.id.menu_item_b -> 1
                else -> throw RuntimeException("invalid id $id")
            }
            viewPager.currentItem = position
        }


        return view
    }

    override fun onResume() {
        super.onResume()
        assertValidMidiDevice()
    }

    private fun assertValidMidiDevice() {
        if (midiDevice == null) {
            if (midiManager.devices.size == 1) {
                openMidiDevice(midiManager.devices[0])
            } else if (midiManager.devices.size > 1) {
                openMidiDeviceSelectDialog(midiManager.devices)
            }
        }
    }

    private fun openMidiDeviceSelectDialog(deviceInfos: Array<MidiDeviceInfo>) {
        val dialog = MidiSelectDialog(deviceInfos)
        dialog.setTargetFragment(this, 0)
        dialog.show(this.parentFragmentManager, "MidiDeviceDialog")
    }

    override fun openMidiDevice(deviceInfo: MidiDeviceInfo) {

        midiManager.openDevice(deviceInfo, {
            if (it != null) {
                MidiController.setDevice(it)
            } else {
                Toast.makeText(context, "Could not open MIDI device", Toast.LENGTH_SHORT).show()
            }
        }, Handler(Looper.getMainLooper()))
    }
}