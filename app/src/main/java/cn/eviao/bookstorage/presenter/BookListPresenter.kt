package cn.eviao.bookstorage.presenter

import androidx.lifecycle.LiveData
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
        loadBooks(null)
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    override fun loadBooks(keyword: String?): LiveData<PagedList<Book>> {
        val loadAll = if (keyword.isNullOrBlank()) {
            bookDao.loadAll()
        } else {
            bookDao.loadAll("%${keyword}%")
        }

        return loadAll.toLiveData(Config(
            pageSize = 30,
            enablePlaceholders = true,
            maxSize = 300
        ))
    }

    override fun loadCount() {
        compositeDisposable.add(bookDao.count()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it > 0) {
                    view.setSearchHint("共 ${it} 本图书")
                } else {
                    view.showEmpty()
                }
            }, {
                view.showError(it.message ?: "获取图书信息失败")
            }))
    }
}