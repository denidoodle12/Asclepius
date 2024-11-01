package com.dicoding.asclepius.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapter.TopHeadlinesAdapter
import com.dicoding.asclepius.databinding.FragmentHomeBinding
import com.dicoding.asclepius.data.Result
import com.dicoding.asclepius.viewmodels.TopHeadlinesViewModel
import com.dicoding.asclepius.viewmodels.TopHeadlinesViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var headlinesAdapter: TopHeadlinesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val factory = TopHeadlinesViewModelFactory.getInstance(requireActivity())
        val viewModel: TopHeadlinesViewModel by viewModels { factory }

        headlinesAdapter = TopHeadlinesAdapter { topHeadlines ->
            // i didn't make it clickable
            println(topHeadlines)
        }

        binding.rvTopHeadlines.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            hasFixedSize()
            adapter = headlinesAdapter
        }

        viewModel.getTopHeadlines()
        viewModel.topHeadlines.observe(viewLifecycleOwner) { topHeadlines ->
            when(topHeadlines) {
                is Result.Loading -> { binding.progressBarHome.visibility = View.VISIBLE }
                is Result.Success -> {
                    binding.progressBarHome.visibility = View.GONE
                    headlinesAdapter.submitList(topHeadlines.data)
                }
                is Result.Error -> {
                    binding.progressBarHome.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), topHeadlines.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}