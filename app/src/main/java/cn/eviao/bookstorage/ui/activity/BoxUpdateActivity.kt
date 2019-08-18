package cn.eviao.bookstorage.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.Gravity.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.marginLeft
import cn.eviao.bookstorage.R
import cn.eviao.bookstorage.ui.BaseActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.themedToolbar
import org.jetbrains.anko.cardview.v7.cardView

class BoxUpdateActivity : BaseActivity() {

    lateinit var ui: BoxUpdateActivityUi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ui = BoxUpdateActivityUi()
        ui.setContentView(this)
    }
}

class BoxUpdateActivityUi : AnkoComponent<BoxUpdateActivity> {

    lateinit var toolbar: Toolbar

    override fun createView(ui: AnkoContext<BoxUpdateActivity>) = with(ui) {
        verticalLayout {
            toolbar = themedToolbar {
                title = "修改"
                backgroundColor = Color.WHITE
                navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_left)
                elevation = dip(1.0f).toFloat()

                inflateMenu(R.menu.menu_box_update)
            }

            cardView {
                elevation = dip(1.0f).toFloat()

                linearLayout {

                    themedTextView("名称", R.style.TextBold).lparams {
                    }.lparams(width = wrapContent) {
                        rightMargin = dip(16)
                    }

                    editText() {
                        hint = "名称"
                        gravity = RIGHT
                        topPadding = dip(16)
                        bottomPadding = dip(16)
                        singleLine = true

                        background = null
                    }.lparams(width = matchParent)
                }.lparams(width = matchParent) {
                    leftMargin = dip(16)
                    rightMargin = dip(16)
                }
            }.lparams(width = matchParent) {
                topMargin = dip(16)
            }

            cardView {
                elevation = dip(1.0f).toFloat()
                setContentPadding(dip(16), dip(16), dip(16), dip(16))

                verticalLayout {

                    themedTextView("描述", R.style.TextBold).lparams {
                    }.lparams(width = matchParent)

                    editText() {
                        hint = "描述"
                        inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
                        minLines = 3
                        singleLine = false
                        gravity = START or TOP
                        backgroundColor = Color.WHITE
                    }.lparams(width = matchParent)
                }.lparams(width = matchParent)
            }.lparams(width = matchParent) {
                topMargin = dip(16)
            }
        }
    }
}