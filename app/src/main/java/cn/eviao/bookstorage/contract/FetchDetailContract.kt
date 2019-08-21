package cn.eviao.bookstorage.contract

import cn.eviao.bookstorage.model.Book
import cn.eviao.bookstorage.presenter.BasePresenter
import cn.eviao.bookstorage.ui.BaseView

interface FetchDetailContract {
    interface View : BaseView<Presenter> {
        fun showSkeleton()

        fun showSubmitLoading()

        fun hideSubmitLoading()

        fun disableSubmitButton()

        fun showError(message: String)

        fun startBookList()

        fun renderBook(book: Book)
    }

    interface Presenter : BasePresenter {
        fun loadBook()

        fun saveBook()
    }
}