package cn.eviao.bookstorage.model

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table

@Table(name = "authors")
data class Author(
    @Column(name = "name", unique = true, notNull = true, index = true) val name: String
) : Model()
