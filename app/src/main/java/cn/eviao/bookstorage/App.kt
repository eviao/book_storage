package cn.eviao.bookstorage

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.facebook.drawee.backends.pipeline.Fresco

class App : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null
        fun getContext(): Context = context!!
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        initFresco()
    }

    private fun initFresco() {
        Fresco.initialize(this);
    }
}