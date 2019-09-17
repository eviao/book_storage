package cn.eviao.bookstorage.contract

import cn.eviao.bookstorage.base.BasePresenter
import cn.eviao.bookstorage.base.BaseView

interface ScannerContract {

    interface View : BaseView<Presenter> {

        fun showLoading()

        fun hideLoading()

        fun showError(message: String)

        fun showErrorISBN(isbn: String)

        fun restartScanning()

        fun startFetchDetail(isbn: String)

        fun startBookDetail(isbn: String)
    }

    interface Presenter : BasePresenter {

        fun loadBook(isbn: String)
    }
}