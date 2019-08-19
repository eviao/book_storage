package cn.eviao.bookstorage.contract

import cn.eviao.bookstorage.model.Book
import cn.eviao.bookstorage.presenter.BasePresenter
import cn.eviao.bookstorage.ui.BaseView

interface FetchDetailContract {
    interface View : BaseView<Presenter> {
        fun showLoading()

        fun hideLoading()

        fun showError(message: String)

        fun renderBook(book: Book)
    }

    interface Presenter : BasePresenter {
        fun loadBook()
    }
}