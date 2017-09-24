package `in`.gif.collection.view.fragments

import `in`.gif.collection.*
import `in`.gif.collection.databinding.FragmentTrendingTermsBinding
import `in`.gif.collection.view.TrendingTermsAdapter
import `in`.gif.collection.viewmodel.trending.TrendingGifViewModel
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.fragment_trending_terms.*
import java.util.*

/**
 * Created by vivek on 21/09/17.
 */
class TrendingTermFragment : BaseFragment(), Observer, ITermItemClickedCallback {


    private lateinit var binding: FragmentTrendingTermsBinding

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trending_terms, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.randomGifModel = TrendingGifViewModel(getFragmentHost())
        setupObserver(binding.randomGifModel!!)
        setupList(termRV)
        binding.randomGifModel!!.fetchTrendingTerms()
    }

    override fun update(p0: Observable?, p1: Any?) {
        when (p0) {
            is TrendingGifViewModel -> {
                val adapter = binding.termRV.adapter as TrendingTermsAdapter
                adapter.setData(p0.getTerms())
            }
        }
    }

    fun setupObserver(observable: TrendingGifViewModel) {
        observable.addObserver(this)
    }

    fun setupList(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(getFragmentHost())
        recyclerView.adapter = TrendingTermsAdapter(getFragmentHost(), this)
    }

    override fun onTermClicked(name: String , view : View) {
        frame.show()
        val bundle = Bundle()
        bundle.putString(Constants.KEY_FRAGMENT_SEARCH_QUERY, name)
        (getFragmentHost() as MainActivity).setupFragments(MainActivity.TAG_SEARCH, bundle)
    }
}