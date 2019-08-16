package cn.eviao.bookstorage.ui.activity

import android.os.Bundle
import android.text.InputFilter
import android.view.Gravity
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import cn.eviao.bookstorage.R
import cn.eviao.bookstorage.model.Book
import cn.eviao.bookstorage.ui.BaseActivity
import cn.eviao.bookstorage.ui.adapter.ListAdapter
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk27.coroutines.onClick

class ListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ListActivityUi().setContentView(this)
    }
}

class ListActivityUi : AnkoComponent<ListActivity> {

    companion object {
        const val COLS = 3
    }

    override fun createView(ui: AnkoContext<ListActivity>) = with(ui) {
        verticalLayout {
            padding = dip(16)

            // header
            linearLayout {

                editText {
                    hint = "书名"
                    textAppearance = R.style.AppTheme_Search
                    background = getDrawable(context, R.drawable.search_edittext)
                    singleLine = true
                    filters = arrayOf(InputFilter.LengthFilter(32))
                }.lparams {
                    weight = 1f
                    rightMargin = dip(8)
                }

                imageButton(imageResource = R.drawable.ic_list) {
                    onClick { toast("list") }
                }.lparams {
                    gravity = Gravity.CENTER_VERTICAL
                    rightMargin = dip(8)
                }

                imageButton(imageResource = R.drawable.ic_bar_code) {
                    onClick { toast("barcode") }
                }.lparams {
                    gravity = Gravity.CENTER_VERTICAL
                }
            }.lparams(width = matchParent, height = wrapContent) {
                bottomMargin = dip(16)
            }

            // list
            val books = listOf(
                Book(title = "111111111111"),
                Book(title = "222222222222"),
                Book(title = "333333333333"),
                Book(title = "444444444444"),
                Book(title = "555555555555"),
                Book(title = "666666666666")
            )

            frameLayout {
                recyclerView {
                    layoutManager = GridLayoutManager(context, COLS)
                    adapter = ListAdapter(context, books)
                }.lparams(width = matchParent, height = wrapContent)
            }
        }.applyRecursively {  }
    }
}
