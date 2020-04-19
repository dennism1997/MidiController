package com.moumou.midicontroller.ui.main.volumeFaders

import abak.tr.com.boxedverticalseekbar.BoxedVertical
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.moumou.midicontroller.Fader
import com.moumou.midicontroller.R
import com.moumou.midicontroller.databinding.VolumeFadersFragmentBinding
import com.moumou.midicontroller.midi.MidiController

/**
 * Created by MouMou on 02-04-20.
 */
class VolumeFadersFragment : Fragment() {
    companion object {
        const val AMOUNT_FADERS = 6

        fun newInstance() = VolumeFadersFragment().apply {
            arguments = bundleOf()
        }
    }

    private lateinit var faders: Array<Fader>
    private lateinit var faderContainer: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =
            DataBindingUtil.inflate<VolumeFadersFragmentBinding>(
                inflater,
                R.layout.volume_faders_fragment,
                container,
                false
            )

        val view = binding.root
        faderContainer = binding.faderContainer

        val iterator = faderContainer.children.iterator()
        faders = Array(faderContainer.childCount, fun(index: Int): Fader {
            val linearLayout = iterator.next() as LinearLayout
            val childIterator = linearLayout.children.iterator()
            val boxedVertical = childIterator.next() as BoxedVertical
            val textView = childIterator.next() as TextView

            return Fader(
                boxedVertical,
                textView,
                MidiController.Notes.channel,
                MidiController.Notes.volumeFadersNotes[index].toByte()
            )
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        faders.forEach { fader ->
            fader.boxedVertical.setOnBoxedPointsChangeListener(object :
                BoxedVertical.OnValuesChangeListener {
                override fun onPointsChanged(boxedPoints: BoxedVertical?, value: Int) {
                    val sent =
                        MidiController.sendControlChange(fader.channel, fader.note, value.toByte())
                    if (!sent) {
                        Toast.makeText(context!!, "Could not send midi message", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onStartTrackingTouch(boxedPoints: BoxedVertical?) {
                }

                override fun onStopTrackingTouch(boxedPoints: BoxedVertical?) {
                }
            })
        }
    }
}