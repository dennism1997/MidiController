package com.moumou.midicontroller.ui.main.fader

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.moumou.midicontroller.R
import com.moumou.midicontroller.databinding.FaderFragmentBinding

/**
 * Created by MouMou on 02-04-20.
 */
class FaderFragment : Fragment() {
    companion object {
        private const val AMOUNT_FADERS = 6
        fun newInstance() = FaderFragment().apply {
            arguments = bundleOf()
        }
    }

    private lateinit var faders: Array<AppCompatSeekBar>

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
        faders = Array(AMOUNT_FADERS) {
            val fader = Fader(ContextThemeWrapper(context!!, R.style.Fader))

            binding.faderContainer.addView(
                fader,
                LinearLayout.LayoutParams(0, MATCH_PARENT, 1f)
            )
            fader
        }


        faders.forEach { fader ->
            fader.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })
        }
        return view
    }
}