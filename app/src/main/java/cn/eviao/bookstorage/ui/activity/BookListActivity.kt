package cn.eviao.bookstorage.ui.activity

import android.os.Bundle
import android.text.InputFilter
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import cn.eviao.bookstorage.R
import cn.eviao.bookstorage.model.Book
import cn.eviao.bookstorage.persistence.DataSource
import cn.eviao.bookstorage.service.BookService
import cn.eviao.bookstorage.ui.BaseActivity
import cn.eviao.bookstorage.ui.adapter.BookListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

class BookListActivity : BaseActivity() {

    private lateinit var dataSource: DataSource
    private lateinit var bookService: BookService

    private lateinit var ui: ListActivityUi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bookService = BookService()

        ui = ListActivityUi()
        ui.setContentView(this)

        bookService.loadAll().observe(this, Observer(ui.listAdapter::submitList))

        ui.scanButton.setOnClickListener(showScanning)

        bookService.add(Book(title = "111111111"))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    val showScanning = View.OnClickListener {
        startActivity<ScannerActivity>()
    }
}

class ListActivityUi() : AnkoComponent<BookListActivity> {

    lateinit var listAdapter: BookListAdapter

    lateinit var searchEdit: EditText
    lateinit var boxButton: ImageButton
    lateinit var scanButton: ImageButton

    companion object {
        const val COLS = 3
    }

    override fun createView(ui: AnkoContext<BookListActivity>) = with(ui) {
        verticalLayout {
            // header
            linearLayout {
                searchEdit = editText {
                    inputType = TYPE_CLASS_TEXT or TYPE_TEXT_FLAG_AUTO_COMPLETE
                    imeOptions = EditorInfo.IME_ACTION_SEARCH
                    hint = "共 128 本图书"
                    textAppearance = R.style.AppTheme_Search
                    background = getDrawable(context, R.drawable.search_edittext)
                    singleLine = true
                    filters = arrayOf(InputFilter.LengthFilter(32))
                }.lparams {
                    weight = 1f
                    rightMargin = dip(8)
                }

                boxButton = imageButton(imageResource = R.drawable.ic_box)
                    .lparams {
                        gravity = Gravity.CENTER_VERTICAL
                        rightMargin = dip(8)
                    }

                scanButton = imageButton(imageResource = R.drawable.ic_bar_code)
                    .lparams {
                        gravity = Gravity.CENTER_VERTICAL
                    }
            }.lparams(width = matchParent, height = wrapContent) {
                margin = dip(16)
            }

            // list
            frameLayout {
                backgroundColor = getColor(context, R.color.app_background_color)

                recyclerView {
                    layoutManager = GridLayoutManager(context, COLS)
                    listAdapter = BookListAdapter(context)
                    adapter = listAdapter
                }
            }.lparams(width = matchParent, height = matchParent)

        }.applyRecursively {  }
    }
}
