package cn.eviao.bookstorage.ui.widget

import androidx.recyclerview.widget.DiffUtil
import cn.eviao.bookstorage.model.Book


class DiffCallback : DiffUtil.ItemCallback<Book>() {

    override fun areItemsTheSame(oldItem: Book, newItem: Book) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Book, newItem: Book) =
        oldItem == newItem
}