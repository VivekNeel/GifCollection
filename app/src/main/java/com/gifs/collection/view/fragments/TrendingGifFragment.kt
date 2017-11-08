package com.gifs.collection.view.fragments

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gifs.collection.*
import com.gifs.collection.utils.CommonUtils
import com.gifs.collection.custom.CustomItemDecorator
import com.gifs.collection.databinding.FragmentTrendingBinding
import com.gifs.collection.model.tenor.tags.TagData
import com.gifs.collection.view.TrendingGifAdapter
import com.gifs.collection.viewmodel.trending.TrendingGifViewModel
import kotlinx.android.synthetic.main.fragment_trending.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by vivek on 17/09/17.
 */
class TrendingGifFragment : BaseFragment(), Observer, ITagItemClickedCallback {


    private lateinit var mainActivityDataBinding: FragmentTrendingBinding
    var isLoading = false
    private lateinit var type: String
    private var next: String? = null
    private lateinit var tagType: String
    private var searchTag: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments.getString(Constants.TYPE_FRAGMENT)
        tagType = arguments.getString(Constants.TYPE_TAG, "")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainActivityDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_trending, container, false)
        mainActivityDataBinding.randomGifModel = TrendingGifViewModel(getFragmentHost())
        return mainActivityDataBinding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUPList(mainActivityDataBinding.randomGifRV)
        setUPObserver(mainActivityDataBinding.randomGifModel)
        when (type) {
            "tags" -> {
                when (tagType) {
                    Constants.TYPE_TAG_REACTION -> {
                        mainActivityDataBinding.randomGifModel?.fetchTags(Constants.TYPE_TAG_REACTION)
                    }
                    Constants.TYPE_TAG_EXPLORE -> {
                        mainActivityDataBinding.randomGifModel?.fetchTags(Constants.TYPE_TAG_EXPLORE)
                    }
                }
            }
            else -> {
                mainActivityDataBinding.randomGifModel?.getData("")

            }
        }
        CommonUtils.setupBanner(adView)
    }


    fun setUPList(recyclerView: RecyclerView) {
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        when (type) {
            "tags" -> {
                val adapter = TrendingGifAdapter(getFragmentHost(), type = "tags", callback = this)
                recyclerView.adapter = adapter
                reactionGifEt.show()
                field.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {
                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        adapter.doSearch(p0.toString())
                    }
                })

            }
            else -> {
                val adapter = TrendingGifAdapter(getFragmentHost())
                recyclerView.adapter = adapter
            }
        }
        recyclerView.addItemDecoration(CustomItemDecorator(15))
        if (type.equals("trending")) {
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        val layoutManager: StaggeredGridLayoutManager = recyclerView?.layoutManager as StaggeredGridLayoutManager
                        val totalItemCount = layoutManager.itemCount
                        val lastVisibleItemPos = layoutManager.findLastCompletelyVisibleItemPositions(null)
                        if ((totalItemCount - 1) == getLastVisibleItem(lastVisibleItemPos)) {
                            isLoading = true
                            mainActivityDataBinding.loadMoreProgress.visibility = View.VISIBLE
                            mainActivityDataBinding.randomGifModel?.fetchTrendingGif(next)
                        }
                    }
                }

            }
            )
        }

    }

    override fun update(o: Observable?, arg: Any?) {
        when (o) {
            is TrendingGifViewModel -> {
                mainActivityDataBinding.loadMoreProgress.visibility = View.GONE
                val adapter = mainActivityDataBinding.randomGifRV.adapter as TrendingGifAdapter

                when (type) {
                    "tags" -> {
                        if (o.getExploreTerms() != null) {
                            if (o.getExploreTerms()!!.list.isEmpty()) {
                                val bundle = Bundle()
                                bundle.putString(Constants.KEY_FRAGMENT_SEARCH_QUERY, searchTag)
                                (getFragmentHost() as MainActivity).setupFragments(MainActivity.TAG_SEARCH, bundle)

                            } else
                                if (o.getExploreTerms()?.list!![0].searchTerm?.isNotEmpty()!!) {
                                    val bundle = Bundle()
                                    bundle.putString(Constants.KEY_FRAGMENT_SEARCH_QUERY, o.getExploreTerms()?.list!![0].searchTerm)
                                    (getFragmentHost() as MainActivity).setupFragments(MainActivity.TAG_SEARCH, bundle)
                                } else {
                                    adapter.setTagList(o.getExploreTerms()!!.list[0].list as ArrayList<TagData>)
                                }
                        } else {
                            adapter.setTagList(o.getTags() as ArrayList<TagData>)
                        }
                    }
                    else -> {
                        isLoading = false
                        adapter.setGifList(list = o.getGifs())
                        this.next = o.getNext()
                    }
                }

            }
        }
    }

    fun setUPObserver(observable: TrendingGifViewModel?) {
        observable?.addObserver(this)
    }

    fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    override fun onTagItemClicked(tag: String?) {
        searchTag = tag
        mainActivityDataBinding.randomGifModel?.fetchExploreTerm(tag)
    }
}