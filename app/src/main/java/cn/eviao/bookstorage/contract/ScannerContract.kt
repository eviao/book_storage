package cn.eviao.bookstorage.contract

import cn.eviao.bookstorage.presenter.BasePresenter
import cn.eviao.bookstorage.ui.BaseView

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
        fun loadDetail(isbn: String)
    }
}