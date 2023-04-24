package com.example.rmovies.presentation.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rmovies.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.rmovies.databinding.FragmentSearchBinding
import com.example.rmovies.presentation.adapter.SearchAdapter
import com.example.rmovies.presentation.base.BaseFragment
import com.example.rmovies.presentation.viewmodel.SearchViewModel
import com.example.rmovies.utils.Constants
import com.example.rmovies.utils.UiState

class SearchFragment : BaseFragment() {

    private var binding: FragmentSearchBinding? = null
    override var uiState: MutableLiveData<UiState> = MutableLiveData()
    private val viewModel: SearchViewModel by viewModel()
    private val adapter = SearchAdapter { movie ->
        val bundle = Bundle()
        bundle.putInt(Constants.KEY_BUNDLE_MOVIE_ID, movie.id)
        findNavController().navigate(
            R.id.action_searchFragment_to_detailsFragment,
            bundle
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchEditText: EditText? =
            binding?.searchView?.findViewById(androidx.appcompat.R.id.search_src_text)
        searchEditText?.setTextColor(Color.WHITE)
        searchEditText?.setHintTextColor(Color.WHITE)
        viewModel.uiState = uiState
        search()
        configRecycle()
        back()
    }

    private fun back() {
        binding?.ivBack?.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun search() {
        binding?.searchView?.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchMovie(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun configRecycle() {
        viewModel.onSuccessSearch.observe(viewLifecycleOwner) { listMovies ->
            adapter.movies = listMovies
            binding?.rvSearchMovies?.adapter = adapter
            binding?.rvSearchMovies?.isVisible = true
            binding?.tvNoResults?.isVisible = false
            binding?.rvSearchMovies?.layoutManager = GridLayoutManager(context, 2)
            if (listMovies.isEmpty()) {
                binding?.rvSearchMovies?.isVisible = false
                binding?.tvNoResults?.isVisible = true
            }
        }

        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {

                }
                is UiState.Error -> {

                }
            }
        }
    }

}