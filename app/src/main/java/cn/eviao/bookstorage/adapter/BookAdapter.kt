package cn.eviao.bookstorage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.eviao.bookstorage.R
import cn.eviao.bookstorage.model.Book
import com.bumptech.glide.Glide

import kotlinx.android.synthetic.main.content_book_list_item.view.*

class BookAdapter(
    val context: Context,
    val data: MutableList<Book>
) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.content_book_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = data.get(position)

        Glide.with(context).load(book.image).into(holder.image)

        holder.title.text = book.title
        holder.subtitle.text = book.subtitle
        holder.summary.text = book.summary
    }

    fun addDatas(other: List<Book>) {
        data.addAll(other)
        notifyDataSetChanged()
    }

    class ViewHolder(
        itemView: View,
        val image: ImageView = itemView.findViewById(R.id.iv_image),
        val title: TextView = itemView.findViewById(R.id.tv_title),
        val subtitle: TextView = itemView.findViewById(R.id.tv_subtitle),
        val summary: TextView = itemView.findViewById(R.id.tv_summary)
    ) : RecyclerView.ViewHolder(itemView)
}