package cn.eviao.bookstorage.presenter

import cn.eviao.bookstorage.contract.BookDetailContract
import cn.eviao.bookstorage.model.Book
import cn.eviao.bookstorage.persistence.BookDao
import cn.eviao.bookstorage.persistence.BoxDao
import cn.eviao.bookstorage.persistence.DataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class BookDetailPresenter(val view: BookDetailContract.View, val isbn: String) : BookDetailContract.Presenter {

    private var compositeDisposable: CompositeDisposable
    private var bookDao: BookDao
    private var boxDao: BoxDao

    init {
        compositeDisposable = CompositeDisposable()

        val ds = DataSource.getInstance()
        bookDao = ds.bookDao()
        boxDao = ds.boxDao()
    }

    override fun subscribe() {
        loadBook()
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    private fun tagsFilter(b: Book): Book {
        if (b.tags.isNullOrBlank()) {
            return b
        }
        val tags = b.tags.split(";").take(3)
            .reduce { t, u -> "${t} / ${u}" }
        return b.copy(tags = tags)
    }

    override fun loadBook() {
        view.showLoading()

        compositeDisposable.add(bookDao.loadBy(isbn)
            .map(::tagsFilter)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(::loadBoxs)
            .doFinally { view.hideLoading() }
            .subscribe({
                view.renderBook(it)
            }, {
                view.showError(it.message ?: "加载失败")
            }))
    }

    fun loadBoxs(book: Book) {
        compositeDisposable.add(boxDao.loadAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.createUpdateBoxDialog(it, book)
            }, {
                view.showError(it.message ?: "加载失败")
            }))
    }
}