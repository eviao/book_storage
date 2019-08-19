package cn.eviao.bookstorage

import android.app.Application
import android.content.Context
import com.facebook.drawee.backends.pipeline.Fresco

class App : Application() {

    companion object {
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