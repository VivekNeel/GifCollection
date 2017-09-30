package com.gifs.collection.view.fragments

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gifs.collection.Constants
import com.gifs.collection.R
import com.gifs.collection.Utils.CommonUtils
import com.gifs.collection.custom.CustomItemDecorator
import com.gifs.collection.databinding.FragmentSearchBinding
import com.gifs.collection.showKeyboard
import com.gifs.collection.view.TrendingGifAdapter
import com.gifs.collection.viewmodel.search.SearchGifViewModel
import com.google.android.gms.ads.InterstitialAd
import java.util.*

/**
 * Created by vivek on 22/09/17.
 */
class SearchFragment : BaseFragment(), Observer {

    private var query: String? = null

    private lateinit var searchBinding: FragmentSearchBinding

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        searchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return searchBinding.root

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            query = arguments.getString(Constants.KEY_FRAGMENT_SEARCH_QUERY)
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        CommonUtils.showInterstitialAds(InterstitialAd(getFragmentHost()))
        setupList(searchBinding.randomGifRV)
        if (!TextUtils.isEmpty(query)) {
            doSearch(query)
        }

    }

    fun setupObservers() {
        searchBinding.viewModel = SearchGifViewModel()
        searchBinding.viewModel!!.addObserver(this)
    }

    fun setupList(recyclerView: RecyclerView) {
        val adapter = TrendingGifAdapter(getFragmentHost())
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(CustomItemDecorator(15))
    }

    override fun update(o: Observable?, arg: Any?) {
        when (o) {
            is SearchGifViewModel -> {
                val adapter = searchBinding.randomGifRV.adapter as TrendingGifAdapter
                adapter.setGifList(o.getGifs())
            }
        }
    }

    fun doSearch(query: String?) {
        searchBinding.viewModel!!.fetchSearchableGif(query)
    }

}