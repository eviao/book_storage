package cn.eviao.bookstorage.contract

import cn.eviao.bookstorage.base.BasePresenter
import cn.eviao.bookstorage.base.BaseView
import cn.eviao.bookstorage.ui.adapter.BookListAdapter

interface BookListContract {

    interface View : BaseView<Presenter> {

        fun showError(message: String)

        fun showEmpty()

        fun showContent()

        fun getListAdapter(): BookListAdapter

        fun setSearchHint(hint: String?)
    }

    interface Presenter : BasePresenter {

        fun loadBooks(keyword: String?)

        fun loadCount()
    }
}