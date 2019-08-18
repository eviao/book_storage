package cn.eviao.bookstorage.ui.widget

import android.view.ViewManager
import com.facebook.drawee.view.SimpleDraweeView
import com.qmuiteam.qmui.nestedScroll.QMUIContinuousNestedScrollLayout
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView
import com.robinhood.ticker.TickerView
import org.jetbrains.anko.custom.ankoView


@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.simpleDraweeView(theme: Int) = simpleDraweeView(theme) {}
inline fun ViewManager.simpleDraweeView(theme: Int = 0, init: SimpleDraweeView.() -> Unit) =
    ankoView({ SimpleDraweeView(it) }, theme, init)


@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.tickerView(theme: Int) = tickerView(theme) {}
inline fun ViewManager.tickerView(theme: Int = 0, init: TickerView.() -> Unit) =
    ankoView({ TickerView(it) }, theme, init)


@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.scrollLayout(theme: Int) = scrollLayout(theme) {}
inline fun ViewManager.scrollLayout(theme: Int = 0, init: QMUIContinuousNestedScrollLayout.() -> Unit) =
    ankoView({ QMUIContinuousNestedScrollLayout(it) }, theme, init)