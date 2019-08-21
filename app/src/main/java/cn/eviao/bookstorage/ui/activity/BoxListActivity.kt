package cn.eviao.bookstorage.ui.activity

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import cn.eviao.bookstorage.R
import cn.eviao.bookstorage.ui.BaseActivity
import cn.eviao.bookstorage.ui.adapter.BoxListAdapter
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.themedToolbar
import org.jetbrains.anko.cardview.v7.cardView
import org.jetbrains.anko.recyclerview.v7.recyclerView
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction
import android.text.InputType
import android.widget.ImageButton
import android.widget.RelativeLayout
import cn.eviao.bookstorage.contract.BoxListContract
import cn.eviao.bookstorage.presenter.BoxListPresenter
import cn.eviao.bookstorage.ui.widget.multipleStatusView
import com.classic.common.MultipleStatusView


class BoxListActivity : BaseActivity(), BoxListContract.View {

    lateinit override var presenter: BoxListContract.Presenter

    lateinit var ui: BoxListActivityUi
    lateinit var createBoxDialog: QMUIDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = BoxListPresenter(this)

        ui = BoxListActivityUi()
        ui.setContentView(this)
        ui.topToolbar.setOnMenuItemClickListener(handleMenuClick)
    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe()
    }

    override fun onPause() {
        super.onPause()
        presenter.unsubscribe()
    }

    override fun showEmpty() {
        val view = layoutInflater.inflate(R.layout.layout_box_list_empty, null)
        val addButton = view.findViewById<ImageButton>(R.id.create_box_button)
        addButton.setOnClickListener { showCreateBoxDialog() }
        ui.statusView.showEmpty(view, RelativeLayout.LayoutParams(matchParent, matchParent))
    }

    override fun showContent() {
        ui.statusView.showContent()
    }

    override fun showError(message: String) {
        longToast(message)
    }

    override fun getListAdapter(): BoxListAdapter {
        return ui.listAdapter
    }

    override fun hideCreateBoxDialog() {
        createBoxDialog?.let { it.dismiss() }
    }

    fun showCreateBoxDialog() {
        createBoxDialog = QMUIDialog.EditTextDialogBuilder(this)
            .setTitle("新增")
            .setPlaceholder("在此输入名称")
            .setInputType(InputType.TYPE_CLASS_TEXT)
            .addAction("取消", QMUIDialogAction.ActionListener { dialog, index -> dialog.dismiss() })
            .addAction("确定", QMUIDialogAction.ActionListener { dialog, index ->
                presenter.createBox("abcc")
            })
            .create(R.style.Dialog)
        createBoxDialog.show()
    }

    val handleMenuClick = Toolbar.OnMenuItemClickListener {
        when (it.itemId) {
            R.id.create_menu_item -> {
                showCreateBoxDialog()
                true
            }
            else -> true
        }
    }
}

class BoxListActivityUi : AnkoComponent<BoxListActivity> {

    lateinit var topToolbar: Toolbar
    lateinit var listAdapter: BoxListAdapter
    lateinit var statusView: MultipleStatusView

    override fun createView(ui: AnkoContext<BoxListActivity>) = with(ui) {
        verticalLayout {
            topToolbar = themedToolbar {
                title = "Box List"
                backgroundColor = Color.WHITE
                navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_left_32_56c596)
                elevation = dip(1.0f).toFloat()

                inflateMenu(R.menu.menu_box_list)
            }

            cardView {
                statusView = multipleStatusView {
                    recyclerView {
                        backgroundColor = Color.WHITE

                        layoutManager = LinearLayoutManager(context)
                        addItemDecoration(
                            DividerItemDecoration(
                                context,
                                DividerItemDecoration.VERTICAL
                            )
                        )
                        listAdapter = BoxListAdapter(context)
                        adapter = listAdapter
                    }
                }
            }.lparams(width = matchParent, height = matchParent)
        }
    }
}