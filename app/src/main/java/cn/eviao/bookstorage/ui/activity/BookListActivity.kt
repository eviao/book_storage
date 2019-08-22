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
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.GridLayoutManager
import cn.eviao.bookstorage.R
import cn.eviao.bookstorage.contract.BookListContract
import cn.eviao.bookstorage.presenter.BookListPresenter
import cn.eviao.bookstorage.ui.BaseActivity
import cn.eviao.bookstorage.ui.adapter.BookListAdapter
import cn.eviao.bookstorage.ui.widget.multipleStatusView
import com.classic.common.MultipleStatusView
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

class BookListActivity : BaseActivity(), BookListContract.View {

    lateinit override var presenter: BookListContract.Presenter

    private lateinit var ui: ListActivityUi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ui = ListActivityUi()
        ui.setContentView(this)

        presenter = BookListPresenter(this)

        ui.boxButton.setOnClickListener(handleStartBox)
        ui.scanButton.setOnClickListener(handleStartScanner)
        ui.searchEdit.setOnEditorActionListener(handleSearch)
    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe()
    }

    override fun onPause() {
        super.onPause()
        presenter.unsubscribe()
    }

    override fun showError(message: String) {
        longToast(message)
    }

    override fun showEmpty() {
        ui.statusView.showEmpty(
            R.layout.layout_book_list_empty,
            RelativeLayout.LayoutParams(matchParent, matchParent)
        )
    }

    override fun showContent() {
        ui.statusView.showContent()
    }

    override fun getListAdapter(): BookListAdapter {
        return ui.listAdapter
    }

    override fun setSearchHint(hint: String) {
        ui.searchEdit.hint = hint
    }

    val handleSearch = TextView.OnEditorActionListener { view, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            val keyword = view.text?.toString()
            presenter.loadBooks(keyword)
        }
        true
    }

    val handleStartBox = View.OnClickListener {
        startActivity<BoxListActivity>()
    }

    val handleStartScanner = View.OnClickListener {
        startActivity<ScannerActivity>()
    }
}

class ListActivityUi() : AnkoComponent<BookListActivity> {

    lateinit var listAdapter: BookListAdapter

    lateinit var statusView: MultipleStatusView
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
                    hintResource = R.string.search_hint
                    textAppearance = R.style.AppTheme_Search
                    background = getDrawable(context, R.drawable.search_edittext)
                    singleLine = true
                    filters = arrayOf(InputFilter.LengthFilter(32))
                }.lparams {
                    weight = 1f
                    rightMargin = dip(8)
                }

                boxButton = imageButton(imageResource = R.drawable.ic_box_32)
                    .lparams {
                        gravity = Gravity.CENTER_VERTICAL
                        rightMargin = dip(8)
                    }

                scanButton = imageButton(imageResource = R.drawable.ic_barcode_32_205072)
                    .lparams {
                        gravity = Gravity.CENTER_VERTICAL
                    }
            }.lparams(width = matchParent, height = wrapContent) {
                margin = dip(16)
            }

            // list
            frameLayout {
                backgroundColor = getColor(context, R.color.app_background_color)

                statusView = multipleStatusView {
                    recyclerView {
                        layoutParams = RelativeLayout.LayoutParams(matchParent, matchParent)

                        layoutManager = GridLayoutManager(context, COLS)
                        listAdapter = BookListAdapter(context)
                        adapter = listAdapter
                    }
                }
            }.lparams(width = matchParent, height = matchParent)

        }.applyRecursively {  }
    }
}
