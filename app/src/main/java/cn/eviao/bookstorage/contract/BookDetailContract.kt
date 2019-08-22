package cn.eviao.bookstorage.contract

import cn.eviao.bookstorage.model.Book
import cn.eviao.bookstorage.model.Box
import cn.eviao.bookstorage.presenter.BasePresenter
import cn.eviao.bookstorage.ui.BaseView

interface BookDetailContract {

    interface View : BaseView<Presenter> {
        fun showLoading()

        fun hideLoading()

        fun showError(message: String)

        fun createUpdateBoxDialog(boxs: List<Box>, book: Book)

        fun renderBook(book: Book)
    }

    interface Presenter : BasePresenter {
        fun loadBook()
    }
}