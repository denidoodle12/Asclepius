package com.dicoding.asclepius.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapter.HistoryAdapter
import com.dicoding.asclepius.databinding.FragmentHistoryBinding
import com.dicoding.asclepius.viewmodels.HistoryIndicatedViewModel
import com.dicoding.asclepius.viewmodels.HistoryIndicatedViewModelFactory

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var historyAdapter: HistoryAdapter
    private val viewModel: HistoryIndicatedViewModel by viewModels {
        HistoryIndicatedViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        historyAdapter = HistoryAdapter()

        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            hasFixedSize()
            adapter = historyAdapter
        }

        binding.iconDeleteHistory.setOnClickListener {
            viewModel.deleteAllHistory()
            historyAdapter.submitList(emptyList())
            binding.iconDeleteHistory.visibility = View.GONE
            binding.emptyHistory.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllHistory { listHistory ->
            historyAdapter.submitList(listHistory)

            if (listHistory.isEmpty()) {
                binding.iconDeleteHistory.visibility = View.GONE
                binding.emptyHistory.visibility = View.VISIBLE
            } else {
                binding.iconDeleteHistory.visibility = View.VISIBLE
                binding.emptyHistory.visibility = View.GONE
            }
        }
    }
}