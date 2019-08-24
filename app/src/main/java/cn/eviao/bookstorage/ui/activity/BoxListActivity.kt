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
import android.view.Gravity.START
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.setPadding
import cn.eviao.bookstorage.contract.BoxListContract
import cn.eviao.bookstorage.model.Box
import cn.eviao.bookstorage.presenter.BoxListPresenter
import cn.eviao.bookstorage.ui.widget.SpecifiedDialogBuilder
import cn.eviao.bookstorage.ui.widget.multipleStatusView
import com.classic.common.MultipleStatusView
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog


class BoxListActivity : BaseActivity(), BoxListContract.View {

    lateinit override var presenter: BoxListContract.Presenter

    lateinit var ui: BoxListActivityUi
    lateinit var editBoxDialog: QMUIDialog
    private lateinit var submitLoadingDialog: QMUITipDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = BoxListPresenter(this)

        submitLoadingDialog = QMUITipDialog.Builder(this)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord("正在保存")
            .create()

        ui = BoxListActivityUi()
        ui.setContentView(this)

        ui.listAdapter.onItemClickListener = { view, box ->
            showUpdateBoxDialog(box)
        }
        ui.listAdapter.onItemLongClickListener = { view, box ->
            showDeleteBoxDialog(box)
            true
        }

        ui.topToolbar.setNavigationOnClickListener { startBookList() }
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
        ui.statusView.showEmpty(
            R.layout.layout_box_list_empty,
            RelativeLayout.LayoutParams(matchParent, matchParent)
        )
    }

    override fun showContent() {
        ui.statusView.showContent()
    }

    override fun showError(message: String) {
        longToast(message)
    }

    override fun showSubmitLoading() {
        submitLoadingDialog.show()
    }

    override fun hideSubmitLoading() {
        submitLoadingDialog.hide()
    }

    override fun getListAdapter(): BoxListAdapter {
        return ui.listAdapter
    }

    @Suppress("DEPRECATION")
    fun showCreateBoxDialog() {
        val dialogUi = BoxEditDialogUi()
        val view = dialogUi.createView(AnkoContext.create(this, this))

        val builder = SpecifiedDialogBuilder(this)
        editBoxDialog = builder.setTitle("新增")
            .setContentView(view)
            .addAction("取消", QMUIDialogAction.ActionListener { dialog, index -> dialog.dismiss() })
            .addAction("确定", QMUIDialogAction.ActionListener { dialog, index ->
                val name = dialogUi.nameEdit.text?.toString()
                val intro = dialogUi.introEdit.text?.toString()
                presenter.createBox(Box(name = name, intro = intro))
            })
            .create()
        editBoxDialog.show()
    }

    fun showUpdateBoxDialog(box: Box) {
        val dialogUi = BoxEditDialogUi()
        val view = dialogUi.createView(AnkoContext.create(this, this))

        dialogUi.nameEdit.setText(box.name)
        dialogUi.introEdit.setText(box.intro)

        val builder = SpecifiedDialogBuilder(this)
        editBoxDialog = builder.setTitle("修改")
            .setContentView(view)
            .addAction("取消", QMUIDialogAction.ActionListener { dialog, index -> dialog.dismiss() })
            .addAction("确定", QMUIDialogAction.ActionListener { dialog, index ->
                val name = dialogUi.nameEdit.text?.toString()
                val intro = dialogUi.introEdit.text?.toString()
                presenter.updateBox(Box(name = name, intro = intro))
            })
            .create()
        editBoxDialog.show()
    }

    fun showDeleteBoxDialog(box: Box) {
        editBoxDialog = QMUIDialog.MessageDialogBuilder(this)
            .setMessage("确定要删除吗？")
            .addAction("取消") { dialog, index -> dialog.dismiss() }
            .addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE ) { dialog, index ->
                presenter.deleteBox(box)
            }
            .create()
        editBoxDialog.show()
    }

    override fun hideEditBoxDialog() {
        editBoxDialog.dismiss()
    }

    fun startBookList() {
        startActivity<BookListActivity>()
    }

    val handleMenuClick = Toolbar.OnMenuItemClickListener {
        when (it.itemId) {
            R.id.save_menu_item -> {
                showCreateBoxDialog()
                true
            }
            else -> true
        }
    }
}

class BoxEditDialogUi : AnkoComponent<BoxListActivity> {

    lateinit var nameEdit: EditText
    lateinit var introEdit: EditText

    override fun createView(ui: AnkoContext<BoxListActivity>) = with(ui) {
        verticalLayout {
            setPadding(dip(24))

            nameEdit = editText {
                hint = "在此输入名称"
                singleLine = true
                backgroundResource = R.drawable.edittext_editor
            }

            introEdit = editText {
                hint = "备注"
                minLines = 3
                maxLines = 3
                gravity = START
                backgroundResource = R.drawable.edittext_editor
            }.lparams(width = matchParent) {
                topMargin = dip(8)
            }
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
                title = resources.getString(R.string.box_list_title)
                setTitleTextColor(getColor(context, R.color.app_text_color_70))
                backgroundColor = Color.WHITE
                navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_left_32_56c596)
                elevation = dip(1.0f).toFloat()

                inflateMenu(R.menu.menu_box_list)
            }

            cardView {
                statusView = multipleStatusView {

                    recyclerView {
                        backgroundColor = Color.WHITE
                        layoutParams = RelativeLayout.LayoutParams(matchParent, matchParent)

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