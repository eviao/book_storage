package cn.eviao.bookstorage.ui.widget

import androidx.recyclerview.widget.DiffUtil
import cn.eviao.bookstorage.model.Identifiable


class DiffCallback<T : Identifiable> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T) =
        oldItem.identity() == newItem.identity()

    override fun areContentsTheSame(oldItem: T, newItem: T) =
        oldItem.equals(newItem)
}