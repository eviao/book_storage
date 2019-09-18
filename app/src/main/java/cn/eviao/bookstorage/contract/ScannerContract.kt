package cn.eviao.bookstorage.contract

import cn.eviao.bookstorage.base.BasePresenter
import cn.eviao.bookstorage.base.BaseView

interface ScannerContract {

    interface View : BaseView<Presenter> {

        fun showError(message: String)

        fun showInvalidISBN(isbn: String)

        fun restartScanning()

        fun startFetchDetail(isbn: String)

        fun startBookDetail(isbn: String)
    }

    interface Presenter : BasePresenter {

        fun loadBook(isbn: String)
    }
}