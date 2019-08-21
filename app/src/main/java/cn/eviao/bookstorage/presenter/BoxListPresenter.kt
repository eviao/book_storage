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

        boxDao.loadAll().toLiveData(config).observe(owner, observer)
    }

    override fun createBox(name: String) {
        compositeDisposable.add(boxDao.insert(Box(name = name))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.hideCreateBoxDialog()
            }, {
                view.showError(it.message ?: "创建失败")
            }))
    }
}