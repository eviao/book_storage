package cn.eviao.bookstorage.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.eviao.bookstorage.R
import cn.eviao.bookstorage.model.Box
import cn.eviao.bookstorage.ui.widget.DiffCallback
import org.jetbrains.anko.*

class BoxListItem : AnkoComponent<ViewGroup> {

    companion object {
        const val ID_NAME = 0x0001
    }

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        verticalLayout {
            padding = dip(16)
            layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent)

            textView {
                id = ID_NAME

                textSize = sp(8).toFloat()
                textColor = getColor(context, R.color.app_text_color_70)
                textAppearance = R.style.AppTheme_Box_List_Item_Title
            }
        }
    }
}

class BoxListAdapter(val context: Context) : PagedListAdapter<Box, BoxListAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(BoxListItem().createView(AnkoContext.create(context, parent)))

    override fun onBindViewHolder(holder: BoxListAdapter.ViewHolder, position: Int) {
        val box = getItem(position)
        holder.nameText.text = box?.name
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val nameText: TextView = itemView.findViewById(BoxListItem.ID_NAME)

        val handleClick = View.OnClickListener {
            println("**************")
        }

        val handleLongClick = View.OnLongClickListener {
            println("==============")
            true
        }

        init {
            itemView.setOnClickListener(handleClick)
            itemView.setOnLongClickListener(handleLongClick)
        }
    }
}