package cn.eviao.bookstorage.presenter

import android.annotation.SuppressLint
import cn.eviao.bookstorage.contract.BoxUpdateContract
import cn.eviao.bookstorage.model.Box
import cn.eviao.bookstorage.persistence.BoxDao
import cn.eviao.bookstorage.persistence.DataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class BoxUpdatePresenter(val view: BoxUpdateContract.View, val id: Long) : BoxUpdateContract.Presenter {

    private var compositeDisposable: CompositeDisposable
    private var boxDao: BoxDao

    private lateinit var box: Box

    init {
        compositeDisposable = CompositeDisposable()
        boxDao = DataSource.getInstance().boxDao()
    }

    override fun subscribe() {
        loadBox()
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    override fun loadBox() {
        view.showLoading()

        compositeDisposable.add(boxDao.loadBy(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { view.hideLoading() }
            .subscribe({
                box = it
                view.renderBox(it)
            }, {
                view.showError(it.message ?: "加载失败")
            }))
    }

    @SuppressLint("CheckResult")
    override fun updateBox(b: Box) {
        compositeDisposable.add(boxDao.update(box.copy(name = b.name, intro = b.intro))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.startBoxList()
            }, {
                view.showError(it.message ?: "修改失败")
            }))
    }

    override fun deleteBox() {
        compositeDisposable.add(boxDao.delete(box)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.startBoxList()
            }, {
                view.showError(it.message ?: "删除失败")
            }))
    }
}