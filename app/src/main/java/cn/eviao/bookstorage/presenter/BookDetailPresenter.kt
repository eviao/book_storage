package cn.eviao.bookstorage.presenter

import cn.eviao.bookstorage.contract.BookDetailContract
import cn.eviao.bookstorage.model.Book
import cn.eviao.bookstorage.model.Box
import cn.eviao.bookstorage.persistence.BookDao
import cn.eviao.bookstorage.persistence.BoxDao
import cn.eviao.bookstorage.persistence.DataSource
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class BookDetailPresenter(val view: BookDetailContract.View, val isbn: String) : BookDetailContract.Presenter {

    private var compositeDisposable: CompositeDisposable

    private var bookDao: BookDao
    private var boxDao: BoxDao

    var book: Book? = null
    var box: Box? = null

    init {
        compositeDisposable = CompositeDisposable()

        val dataSource = DataSource.getInstance()
        bookDao = dataSource.bookDao()
        boxDao = dataSource.boxDao()
    }

    override fun subscribe() {
        loadBook()
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    private fun tagsFilter(book: Book): Book {
        if (book.tags.isNullOrBlank()) {
            return book
        }
        val tags = book.tags.split(";").take(3)
            .reduce { t, u -> "${t} / ${u}" }
        return book.copy(tags = tags)
    }

    override fun loadBook() {
        compositeDisposable.add(bookDao.loadBy(isbn)
            .map(::tagsFilter)
            .doOnSuccess { book = it }
            .flatMap {
                if (it.boxId != null) {
                    boxDao.loadBy(it.boxId)
                } else {
                    Maybe.just(Box.EMPTY)
                }
            }
            .doOnSuccess { box = it }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                book = null
                box = null
                view.showLoading()
            }
            .doFinally { view.hideLoading() }
            .subscribe({
                view.renderBook()
            }, {
                it.printStackTrace()
                view.showError(it.message!!)
            }))
    }

    override fun loadBoxs(){
        compositeDisposable.add(boxDao.loadAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.showUpdateBoxDialog(it)
            }, {
                it.printStackTrace()
                view.showError(it.message!!)
            }))
    }

    override fun updateBox(box: Box) {
        if (book == null) {
            throw RuntimeException("book is not loaded.")
        }

        compositeDisposable.add(bookDao.update(book!!.copy(boxId = box.id))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                loadBook()
            }, {
                it.printStackTrace()
                view.showError(it.message!!)
            }))
    }

    override fun deleteBook() {
        if (book == null) {
            throw RuntimeException("book is not loaded.")
        }

        compositeDisposable.add(bookDao.delete(book!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.startBookList()
            }, {
                it.printStackTrace()
                view.showError(it.message!!)
            }))
    }
}