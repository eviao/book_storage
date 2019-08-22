package cn.eviao.bookstorage.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.Gravity.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import cn.eviao.bookstorage.R
import cn.eviao.bookstorage.contract.BoxUpdateContract
import cn.eviao.bookstorage.model.Box
import cn.eviao.bookstorage.presenter.BoxUpdatePresenter
import cn.eviao.bookstorage.ui.BaseActivity
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.themedToolbar
import org.jetbrains.anko.cardview.v7.cardView
import android.widget.Toast
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction


class BoxUpdateActivity : BaseActivity(), BoxUpdateContract.View {

    lateinit override var presenter: BoxUpdateContract.Presenter

    private lateinit var ui: BoxUpdateActivityUi
    private var id: Long = 0

    private lateinit var submitLoadingDialog: QMUITipDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        id = intent.getLongExtra("id", 0)

        submitLoadingDialog = QMUITipDialog.Builder(this)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord("正在保存")
            .create()

        presenter = BoxUpdatePresenter(this, id)
        presenter.subscribe()

        ui = BoxUpdateActivityUi()
        ui.setContentView(this)

        ui.topToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.save_menu_item -> {
                    updateBox()
                    true
                }
                R.id.delete_menu_item -> {
                    deleteBox()
                    true
                }
                else -> true
            }
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.unsubscribe()
    }

    override fun showLoading() {
        submitLoadingDialog.show()
    }

    override fun hideLoading() {
        submitLoadingDialog.hide()
    }

    override fun showError(message: String) {
        longToast(message)
    }

    override fun startBoxList() {
        startActivity<BoxListActivity>()
    }

    override fun renderBox(box: Box) {
        ui.nameEdit.setText(box.name)
        ui.introEdit.setText(box.intro)
    }

    fun updateBox() {
        val name = ui.nameEdit.text?.toString()
        if (name.isNullOrBlank()) {
            longToast("请输入名称")
            return
        }
        val intro = ui.introEdit.text?.toString()
        presenter.updateBox(Box(name = name, intro = intro))
    }

    fun deleteBox() {
        QMUIDialog.MessageDialogBuilder(this)
            .setMessage("确定要删除吗？")
            .addAction("取消") { dialog, index -> dialog.dismiss() }
            .addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE ) { dialog, index ->
                presenter.deleteBox()
            }
            .create(R.style.Dialog)
            .show()
    }
}

class BoxUpdateActivityUi : AnkoComponent<BoxUpdateActivity> {

    lateinit var topToolbar: Toolbar

    lateinit var nameEdit: EditText
    lateinit var introEdit: EditText

    override fun createView(ui: AnkoContext<BoxUpdateActivity>) = with(ui) {
        verticalLayout {
            topToolbar = themedToolbar {
                title = resources.getString(R.string.box_update_title)
                setTitleTextColor(ContextCompat.getColor(context, R.color.app_text_color_70))
                backgroundColor = Color.WHITE
                navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_left_32_56c596)
                elevation = dip(1.0f).toFloat()

                inflateMenu(R.menu.menu_box_update)
            }

            cardView {
                elevation = dip(1.0f).toFloat()

                linearLayout {
                    themedTextView("名称", R.style.TextBold) {
                        textColor = getColor(context, R.color.app_text_color_50)
                    }.lparams(width = wrapContent) {
                        rightMargin = dip(16)
                    }

                    nameEdit = editText() {
                        hint = "名称"
                        hintTextColor = getColor(context, R.color.app_text_color_30)
                        inputType = InputType.TYPE_CLASS_TEXT
                        imeOptions = EditorInfo.IME_ACTION_NEXT
                        gravity = END
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
                    themedTextView("描述", R.style.TextBold) {
                        textColor = getColor(context, R.color.app_text_color_50)
                    }.lparams(width = matchParent)

                    introEdit = editText() {
                        hint = "描述"
                        hintTextColor = getColor(context, R.color.app_text_color_30)
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