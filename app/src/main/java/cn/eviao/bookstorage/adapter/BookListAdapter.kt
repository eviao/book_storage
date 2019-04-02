package cn.eviao.bookstorage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cn.eviao.bookstorage.R
import cn.eviao.bookstorage.model.Book
import com.bumptech.glide.Glide

class BookListAdapter(val context: Context) : PagedListAdapter<Book, BookListAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Book, newItem: Book): Any? {
        return null
    }
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(context, LayoutInflater.from(context)
            .inflate(R.layout.content_book_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    class ViewHolder(val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.iv_image)
        val titleView: TextView = itemView.findViewById(R.id.tv_title)
        val subtitleView: TextView = itemView.findViewById(R.id.tv_subtitle)
        val summaryView: TextView = itemView.findViewById(R.id.tv_summary)

        fun bindTo(book: Book?) {
            book?.image?.let { Glide.with(context).load(it).into(imageView) }

            titleView.text = book?.title
            subtitleView.text = book?.subtitle
            summaryView.text = book?.summary
        }
    }
}