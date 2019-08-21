package cn.eviao.bookstorage.contract

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import cn.eviao.bookstorage.model.Book
import cn.eviao.bookstorage.presenter.BasePresenter
import cn.eviao.bookstorage.ui.BaseView

interface BookListContract {

    interface View : BaseView<Presenter> {
        fun showError(message: String)

        fun showEmpty()

        fun setSearchHint(hint: String)
    }

    interface Presenter : BasePresenter {
        fun loadBooks(keyword: String?): LiveData<PagedList<Book>>

        fun loadCount()
    }
}