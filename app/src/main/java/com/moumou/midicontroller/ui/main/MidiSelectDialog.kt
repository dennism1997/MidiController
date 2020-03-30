package com.moumou.midicontroller.ui.main

import android.app.Dialog
import android.media.midi.MidiDeviceInfo
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class MidiSelectDialog(private val deviceInfos: Array<MidiDeviceInfo>) :
    DialogFragment() {

    private lateinit var listener: Listener

    interface Listener {
        fun openMidiDevice(deviceInfo: MidiDeviceInfo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listener = targetFragment as Listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(this.context!!)
        builder.setTitle("Select MIDI Device")
        builder.setItems(
            deviceInfos.map { it.id.toString() }.toTypedArray()
        ) { dialog, which ->
            listener.openMidiDevice(deviceInfos[which])
        }

        // Create the AlertDialog object and return it
        return builder.create()
    }


}