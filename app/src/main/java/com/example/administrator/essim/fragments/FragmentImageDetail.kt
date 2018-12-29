package com.example.administrator.essim.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget
import com.example.administrator.essim.R
import com.example.administrator.essim.activities_re.ImageDetailActivity
import com.example.administrator.essim.utils_re.GlideUtil
import com.github.ybq.android.spinkit.style.Wave
import kotlinx.android.synthetic.main.fragment_image_detail.*

class FragmentImageDetail : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val index = arguments!!.getSerializable("index") as Int
        val illustsBean = ((activity) as ImageDetailActivity).mIllustsBean
        val wave = Wave()
        mProgressbar.indeterminateDrawable = wave
        Glide.get(mContext).clearMemory()
        Glide.with(mContext).load<GlideUrl>(GlideUtil.getLargeImage(illustsBean, index))
                .into<GlideDrawableImageViewTarget>(object : GlideDrawableImageViewTarget(originalImage) {
                    override fun onResourceReady(drawable: GlideDrawable?, animation: GlideAnimation<in GlideDrawable>?) {
                        if (mProgressbar != null) {
                            mProgressbar.visibility = View.INVISIBLE
                            super.onResourceReady(drawable, animation)
                        }
                    }
                })
    }

    companion object {
        fun newInstance(position: Int): FragmentImageDetail {
            val args = Bundle()
            args.putSerializable("index", position)
            val fragment = FragmentImageDetail()
            fragment.arguments = args
            return fragment
        }
    }
}
