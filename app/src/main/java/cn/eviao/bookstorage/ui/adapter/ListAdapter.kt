package cn.eviao.bookstorage.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import cn.eviao.bookstorage.model.Book
import cn.eviao.bookstorage.ui.widget.simpleDraweeView
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.generic.RoundingParams
import com.facebook.drawee.view.SimpleDraweeView
import org.jetbrains.anko.*


class ListItem : AnkoComponent<ViewGroup> {

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

class ListAdapter(val context: Context, val books: List<Book>) : RecyclerView.Adapter<ListAdapter.ViewHolder>()  {

    override fun getItemCount() = books.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(context, ListItem().createView(AnkoContext.create(context, parent)))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindTo(position, books.get(position))

    class ViewHolder(val context: Context, root: View) : RecyclerView.ViewHolder(root) {
        val picture = root.findViewById<SimpleDraweeView>(ListItem.ID_PICTURE)

        fun bindTo(position: Int, book: Book?) = with(context) {
            picture.setImageURI("https://img3.doubanio.com/view/subject/l/public/s29063065.jpg")
        }
    }
}