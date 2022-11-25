package com.ilisium.onecashier.view.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import com.ilisium.onecashier.databinding.ActivitySplashScreenBinding
import com.ilisium.onecashier.helper.viewBinding
import com.ilisium.onecashier.view.base.BaseActivity

class SplashScreenActivity : BaseActivity() {
    private val binding by viewBinding(ActivitySplashScreenBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        Log.d("isTablet", isTablet(this).toString())

        setOrientation()
        Handler().postDelayed({

//            isTablet(this)

            if (session.isLogin()){
                val i = Intent(this@SplashScreenActivity, HomeActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

                startActivity(i)
                finish()
            } else {
                val i = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

                startActivity(i)
                finish()
            }



//            if (isTablet(this)){
////                toast("Detected... You're using a Tablet")
//
//                if (session.isLogin()){
//                    val i = Intent(this@SplashScreenActivity, HomeActivity::class.java)
//                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//
//                    startActivity(i)
//                    finish()
//                } else {
//                    val i = Intent(this@SplashScreenActivity, LoginActivity::class.java)
//                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//
//                    startActivity(i)
//                    finish()
//                }
//
//            } else {
////                toast("Detected... You're using a Mobile Phone")
//                alertNotTablet()
//            }

        }, SPLASH_TIME_OUT.toLong())
    }

    private fun setOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    fun alertNotTablet() {
        val builder1: AlertDialog.Builder = AlertDialog.Builder(this)
        builder1.setMessage("Aplikasi hanya untuk Tablet.")
        builder1.setCancelable(true)
        builder1.setPositiveButton(
            "OK"
        ) { dialog, id ->
            dialog.cancel()
            finish()
        }
//        builder1.setNegativeButton(
//            "Tidak"
//        ) { dialog, id -> dialog.cancel() }
        val alert11: AlertDialog = builder1.create()
        alert11.show()
    }

    companion object {
        // Splash screen timer
        private const val SPLASH_TIME_OUT = 1000
    }

}
