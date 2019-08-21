package cn.eviao.bookstorage.ui.widget

import android.view.ViewManager
import com.classic.common.MultipleStatusView
import com.facebook.drawee.view.SimpleDraweeView
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
inline fun ViewManager.multipleStatusView(theme: Int) = multipleStatusView(theme) {}
inline fun ViewManager.multipleStatusView(theme: Int = 0, init: MultipleStatusView.() -> Unit) =
    ankoView({ MultipleStatusView(it) }, theme, init)

