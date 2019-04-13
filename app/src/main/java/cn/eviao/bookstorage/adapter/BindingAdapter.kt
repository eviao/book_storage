package cn.eviao.bookstorage.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.Glide
import co.lujun.androidtagview.TagContainerLayout


@BindingAdapter("url")
fun ImageView.setImageUrl(url: String?) {
    url?.let {
        Glide.with(context)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    }
}

@BindingAdapter("tags")
fun TagContainerLayout.setTags(tags: List<String>?) {
    tags?.let { this.setTags(it) }
}