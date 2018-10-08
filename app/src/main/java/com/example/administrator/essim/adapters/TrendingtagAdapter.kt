package com.example.administrator.essim.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.administrator.essim.R
import com.example.administrator.essim.interf.OnItemClickListener
import com.example.administrator.essim.response.TrendingtagResponse
import com.example.administrator.essim.utils.GlideUtil
import kotlinx.android.synthetic.main.tag_item_grid.view.*

class TrendingtagAdapter(private val mPixivRankItem: List<TrendingtagResponse.TrendTagsBean>,
                         private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var mOnItemClickListener: OnItemClickListener? = null
    private var imageHeight = 0

    init {
        imageHeight = ((mContext.resources.displayMetrics.widthPixels -
                4 * mContext.resources.getDimensionPixelSize(R.dimen.six_dp)) / 3) * 7 / 5

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TagHolder(mLayoutInflater.inflate(R.layout.tag_item_grid, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Glide.with(mContext).load(GlideUtil().getMediumImageUrl(mPixivRankItem[position]
                .illust))
                .into((holder as TagHolder).itemView.pixiv_image)
        holder.itemView.pixiv_item_size.text = mPixivRankItem[position].tag
        holder.itemView.setOnClickListener { view -> mOnItemClickListener!!.onItemClick(view, position, 0) }
        holder.itemView.setOnLongClickListener { view ->
            mOnItemClickListener!!.onItemLongClick(view, position)
            true
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int {
        return mPixivRankItem.size
    }

    private class TagHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
