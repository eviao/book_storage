package cn.eviao.bookstorage.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
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

typealias OnItemClickListener = (view: View, box: Box) -> Unit
typealias OnItemLongClickListener = (view: View, box: Box) -> Boolean

class BoxListAdapter(val context: Context) : PagedListAdapter<Box, BoxListAdapter.ViewHolder>(DiffCallback()) {

    var onItemClickListener: OnItemClickListener? = null
    var onItemLongClickListener: OnItemLongClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            context,
            BoxListItemUi().createView(AnkoContext.create(context, parent)),
            onItemClickListener,
            onItemLongClickListener
        )

    override fun onBindViewHolder(holder: BoxListAdapter.ViewHolder, position: Int) =
        holder.bindTo(getItem(position))

    class ViewHolder(
        val context: Context,
        itemView: View,
        val onItemClickListener: OnItemClickListener?,
        val onItemLongClickListener: OnItemLongClickListener?
    ) : RecyclerView.ViewHolder(itemView) {

        var box: Box? = null

        val nameText: TextView = itemView.findViewById(BoxListItemUi.ID_NAME)
        val introText: TextView = itemView.findViewById(BoxListItemUi.ID_INTRO)

        fun bindTo(box: Box?) {
            if (box == null) {
                return
            }

            this.box = box
            nameText.text = box.name

            if (box.intro.isNullOrBlank()) {
                introText.visibility = GONE
            } else {
                introText.visibility = VISIBLE
                introText.text = box.intro
            }

            onItemClickListener?.let {
                itemView.setOnClickListener {
                    onItemClickListener.invoke(it, box)
                }
            }

            onItemLongClickListener?.let {
                itemView.setOnLongClickListener {
                    onItemLongClickListener.invoke(it, box)
                }
            }
        }
    }
}

class BoxListItemUi : AnkoComponent<ViewGroup> {

    companion object {
        const val ID_NAME = 0x0001
        const val ID_INTRO = 0x0002
    }

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        verticalLayout {
            layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent)
            padding = dip(16)
            backgroundColor = Color.WHITE

            textView {
                id = ID_NAME

                textSize = sp(6).toFloat()
                textColor = getColor(context, R.color.app_text_color_70)
            }.lparams(width = matchParent)

            textView {
                id = ID_INTRO

                textSize = sp(6).toFloat()
                textColor = getColor(context, R.color.app_text_color_10)
            }.lparams(width = matchParent)
        }
    }
}