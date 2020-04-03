package com.moumou.midicontroller.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.moumou.midicontroller.R
import com.moumou.midicontroller.databinding.FaderFragmentBinding

/**
 * Created by MouMou on 02-04-20.
 */
class FaderFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =
            DataBindingUtil.inflate<FaderFragmentBinding>(
                inflater,
                R.layout.main_fragment,
                container,
                false
            )
        val view = binding.root
        return view
    }
}