package cn.eviao.bookstorage.ui.widget

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder


class SpecifiedDialogBuilder(context: Context?) : QMUIDialogBuilder<SpecifiedDialogBuilder>(context) {

    private var contentView: View? = null

    fun setContentView(view: View): SpecifiedDialogBuilder {
        this.contentView = view
        return this
    }

    fun getContentView(): View? {
        return contentView
    }

    override fun onCreateContent(dialog: QMUIDialog, parent: ViewGroup, context: Context) {
        parent.addView(contentView)
    }
}