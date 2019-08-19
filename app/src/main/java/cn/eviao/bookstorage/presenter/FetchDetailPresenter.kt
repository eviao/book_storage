package cn.eviao.bookstorage.presenter

import cn.eviao.bookstorage.api.Api
import cn.eviao.bookstorage.api.impl.DoubanCrawlImpl
import cn.eviao.bookstorage.contract.FetchDetailContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FetchDetailPresenter(
    val view: FetchDetailContract.View,
    val isbn: String
) : FetchDetailContract.Presenter {

    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var api: Api

    init {
        compositeDisposable = CompositeDisposable()
        api = DoubanCrawlImpl()
    }

    override fun subscribe() {
        loadBook()
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    override fun loadBook() {
        view.showLoading()

        compositeDisposable.add(api.fetch(isbn)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.renderBook(it)
            }, {
                println("==============")
                it.printStackTrace()
            }, {
                view.hideLoading()
            }))
    }
}