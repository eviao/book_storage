package cn.eviao.bookstorage.ui.activity

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import cn.eviao.bookstorage.R
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.themedToolbar
import org.jetbrains.anko.recyclerview.v7.recyclerView
import android.view.Gravity.START
import android.view.View.FOCUSABLE_AUTO
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.setPadding
import cn.eviao.bookstorage.contract.BoxListContract
import cn.eviao.bookstorage.model.Box
import cn.eviao.bookstorage.presenter.BoxListPresenter
import cn.eviao.bookstorage.ui.widget.multipleStatusView
import com.classic.common.MultipleStatusView
import android.text.InputFilter
import android.view.WindowManager
import android.widget.LinearLayout
import cn.eviao.bookstorage.base.BaseActivity
import cn.eviao.bookstorage.ui.adapter.BoxListAdapter


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
class BoxListActivity : BaseActivity(), BoxListContract.View {

    lateinit override var presenter: BoxListContract.Presenter

    private lateinit var ui: BoxListUi

    private var editBoxDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = BoxListPresenter(this)

        ui = BoxListUi()
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
            LinearLayout.LayoutParams(matchParent, matchParent)
        )
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

    @Suppress("DEPRECATION")
    fun showCreateBoxDialog() {
        val dialogUi = BoxEditDialogUi()
        val view = dialogUi.createView(AnkoContext.create(this, this))

        val dialog = alert {
            title = "新增"
            customView {
                addView(view, LinearLayout.LayoutParams(matchParent, matchParent))
            }
            negativeButton("取消", { dialog -> dialog.dismiss() })
            positiveButton("确定", { dialog ->
                val name = dialogUi.nameEdit.text?.toString()
                val intro = dialogUi.introEdit.text?.toString()
                presenter.createBox(Box(name = name, intro = intro))
            })
        }.build() as Dialog

        val window = dialog.getWindow()
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        dialog.show()
        dialogUi.nameEdit.requestFocus()

        editBoxDialog = dialog
    }

    fun showUpdateBoxDialog(box: Box) {
        val dialogUi = BoxEditDialogUi()
        val view = dialogUi.createView(AnkoContext.create(this, this))

        dialogUi.nameEdit.setText(box.name)
        dialogUi.introEdit.setText(box.intro)

        val dialog = alert {
            title = "修改"
            customView {
                addView(view, LinearLayout.LayoutParams(matchParent, matchParent))
            }
            negativeButton("取消", { dialog -> dialog.dismiss() })
            positiveButton("确定", { dialog ->
                val name = dialogUi.nameEdit.text?.toString()
                val intro = dialogUi.introEdit.text?.toString()
                presenter.updateBox(Box(id = box.id, name = name, intro = intro))
            })
        }.build() as Dialog

        val window = dialog.getWindow()
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        dialog.show()
        dialogUi.nameEdit.requestFocus()

        editBoxDialog = dialog
    }

    override fun hideEditBoxDialog() {
        editBoxDialog?.dismiss()
    }

    override fun showToast(message: String) {
        toast(message)
    }

    fun showDeleteBoxDialog(box: Box) {
        alert {
            message = "确定要删除吗？"
            negativeButton("取消", { dialog -> dialog.dismiss() })
            positiveButton("确定", { dialog -> presenter.deleteBox(box) })
        }.show()
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
                filters = arrayOf<InputFilter>(InputFilter.LengthFilter(32))

                backgroundResource = R.drawable.edittext_editor

                focusable = FOCUSABLE_AUTO
                isFocusableInTouchMode = true
            }

            introEdit = editText {
                gravity = START

                hint = "备注"
                minLines = 3
                maxLines = 3
                filters = arrayOf<InputFilter>(InputFilter.LengthFilter(128))

                backgroundResource = R.drawable.edittext_editor
            }.lparams(width = matchParent) {
                topMargin = dip(8)
            }
        }
    }
}

class BoxListUi : AnkoComponent<BoxListActivity> {

    lateinit var topToolbar: Toolbar
    lateinit var listAdapter: BoxListAdapter
    lateinit var statusView: MultipleStatusView

    override fun createView(ui: AnkoContext<BoxListActivity>) = with(ui) {
        verticalLayout {
            topToolbar = themedToolbar {
                title = resources.getString(R.string.box_list_title)
                setTitleTextColor(getColor(context, R.color.app_text_color_secondary))

                backgroundColor = Color.WHITE
                navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_left_32_56c596)
                elevation = dip(1.0f).toFloat()

                inflateMenu(R.menu.menu_box_list)
            }

            verticalLayout {
                statusView = multipleStatusView {
                    recyclerView {
                        backgroundColor = getColor(context, R.color.app_background_color)

                        layoutParams = RelativeLayout.LayoutParams(matchParent, matchParent)
                        layoutManager = LinearLayoutManager(context)

                        addItemDecoration(
                            DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                                setDrawable(context.getDrawable(R.drawable.divider_list))
                            }
                        )

                        listAdapter = BoxListAdapter(context)
                        adapter = listAdapter
                    }
                }
            }.lparams(width = matchParent, height = matchParent)
        }
    }
}