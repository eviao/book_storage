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
import cn.eviao.bookstorage.activity.BookDetailActivity
import cn.eviao.bookstorage.model.Book
import com.bumptech.glide.Glide

class DiffCallback : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }
}

class BookListAdapter(val context: Context) : PagedListAdapter<Book, BookListAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(context, LayoutInflater.from(context)
            .inflate(R.layout.activity_book_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    class ViewHolder(val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val imageView: ImageView = itemView.findViewById(R.id.iv_image)
        val titleView: TextView = itemView.findViewById(R.id.tv_title)
        val authorView: TextView = itemView.findViewById(R.id.tv_author)
        val summaryView: TextView = itemView.findViewById(R.id.tv_summary)

        var book: Book? = null

        init {
            itemView.setOnClickListener(this)
        }

        fun bindTo(book: Book?) {
            this.book = book

            book?.image?.let { Glide.with(context).load(it).into(imageView) }

            titleView.text = book?.title
            authorView.text = book?.author
            summaryView.text = book?.summary
        }

        override fun onClick(v: View) {
            book?.let { BookDetailActivity.start(context, it.isbn) }
        }
    }
}