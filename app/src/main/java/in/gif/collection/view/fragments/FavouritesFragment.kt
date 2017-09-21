package `in`.gif.collection.view.fragments

import `in`.gif.collection.R
import `in`.gif.collection.custom.CustomItemDecorator
import `in`.gif.collection.databinding.FragmentTrendingBinding
import `in`.gif.collection.view.TrendingGifAdapter
import `in`.gif.collection.viewmodel.trending.TrendingGifViewModel
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * Created by vivek on 21/09/17.
 */
class FavouritesFragment : BaseFragment(), Observer {

    private lateinit var mainActivityDataBinding: FragmentTrendingBinding

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainActivityDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_trending, container, false)
        mainActivityDataBinding.randomGifModel = TrendingGifViewModel(getFragmentHost())
        return mainActivityDataBinding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUPList(mainActivityDataBinding.randomGifRV)
        setUPObserver(mainActivityDataBinding.randomGifModel)
        mainActivityDataBinding.randomGifModel?.fetchFavouritesGifs()
    }

    fun setUPList(recyclerView: RecyclerView) {
        val adapter = TrendingGifAdapter(getFragmentHost())

        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(CustomItemDecorator(15))
    }

    override fun update(o: Observable?, arg: Any?) {
        when (o) {
            is TrendingGifViewModel -> {
                mainActivityDataBinding.loadMoreProgress.visibility = View.GONE
                val adapter = mainActivityDataBinding.randomGifRV.adapter as TrendingGifAdapter
                adapter.setGifList(list = o.getGifs())
            }
        }
    }

    fun setUPObserver(observable: TrendingGifViewModel?) {
        observable?.addObserver(this)
    }

}