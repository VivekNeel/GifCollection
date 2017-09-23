package `in`.gif.collection.view

import `in`.gif.collection.ITermItemClickedCallback
import `in`.gif.collection.R
import `in`.gif.collection.databinding.LayoutTermBinding
import `in`.gif.collection.model.tenor.GifData
import `in`.gif.collection.model.tenor.GifResultsData
import `in`.gif.collection.viewmodel.CommonItemGifModel
import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.layout_term.view.*

/**
 * Created by vivek on 21/09/17.
 */
class TrendingTermsAdapter(context: Context, callback: ITermItemClickedCallback) : RecyclerView.Adapter<TrendingTermsAdapter.ViewHolder>() {

    private var data: List<String> = emptyList()
    val context: Context = context
    val callback = callback

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.let {
            holder.bindTerm(data[position])
            holder.itemView.itemCard.setOnClickListener {
                callback.onTermClicked(data[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val binding: LayoutTermBinding = DataBindingUtil.inflate(LayoutInflater.from(parent?.context), R.layout.layout_term, parent, false)
        return ViewHolder(binding.root, binding)
    }

    class ViewHolder(itemView: View?, var binding: LayoutTermBinding) : RecyclerView.ViewHolder(itemView) {

        fun bindTerm(term: String) {
            if (binding.viewModel == null) {
                binding.viewModel = CommonItemGifModel(itemView.context, GifResultsData(emptyList(), ""), 0, term)
            } else {
                binding.viewModel!!.setTrendingTermData(term)
            }
        }
    }

    fun setData(data: List<String>) {
        this.data = data
        notifyDataSetChanged()
    }
}