package com.alva.testbrowser.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alva.testbrowser.R
import com.alva.testbrowser.activity.NewsActivity
import com.alva.testbrowser.databinding.FragmentInfoBinding
import com.alva.testbrowser.util.NewsViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private val types = listOf(
    "T1348647853363",
    "T1467284926140",
    "T1348648517839",
    "T1348649079062"
)

@AndroidEntryPoint
class InfoFragment : Fragment() {
    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel by navGraphViewModels<NewsViewModel>(R.id.navigation) { defaultViewModelProviderFactory }

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

        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = types.size
            override fun createFragment(position: Int): Fragment {
                (requireActivity() as NewsActivity).binding.searchEdit.doAfterTextChanged {
                    lifecycleScope.launch {
                        viewModel.getPagingData(it.toString().trim(), types[position])
                    }
                }
                return NewsFragment().also {
                    Bundle().apply {
                        putInt("POSITION", position)
                        it.arguments = this
                    }
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.getPagingData("", types[position])
                    }
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