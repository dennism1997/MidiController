package com.moumou.midicontroller.ui.main.mixer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.moumou.midicontroller.Fader
import com.moumou.midicontroller.R
import com.moumou.midicontroller.databinding.MixerFragmentBinding

/**
 * Created by MouMou on 19-04-20.
 */
class MixerFragment : Fragment() {
    companion object {
        const val AMOUNT_FADERS = 4
        fun newInstance() = MixerFragment().apply {
            arguments = bundleOf(
            )
        }
    }

    private lateinit var faders: Array<Fader>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<MixerFragmentBinding>(
            inflater,
            R.layout.mixer_fragment,
            container,
            false
        )

        return binding.root
    }
}