package cn.eviao.bookstorage.ui.activity

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import cn.eviao.bookstorage.R
import cn.eviao.bookstorage.model.Box
import cn.eviao.bookstorage.persistence.DataSource
import cn.eviao.bookstorage.service.BoxService
import cn.eviao.bookstorage.ui.BaseActivity
import cn.eviao.bookstorage.ui.adapter.BoxListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.themedToolbar
import org.jetbrains.anko.cardview.v7.cardView
import org.jetbrains.anko.recyclerview.v7.recyclerView
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction
import android.text.InputType


class BoxListActivity : BaseActivity() {

    private lateinit var dataSource: DataSource
    private lateinit var boxService: BoxService

    lateinit var ui: BoxListActivityUi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataSource = DataSource.getInstance(this)
        boxService = BoxService(dataSource)

        ui = BoxListActivityUi()
        ui.setContentView(this)

        ui.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_menu_item -> {
                    val builder = QMUIDialog.EditTextDialogBuilder(this)
                    builder.setTitle("创建")
                        .setPlaceholder("在此输入名称")
                        .setInputType(InputType.TYPE_CLASS_TEXT)
                        .addAction("取消", QMUIDialogAction.ActionListener { dialog, index -> dialog.dismiss() })
                        .addAction("确定", QMUIDialogAction.ActionListener { dialog, index ->

                        })
                        .create(R.style.Dialog).show()
                    true
                }
                else -> true
            }
        }

        boxService.loadAll().observe(this, Observer(ui.listAdapter::submitList))

        boxService.add(Box(name = "纸箱1", intro = "描述描述描述描述描述描述描述"))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        boxService.add(Box(name = "纸箱2"))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        boxService.add(Box(name = "纸箱3", intro = "描述描述描述描述描述描述描述"))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }
}

class BoxListActivityUi : AnkoComponent<BoxListActivity> {

    lateinit var toolbar: Toolbar
    lateinit var listAdapter: BoxListAdapter

    override fun createView(ui: AnkoContext<BoxListActivity>) = with(ui) {
        verticalLayout {
            toolbar = themedToolbar {
                title = "列表"
                backgroundColor = Color.WHITE
                navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_left)
                elevation = dip(1.0f).toFloat()

                inflateMenu(R.menu.menu_box_list)
            }

            cardView {

                recyclerView {
                    backgroundColor = Color.WHITE

                    layoutManager = LinearLayoutManager(context)
                    addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                    listAdapter = BoxListAdapter(context)
                    adapter = listAdapter
                }
            }.lparams(width = matchParent, height = matchParent)
        }
    }
}