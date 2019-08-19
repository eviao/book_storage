package cn.eviao.bookstorage.contract

import cn.eviao.bookstorage.presenter.BasePresenter
import cn.eviao.bookstorage.ui.BaseView

interface ScannerContract {
    interface View : BaseView<Presenter> {
        fun showLoading()

        fun hideLoading()

        fun showErrorISBN(isbn: String)

        fun restartScanning()

        fun showFetchDetail()

        fun showBookDetail()
    }

    interface Presenter : BasePresenter {
        fun loadDetail(isbn: String)
    }
}