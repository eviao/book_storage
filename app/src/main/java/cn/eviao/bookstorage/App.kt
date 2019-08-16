package cn.eviao.bookstorage

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initFresco()
    }

    private fun initFresco() {
        Fresco.initialize(this);
    }
}