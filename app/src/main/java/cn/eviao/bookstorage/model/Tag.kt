package cn.eviao.bookstorage.model

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table

@Table(name = "tags")
data class Tag(
    @Column(name = "text", unique = true, notNull = true, index = true) val text: String
) : Model()