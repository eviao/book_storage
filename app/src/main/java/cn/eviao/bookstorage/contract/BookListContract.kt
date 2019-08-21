package cn.eviao.bookstorage.contract

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import cn.eviao.bookstorage.model.Book
import cn.eviao.bookstorage.presenter.BasePresenter
import cn.eviao.bookstorage.ui.BaseView
import cn.eviao.bookstorage.ui.adapter.BookListAdapter

interface BookListContract {

    interface View : BaseView<Presenter> {
        fun showError(message: String)

        fun showEmpty()

        fun showContent()

        fun getListAdapter(): BookListAdapter

        fun setSearchHint(hint: String)
    }

    interface Presenter : BasePresenter {
        fun loadBooks(keyword: String?)

        fun loadCount()
    }
}