package com.fifty.fiftynews.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fifty.fiftynews.R
import com.fifty.fiftynews.adapters.ExclusiveNewsAdapter
import com.fifty.fiftynews.databinding.FragmentExclusiveNewsBinding
import com.fifty.fiftynews.models.Article
import com.fifty.fiftynews.models.ExclusiveNews
import com.fifty.fiftynews.models.Source
import com.fifty.fiftynews.ui.NewsActivity
import com.fifty.fiftynews.ui.NewsSubscriptionActivity
import com.fifty.fiftynews.ui.viewmodels.NewsViewModel
import com.fifty.fiftynews.ui.viewmodels.SubscriptionViewModel
import com.fifty.fiftynews.util.UiState

class ExclusiveNewsFragment : Fragment(R.layout.fragment_exclusive_news) {

    private lateinit var binding: FragmentExclusiveNewsBinding
    private lateinit var exclusiveNewsAdapter: ExclusiveNewsAdapter
    private lateinit var viewModel: NewsViewModel
    private lateinit var subscriptionViewModel: SubscriptionViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentExclusiveNewsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        subscriptionViewModel = (activity as NewsActivity).subscriptionViewModel

        setupRecyclerView()

        exclusiveNewsAdapter.setOnItemClickListener {
            // Get the clicked the article and put into a bundle and attach the bundle to a navigation component.

            val bundle = Bundle().apply {
                putSerializable("exclusive_news", it)
            }
            // Fragment transition.
            findNavController().navigate(
                R.id.action_exclusiveNewsFragment_to_exclusiveNewsViewFragment, bundle
            )
        }
        checkIsSubscriptionExist()
    }

    override fun onStart() {
        super.onStart()
        subscriptionViewModel.checkUserHaveAnyActiveSubscription {
            when (it) {
                is UiState.Failure -> {
                    Toast.makeText(activity, "Couldn't complete request, please try again", Toast.LENGTH_SHORT).show()
                }
                UiState.Loading -> {}
                is UiState.Success -> {
                    if (it.data) {
                        viewModel.getExclusiveNews()
                        viewModel.exclusiveNewsFromFireStore.observe(viewLifecycleOwner, Observer { exclusiveNews ->
                            exclusiveNewsAdapter.differ.submitList(exclusiveNews)
                        })
                    }
                }
            }
        }
    }

    private fun checkIsSubscriptionExist() {
        subscriptionViewModel.checkUserHaveAnyActiveSubscription {
            when (it) {
                is UiState.Failure -> {
                    Toast.makeText(activity, "Couldn't complete request, please try again", Toast.LENGTH_SHORT).show()
                }
                UiState.Loading -> {}
                is UiState.Success -> {
                    if (it.data) {
                        viewModel.getExclusiveNews()
                        viewModel.exclusiveNewsFromFireStore.observe(viewLifecycleOwner, Observer { exclusiveNews ->
                            exclusiveNewsAdapter.differ.submitList(exclusiveNews)
                        })
                    } else {
                        startActivity(Intent(activity, NewsSubscriptionActivity::class.java))
                    }
                }
            }
        }
    }

    private fun getExclusiveNewsToArticle(exclusiveNews: ExclusiveNews): Article {
        return Article(
            1,
            exclusiveNews.author,
            exclusiveNews.content,
            exclusiveNews.description,
            exclusiveNews.publishedAt,
            Source("", exclusiveNews.source),
            exclusiveNews.title,
            exclusiveNews.url,
            exclusiveNews.urlToImage
        )
    }

    private fun setupRecyclerView() {
        exclusiveNewsAdapter = ExclusiveNewsAdapter()
        binding.rvExclusiveNews.apply {
            adapter = exclusiveNewsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}