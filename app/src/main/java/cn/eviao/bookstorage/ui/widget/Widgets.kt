package cn.eviao.bookstorage.ui.widget

import android.view.ViewManager
import com.facebook.drawee.view.SimpleDraweeView
import org.jetbrains.anko.custom.ankoView


@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.simpleDraweeView(theme: Int = 0) = simpleDraweeView(theme) {}
inline fun ViewManager.simpleDraweeView(theme: Int = 0, init: SimpleDraweeView.() -> Unit) =
    ankoView({ SimpleDraweeView(it) }, theme, init)