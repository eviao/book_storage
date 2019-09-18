package cn.eviao.bookstorage.contract

import cn.eviao.bookstorage.base.BasePresenter
import cn.eviao.bookstorage.base.BaseView

interface FetchDetailContract {

    interface View : BaseView<Presenter> {

        fun showSkeleton()

        fun showSubmitLoading()

        fun hideSubmitLoading()

        fun disableSubmitButton()

        fun showError(message: String)

        fun startBookList()

        fun renderBook()
    }

    interface Presenter : BasePresenter {

        fun loadBook()

        fun saveBook()
    }
}