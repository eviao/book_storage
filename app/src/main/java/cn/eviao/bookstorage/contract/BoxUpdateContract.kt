package cn.eviao.bookstorage.contract

import cn.eviao.bookstorage.model.Box
import cn.eviao.bookstorage.presenter.BasePresenter
import cn.eviao.bookstorage.ui.BaseView

interface BoxUpdateContract {

    interface View : BaseView<Presenter> {

        fun showLoading()

        fun hideLoading()

        fun showError(message: String)

        fun startBoxList()

        fun renderBox(box: Box)
    }

    interface Presenter : BasePresenter {
        fun loadBox()

        fun updateBox(box: Box)

        fun deleteBox()
    }
}