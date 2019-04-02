package cn.eviao.bookstorage.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.paging.Config
import androidx.paging.toLiveData
import cn.eviao.bookstorage.persistence.AppDatabase

class BookListViewModel(app: Application) : AndroidViewModel(app) {
    private val bookDao = AppDatabase.get(app).bookDao()

    val allBooks = bookDao.findAll().toLiveData(Config(
        pageSize = 20,
        enablePlaceholders = true,
        maxSize = 200
    ))
}