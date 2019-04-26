package cn.eviao.bookstorage.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.paging.Config
import androidx.paging.toLiveData
import cn.eviao.bookstorage.data.AppDatabase

class BookListViewModel(app: Application) : AndroidViewModel(app) {
    private val bookDao = AppDatabase.get(app).bookDao()

    val allBooks = bookDao.loadAll().toLiveData(Config(
        pageSize = 20,
        enablePlaceholders = true,
        maxSize = 200
    ))

    val booksCount = bookDao.count()
}