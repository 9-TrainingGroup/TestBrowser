package com.alva.testbrowser.test

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alva.testbrowser.R
import com.alva.testbrowser.databinding.FragmentInfoBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class InfoFragment : Fragment() {
    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel by activityViewModels<NewsViewModel>()
        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 4
            override fun createFragment(position: Int): Fragment {
                requireActivity().findViewById<MaterialAutoCompleteTextView>(R.id.searchEdit)
                    .addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            when (position) {
                                0 -> viewModel.getPagingData(s.toString(), "T1348647853363")
                                1 -> viewModel.getPagingData(s.toString(), "T1467284926140")
                                2 -> viewModel.getPagingData(s.toString(), "T1348648517839")
                                else -> viewModel.getPagingData(s.toString(), "T1348649079062")
                            }
                        }

                        override fun afterTextChanged(s: Editable?) {
                        }
                    })
                return when (position) {
                    0 -> NewsFragment(0).also { viewModel.getPagingData("", "T1348647853363") }
                    1 -> NewsFragment(1).also { viewModel.getPagingData("", "T1467284926140") }
                    2 -> NewsFragment(2).also { viewModel.getPagingData("", "T1348648517839") }
                    else -> NewsFragment(3).also { viewModel.getPagingData("", "T1348649079062") }
                }
            }
        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_title_tt)
                1 -> getString(R.string.tab_title_jx)
                2 -> getString(R.string.tab_title_yl)
                else -> getString(R.string.tab_title_yd)
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}