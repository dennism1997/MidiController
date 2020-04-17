package com.moumou.midicontroller.ui.main.fader

import abak.tr.com.boxedverticalseekbar.BoxedVertical
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.moumou.midicontroller.R
import com.moumou.midicontroller.databinding.FaderFragmentBinding
import com.moumou.midicontroller.midi.MidiController

/**
 * Created by MouMou on 02-04-20.
 */
class FaderFragment : Fragment() {
    companion object {
        fun newInstance() = FaderFragment().apply {
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
            DataBindingUtil.inflate<FaderFragmentBinding>(
                inflater,
                R.layout.fader_fragment,
                container,
                false
            )

        val view = binding.root
        faderContainer = binding.faderContainer

        val iterator = faderContainer.children.iterator()
        faders = Array(faderContainer.childCount, fun(_: Int): Fader {
            val linearLayout = iterator.next() as LinearLayout
            val childIterator = linearLayout.children.iterator()
            val boxedVertical = childIterator.next() as BoxedVertical
            val textView = childIterator.next() as TextView
            return Fader(boxedVertical, textView)
        })

        faders.forEach { fader ->
            fader.boxedVertical.setOnBoxedPointsChangeListener(object :
                BoxedVertical.OnValuesChangeListener {
                override fun onPointsChanged(boxedPoints: BoxedVertical?, value: Int) {
                    MidiController.sendControlChange(fader.channel, fader.note, value.toByte())
                }

                override fun onStartTrackingTouch(boxedPoints: BoxedVertical?) {
                }

                override fun onStopTrackingTouch(boxedPoints: BoxedVertical?) {
                }
            })
        }
        return view
    }

}