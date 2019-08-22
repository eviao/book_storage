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
import cn.eviao.bookstorage.ui.activity.BoxUpdateActivity
import cn.eviao.bookstorage.ui.widget.DiffCallback
import org.jetbrains.anko.*

class BoxListItemUi : AnkoComponent<ViewGroup> {

    companion object {
        const val ID_NAME = 0x0001
        const val ID_INTRO = 0x0002
    }

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        verticalLayout {
            padding = dip(16)
            layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent)

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

class BoxListAdapter(val context: Context) : PagedListAdapter<Box, BoxListAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(context, BoxListItemUi().createView(AnkoContext.create(context, parent)))

    override fun onBindViewHolder(holder: BoxListAdapter.ViewHolder, position: Int) =
        holder.bindTo(getItem(position))

    class ViewHolder(val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
        var box: Box? = null

        val nameText: TextView = itemView.findViewById(BoxListItemUi.ID_NAME)
        val introText: TextView = itemView.findViewById(BoxListItemUi.ID_INTRO)

        val handleClick = View.OnClickListener {
            with(context) {
                box?.let {
                    startActivity<BoxUpdateActivity>("id" to it.id)
                }
            }
        }

        init {
            itemView.setOnClickListener(handleClick)
        }

        fun bindTo(b: Box?) {
            b?.let {
                box = it

                nameText.text = it.name

                if (it.intro.isNullOrBlank()) {
                    introText.visibility = View.GONE
                } else {
                    introText.text = it.intro
                }
            }
        }
    }
}