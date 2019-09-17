package cn.eviao.bookstorage.contract

import cn.eviao.bookstorage.base.BasePresenter
import cn.eviao.bookstorage.base.BaseView
import cn.eviao.bookstorage.model.Box
import cn.eviao.bookstorage.ui.adapter.BoxListAdapter

interface BoxListContract {

    interface View : BaseView<Presenter> {

        fun showEmpty()

        fun showContent()

        fun showError(message: String)

        fun showSubmitLoading()

        fun hideSubmitLoading()

        fun showToast(message: String)

        fun hideEditBoxDialog()

        fun getListAdapter(): BoxListAdapter
    }

    interface Presenter : BasePresenter {

        fun loadBoxs()

        fun createBox(box: Box)

        fun updateBox(box: Box)

        fun deleteBox(box: Box)
    }
}