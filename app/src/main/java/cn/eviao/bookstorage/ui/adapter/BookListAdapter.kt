package cn.eviao.bookstorage.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.eviao.bookstorage.R
import cn.eviao.bookstorage.model.Book
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
                leftMargin = dip(8)
                topMargin = dip(24)
                rightMargin = dip(8)
                bottomMargin = dip(0)
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

        fun bindTo(b: Book?) {
            b?.let {
                book = it

                if (it.image == null) {
                    pictureImage.image = context.getDrawable(R.drawable.ic_placeholder_64_56c596)
                } else {
                    pictureImage.setImageURI(it.image)
                }
            }
        }

        override fun onClick(v: View?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}