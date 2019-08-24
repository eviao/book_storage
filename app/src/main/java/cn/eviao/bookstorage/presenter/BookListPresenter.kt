package cn.eviao.bookstorage.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import cn.eviao.bookstorage.contract.BookListContract
import cn.eviao.bookstorage.model.Book
import cn.eviao.bookstorage.persistence.BookDao
import cn.eviao.bookstorage.persistence.DataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class BookListPresenter(val view: BookListContract.View) : BookListContract.Presenter {

    private var compositeDisposable: CompositeDisposable
    private var bookDao: BookDao

    init {
        compositeDisposable = CompositeDisposable()
        bookDao = DataSource.getInstance().bookDao()
    }

    override fun subscribe() {
        loadCount()
        loadBooks(null)
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    override fun loadBooks(keyword: String?) {
        val loadAll = if (keyword.isNullOrBlank()) {
            bookDao.loadPage()
        } else {
            bookDao.loadPage("%${keyword}%")
        }

        val config = Config(
            pageSize = 30,
            enablePlaceholders = true,
            maxSize = 300
        )
        val owner = view as LifecycleOwner
        val observer = Observer<PagedList<Book>> {
            if (it.isEmpty()) {
                view.showEmpty()
            } else {
                view.showContent()
            }
            view.getListAdapter().submitList(it)
        }

        loadAll.toLiveData(config).observe(owner, observer)
    }

    override fun loadCount() {
        compositeDisposable.add(bookDao.count()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it > 0) {
                    view.setSearchHint("共 ${it} 本图书")
                }
            }, {
                view.showError(it.message ?: "获取图书信息失败")
            }))
    }
}