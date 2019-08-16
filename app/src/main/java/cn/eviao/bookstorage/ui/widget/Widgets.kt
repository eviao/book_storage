package cn.eviao.bookstorage.ui.widget

import android.view.ViewManager
import com.facebook.drawee.view.SimpleDraweeView
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton
import org.jetbrains.anko.custom.ankoView


@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.simpleDraweeView(theme: Int) = simpleDraweeView(theme) {}
inline fun ViewManager.simpleDraweeView(theme: Int = 0, init: SimpleDraweeView.() -> Unit) =
    ankoView({ SimpleDraweeView(it) }, theme, init)