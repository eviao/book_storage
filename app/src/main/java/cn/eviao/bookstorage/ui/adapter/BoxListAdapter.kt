package cn.eviao.bookstorage.ui.adapter

import android.content.Context
import android.text.TextWatcher
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
import org.jetbrains.anko.sdk27.coroutines.textChangedListener

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
        ViewHolder(BoxListItemUi().createView(AnkoContext.create(context, parent)))

    override fun onBindViewHolder(holder: BoxListAdapter.ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.nameText.text = it.name

            if (it.intro.isNullOrBlank()) {
                holder.introText.visibility = View.GONE
            } else {
                holder.introText.text = it.intro
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val nameText: TextView = itemView.findViewById(BoxListItemUi.ID_NAME)
        val introText: TextView = itemView.findViewById(BoxListItemUi.ID_INTRO)

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