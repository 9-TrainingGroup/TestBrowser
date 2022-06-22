package com.alva.testbrowser.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navGraphViewModels
import androidx.paging.LoadState
import com.alva.testbrowser.R
import com.alva.testbrowser.activity.NewsActivity
import com.alva.testbrowser.adapter.FooterAdapter
import com.alva.testbrowser.adapter.NewsAdapter
import com.alva.testbrowser.databinding.FragmentNewsBinding
import com.alva.testbrowser.util.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NewsFragment : Fragment() {
    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by navGraphViewModels<NewsViewModel>(R.id.navigation) { defaultViewModelProviderFactory }

    @Inject
    lateinit var newsAdapter: NewsAdapter

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

        binding.recyclerView.adapter =
            newsAdapter.withLoadStateFooter(FooterAdapter { newsAdapter.retry() })
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                when (arguments?.getInt("POSITION")) {
                    0 -> viewModel.pagingDataTT.collectLatest { newsAdapter.submitData(it) }
                    1 -> viewModel.pagingDataJX.collectLatest { newsAdapter.submitData(it) }
                    2 -> viewModel.pagingDataYL.collectLatest { newsAdapter.submitData(it) }
                    else -> viewModel.pagingDataYD.collectLatest { newsAdapter.submitData(it) }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            newsAdapter.loadStateFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest {
                    when (it.refresh) {
                        is LoadState.NotLoading -> {
                            delay(800)
                            binding.swipeRefresh.isRefreshing = false
                        }
                        is LoadState.Loading -> binding.swipeRefresh.isRefreshing = true
                        is LoadState.Error -> {
                            delay(3000)
                            binding.swipeRefresh.isRefreshing = false
                            newsAdapter.refresh()
                                .run { binding.swipeRefresh.isRefreshing = true }
                        }
                    }
                }
        }
        binding.swipeRefresh.setOnRefreshListener {
            newsAdapter.refresh()
        }
        (requireActivity() as NewsActivity).binding.refreshNews.setOnClickListener {
            newsAdapter.refresh()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}