package cn.eviao.bookstorage.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import cn.eviao.bookstorage.contract.BoxListContract
import cn.eviao.bookstorage.model.Box
import cn.eviao.bookstorage.persistence.BoxDao
import cn.eviao.bookstorage.persistence.DataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class BoxListPresenter(val view: BoxListContract.View) : BoxListContract.Presenter {

    private var compositeDisposable: CompositeDisposable
    private var boxDao: BoxDao

    private lateinit var box: Box

    init {
        compositeDisposable = CompositeDisposable()
        boxDao = DataSource.getInstance().boxDao()
    }

    override fun subscribe() {
        loadBoxs()
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    override fun loadBoxs() {
        val config = Config(
            pageSize = 20,
            enablePlaceholders = true,
            maxSize = 200
        )
        val owner = view as LifecycleOwner
        val observer = Observer<PagedList<Box>> {
            if (it.isEmpty()) {
                view.showEmpty()
            } else {
                view.showContent()
            }
            view.getListAdapter().submitList(it)
        }

        boxDao.loadPage().toLiveData(config).observe(owner, observer)
    }
//
//    override fun loadBoxForUpdate(id: Long) {
//        compositeDisposable.add(boxDao.loadBy(id)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                box = it
//                view.showUpdateBoxDialog(it)
//            }, {
//                view.showError(it.message ?: "加载失败")
//            }))
//    }

    override fun createBox(box: Box) {
        if (box.name.isNullOrBlank()) {
            view.showError("请输入名称")
            return
        }

        view.showSubmitLoading()

        compositeDisposable.add(boxDao.insert(box)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { view.hideSubmitLoading() }
            .subscribe({
                view.hideEditBoxDialog()
            }, {
                view.showError(it.message ?: "创建失败")
            }))
    }

    override fun updateBox(box: Box) {
        if (box.name.isNullOrBlank()) {
            view.showError("请输入名称")
            return
        }

        view.showSubmitLoading()

        compositeDisposable.add(boxDao.update(this.box.copy(name = box.name, intro = box.intro))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { view.hideSubmitLoading() }
            .subscribe({
                view.hideEditBoxDialog()
            }, {
                view.showError(it.message ?: "修改失败")
            }))
    }

    override fun deleteBox(box: Box) {
        compositeDisposable.add(boxDao.delete(box)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { view.hideSubmitLoading() }
            .subscribe({
                view.hideEditBoxDialog()
            }, {
                view.showError(it.message ?: "删除失败")
            }))
    }
}