package cn.eviao.bookstorage.presenter

import cn.eviao.bookstorage.contract.ScannerContract
import cn.eviao.bookstorage.persistence.BookDao
import cn.eviao.bookstorage.persistence.DataSource
import cn.eviao.bookstorage.utils.isValidISBN
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ScannerPresenter(val view: ScannerContract.View) : ScannerContract.Presenter {

    private var compositeDisposable: CompositeDisposable
    private var bookDao: BookDao

    init {
        compositeDisposable = CompositeDisposable()
        bookDao = DataSource.getInstance().bookDao()
    }

    override fun subscribe() {}

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    override fun loadBook(isbn: String) {
        if (!isValidISBN(isbn)) {
            view.showInvalidISBN(isbn)
            view.restartScanning()
            return
        }

        compositeDisposable.add(bookDao.countBy(isbn)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { view.showLoading() }
            .doFinally { view.hideLoading() }
            .subscribe({
                if (it > 0) {
                    view.startBookDetail(isbn)
                } else {
                    view.startFetchDetail(isbn)
                }
            }, {
                it.printStackTrace()
                view.showError(it.message!!)
            }))
    }
}