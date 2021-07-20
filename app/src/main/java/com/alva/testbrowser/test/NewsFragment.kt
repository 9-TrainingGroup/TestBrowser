package com.alva.testbrowser.test

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.alva.testbrowser.databinding.FragmentNewsBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NewsFragment : Fragment() {
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

        val viewModel by activityViewModels<NewsViewModel>()
        val adapter = NewsAdapter()
        binding.recyclerView.adapter =
            adapter.withLoadStateFooter(FooterAdapter { adapter.retry() })
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pagingData.collectLatest {
                adapter.submitData(it)
            }
        }
        viewModel.initial.observe(viewLifecycleOwner, {
            binding.recyclerView.isVisible = it
        })
        adapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> {
                    viewLifecycleOwner.lifecycleScope.launch {
                        delay(800)
                        binding.swipeRefresh.isRefreshing = false
                        binding.recyclerView.visibility = View.VISIBLE
                        viewModel.initial.value = true
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}