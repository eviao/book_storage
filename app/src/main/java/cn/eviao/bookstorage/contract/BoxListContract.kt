package cn.eviao.bookstorage.contract

import cn.eviao.bookstorage.presenter.BasePresenter
import cn.eviao.bookstorage.ui.BaseView
import cn.eviao.bookstorage.ui.adapter.BoxListAdapter

interface BoxListContract {

    interface View : BaseView<Presenter> {

        fun showEmpty()

        fun showContent()

        fun showError(message: String)

        fun hideCreateBoxDialog()

        fun getListAdapter(): BoxListAdapter
    }

    interface Presenter : BasePresenter {
        fun loadBoxs()

        fun createBox(name: String)
    }
}