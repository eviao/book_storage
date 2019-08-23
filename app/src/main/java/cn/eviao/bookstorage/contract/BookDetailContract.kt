package cn.eviao.bookstorage.contract

import cn.eviao.bookstorage.model.Book
import cn.eviao.bookstorage.model.Box
import cn.eviao.bookstorage.presenter.BasePresenter
import cn.eviao.bookstorage.ui.BaseView
import io.reactivex.Single

interface BookDetailContract {

    interface View : BaseView<Presenter> {
        fun showLoading()

        fun hideLoading()

        fun showError(message: String)

        fun showUpdateBoxDialog(boxs: List<Box>, book: Book)

        fun hideUpdateBoxDialog()

        fun startBookList()

        fun renderBook(book: Book)
    }

    interface Presenter : BasePresenter {
        fun loadBook()

        fun loadBoxs()

        fun updateBox(box: Box)

        fun deleteBook()
    }
}