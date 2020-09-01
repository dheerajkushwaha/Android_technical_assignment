package com.example.assignment.ui.feeds

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.assignment.R
import com.example.assignment.databinding.RecyclerViewItemBinding

import com.example.assignment.repository.model.Row

/**
 * FeedRecyclerAdapter class use for render list item views
 */
class FeedRecyclerAdapter(
    var layoutId:Int,
    var mViewDataModel: FeedViewModel
) : RecyclerView.Adapter<FeedRecyclerAdapter.CustomViewHolder>() {

    private var articleList: List<Row>? = null

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val recyclerViewItemBinding= DataBindingUtil.inflate<RecyclerViewItemBinding>(layoutInflater,R.layout.recycler_view_item,parent,false)
            return CustomViewHolder(
                recyclerViewItemBinding
            )
    }

    override fun getItemCount(): Int {
        return if(articleList==null) 0 else articleList!!.size
    }

    @Override
    override fun getItemViewType(position: Int): Int {
        return layoutId
    }

    /**
     * view holder for row data
     */
    class CustomViewHolder(recyclerViewItemBinding:RecyclerViewItemBinding): RecyclerView.ViewHolder(recyclerViewItemBinding.root) {
        private var recyclerViewItemBinding=recyclerViewItemBinding
        fun bind(obj: FeedViewModel, index: Int) {
            recyclerViewItemBinding.dashboardViewModelCell=obj
            recyclerViewItemBinding.position=index
            recyclerViewItemBinding.executePendingBindings()
        }
    }

    //set updated feeddata
    fun setFeedList(articleList:List<Row>){
        this.articleList=articleList
    }
    companion object{
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: ImageView, url: String?) {
            if (!url.isNullOrEmpty()) {
                Glide.with(view.context)
                    .load(url)
                    .into(view)
            }
        }
    }
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(mViewDataModel,position)
    }

}