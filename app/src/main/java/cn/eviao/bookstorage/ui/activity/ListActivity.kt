package cn.eviao.bookstorage.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import cn.eviao.bookstorage.R
import cn.eviao.bookstorage.ui.BaseActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class ListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ListActivityUi().setContentView(this)
    }
}

class ListActivityUi : AnkoComponent<ListActivity> {

    private val style = { v: View ->
        when (v) {
            is Button -> {
                v.textSize = 26f
            }
        }
    }

    override fun createView(ui: AnkoContext<ListActivity>) = with(ui) {
        verticalLayout {
            padding = dip(32)

            button("hello") {
                onClick {
                    toast("hello, world")
                }
            }
        }.applyRecursively(style)
    }
}
