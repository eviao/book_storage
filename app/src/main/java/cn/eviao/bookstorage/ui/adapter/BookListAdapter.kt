package cn.eviao.bookstorage.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.eviao.bookstorage.model.Book
import cn.eviao.bookstorage.ui.widget.DiffCallback
import cn.eviao.bookstorage.ui.widget.simpleDraweeView
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.generic.RoundingParams
import com.facebook.drawee.view.SimpleDraweeView
import org.jetbrains.anko.*


class BookListItem : AnkoComponent<ViewGroup> {

    companion object {
        const val ID_PICTURE = 0x0001
    }

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        verticalLayout {
            simpleDraweeView {
                id = ID_PICTURE

                val hierarchy = GenericDraweeHierarchyBuilder(resources).build()
                hierarchy.roundingParams = RoundingParams().setCornersRadius(dip(10).toFloat())
                setHierarchy(hierarchy)

                scaleType = ImageView.ScaleType.FIT_XY

            }.lparams(width = matchParent, height = dip(120)) {
                leftMargin = dip(16)
                rightMargin = dip(16)
                bottomMargin = dip(32)
            }
        }
    }
}

class BookListAdapter(val context: Context) : PagedListAdapter<Book, BookListAdapter.ViewHolder>(DiffCallback())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(context, BookListItem().createView(AnkoContext.create(context, parent)))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindTo(getItem(position))


    class ViewHolder(val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var book: Book? = null
        val pictureImage = itemView.findViewById<SimpleDraweeView>(BookListItem.ID_PICTURE)

        init {
            itemView.setOnClickListener(this)
        }

        fun bindTo(book: Book?) {
            this.book = book

            pictureImage.setImageURI("https://img3.doubanio.com/view/subject/l/public/s29063065.jpg")
        }

        override fun onClick(v: View?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}