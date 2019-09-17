package cn.eviao.bookstorage.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.eviao.bookstorage.model.Book
import cn.eviao.bookstorage.ui.activity.BookDetailActivity
import cn.eviao.bookstorage.ui.widget.DiffCallback
import cn.eviao.bookstorage.ui.widget.simpleDraweeView
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.view.SimpleDraweeView
import org.jetbrains.anko.*


class BookListItemUi : AnkoComponent<ViewGroup> {

    companion object {
        const val ID_PICTURE = 0x0001
    }

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        verticalLayout {
            simpleDraweeView {
                id = ID_PICTURE

                val hierarchy = GenericDraweeHierarchyBuilder(resources).build()
                hierarchy.actualImageScaleType = ScalingUtils.ScaleType.FIT_CENTER
                setHierarchy(hierarchy)
            }.lparams(width = matchParent, height = dip(110)) {
                setMargins(dip(8), dip(24), dip(8), dip(0))
            }
        }
    }
}

class BookListAdapter(val context: Context) : PagedListAdapter<Book, BookListAdapter.ViewHolder>(DiffCallback())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(context, BookListItemUi().createView(AnkoContext.create(context, parent)))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindTo(getItem(position))


    class ViewHolder(val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var book: Book? = null
        val pictureImage = itemView.findViewById<SimpleDraweeView>(BookListItemUi.ID_PICTURE)

        init {
            itemView.setOnClickListener(this)
        }

        fun bindTo(book: Book?) {
            book?.let {
                this.book = it
                it.image?.let { pictureImage.setImageURI(it) }
            }
        }

        override fun onClick(v: View?) = with(context) {
            book?.let {
                startActivity<BookDetailActivity>("isbn" to it.isbn)
            }
            Unit
        }
    }
}