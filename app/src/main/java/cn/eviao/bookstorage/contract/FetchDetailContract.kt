package cn.eviao.bookstorage.contract

import cn.eviao.bookstorage.base.BasePresenter
import cn.eviao.bookstorage.base.BaseView

interface FetchDetailContract {

    interface View : BaseView<Presenter> {

        fun showSkeleton()

        fun showToast(message: String)

        fun showLoading()

        fun hideLoading()

        fun showError(message: String)

        fun disableSubmitButton()

        fun startBookList()

        fun renderBook()
    }

    interface Presenter : BasePresenter {

        fun loadBook()

        fun saveBook()
    }
}