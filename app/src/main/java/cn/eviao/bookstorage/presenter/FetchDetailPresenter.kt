package cn.eviao.bookstorage.presenter

import android.annotation.SuppressLint
import cn.eviao.bookstorage.api.Api
import cn.eviao.bookstorage.api.impl.DoubanCrawlImpl
import cn.eviao.bookstorage.contract.FetchDetailContract
import cn.eviao.bookstorage.model.Book
import cn.eviao.bookstorage.persistence.BookDao
import cn.eviao.bookstorage.persistence.DataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FetchDetailPresenter(
    val view: FetchDetailContract.View,
    val isbn: String
) : FetchDetailContract.Presenter {

    private var compositeDisposable: CompositeDisposable

    private var api: Api
    private var bookDao: BookDao

    private var loading = false

    var book: Book? = null

    init {
        compositeDisposable = CompositeDisposable()

        api = DoubanCrawlImpl()
        bookDao = DataSource.getInstance().bookDao()
    }

    override fun subscribe() {
        loadBook()
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    override fun loadBook() {
        if (loading) {
            return
        }

        compositeDisposable.add(api.fetch(isbn)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                book = null
                loading = true
                view.showSkeleton()
            }
            .doFinally { loading = false }
            .subscribe({
                book = it
                view.renderBook()
            }, {
                it.printStackTrace()
                view.showError(it.message!!)
            }))
    }

    @SuppressLint("CheckResult")
    override fun saveBook() {
        if (book == null) {
            throw RuntimeException("book is not loaded.")
        }

        compositeDisposable.add(bookDao.insert(book!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.showSubmitLoading()
                view.disableSubmitButton()
            }
            .doFinally {
                view.hideSubmitLoading()
            }
            .subscribe({
                view.startBookList()
            }, {
                it.printStackTrace()
                view.showError(it.message!!)
            }))
    }
}