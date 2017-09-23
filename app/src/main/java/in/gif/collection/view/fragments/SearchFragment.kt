package `in`.gif.collection.view.fragments

import `in`.gif.collection.Constants
import `in`.gif.collection.R
import `in`.gif.collection.custom.CustomItemDecorator
import `in`.gif.collection.databinding.FragmentSearchBinding
import `in`.gif.collection.hideKeyboard
import `in`.gif.collection.view.TrendingGifAdapter
import `in`.gif.collection.viewmodel.search.SearchGifViewModel
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * Created by vivek on 22/09/17.
 */
class SearchFragment : BaseFragment(), Observer {

    private var query: String = ""

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
        var adapter = TrendingGifAdapter(getFragmentHost())
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
                getFragmentHost().hideKeyboard()
            }
        }
    }

    fun doSearch(query: String) {
        searchBinding.viewModel!!.fetchSearchableGif(query)
    }

}