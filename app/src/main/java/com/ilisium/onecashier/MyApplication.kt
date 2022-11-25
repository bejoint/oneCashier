package com.ilisium.onecashier

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.mazenrashed.printooth.Printooth
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

class MyApplication : MultiDexApplication() {

    companion object {

        var last_opened_tab = 0

        private var instance: MyApplication? = null

        fun getInstance(): MyApplication? {
            return instance
        }

        fun getContext(): Context? {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        Printooth.init(this)

        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )
//        Slider.init(GlideImageLoadingService(this))
    }



//    fun touch() {
//        ApplockManager.getInstance().updateTouch()
////        ApplockManager.getInstance().enableDefaultAppLockIfAvailable(this)
//    }

}