package com.moumou.midicontroller.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.moumou.midicontroller.R
import com.moumou.midicontroller.databinding.MainFragmentBinding

/**
 * Created by MouMou on 03-04-20.
 */
class MainFragment : Fragment() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

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
        tabLayout = binding.tabs
        viewPager = binding.viewPager

        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> LaunchPadFragment()
                    1 -> FaderFragment()
                    else -> throw RuntimeException("invalid position $position")
                }
            }

            override fun getItemCount(): Int {
                return 2
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.tab_a_label)
                }
                1 -> {
                    tab.text = getString(R.string.tab_b_label)
                }
                else -> throw RuntimeException("invalid position $position")
            }
        }.attach()
    }
}