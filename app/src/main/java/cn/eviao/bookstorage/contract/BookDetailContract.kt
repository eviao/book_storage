package cn.eviao.bookstorage.contract

import cn.eviao.bookstorage.base.BasePresenter
import cn.eviao.bookstorage.base.BaseView
import cn.eviao.bookstorage.model.Box

interface BookDetailContract {

    interface View : BaseView<Presenter> {

        fun showToast(message: String)

        fun showError(message: String)

        fun showUpdateBoxDialog(boxs: List<Box>)

        fun startBookList()

        fun renderBook()
    }

    interface Presenter : BasePresenter {

        fun loadBook()

        fun loadBoxs()

        fun updateBox(box: Box)

        fun deleteBook()
    }
}