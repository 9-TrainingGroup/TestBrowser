package com.alva.testbrowser.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.alva.testbrowser.adapter.FooterAdapter
import com.alva.testbrowser.adapter.NewsAdapter
import com.alva.testbrowser.R
import com.alva.testbrowser.databinding.FragmentNewsBinding
import com.alva.testbrowser.util.NewsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NewsFragment(val type: Int) : Fragment() {
    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = NewsAdapter()
        val viewModel by activityViewModels<NewsViewModel>()
        binding.recyclerView.adapter =
            adapter.withLoadStateFooter(FooterAdapter { adapter.retry() })
        when (type) {
            0 -> viewModel.pagingDataTT.observe(viewLifecycleOwner, {
                viewLifecycleOwner.lifecycleScope.launch {
                    adapter.submitData(it)
                }
            })
            1 -> viewModel.pagingDataJX.observe(viewLifecycleOwner, {
                viewLifecycleOwner.lifecycleScope.launch {
                    adapter.submitData(it)
                }
            })
            2 -> viewModel.pagingDataYL.observe(viewLifecycleOwner, {
                viewLifecycleOwner.lifecycleScope.launch {
                    adapter.submitData(it)
                }
            })
            else -> viewModel.pagingDataYD.observe(viewLifecycleOwner, {
                viewLifecycleOwner.lifecycleScope.launch {
                    adapter.submitData(it)
                }
            })
        }
        adapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> {
                    viewLifecycleOwner.lifecycleScope.launch {
                        delay(800)
                        binding.swipeRefresh.isRefreshing = false
                    }
                }
                is LoadState.Loading -> {
                    binding.swipeRefresh.isRefreshing = true
                }
                is LoadState.Error -> {
                    viewLifecycleOwner.lifecycleScope.launch {
                        delay(3000)
                        binding.swipeRefresh.isRefreshing = false
                        adapter.refresh().run { binding.swipeRefresh.isRefreshing = true }
                    }
                }
            }
        }
        binding.swipeRefresh.setOnRefreshListener {
            adapter.refresh()
        }
        requireActivity().findViewById<ImageButton>(R.id.refreshNews).setOnClickListener {
            adapter.refresh()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}