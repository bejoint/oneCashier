package com.ilisium.onecashier.view.activity

import android.content.Intent
import android.os.Bundle
import com.ilisium.onecashier.databinding.ActivityForgotPasswordBinding
import com.ilisium.onecashier.helper.viewBinding
import com.ilisium.onecashier.view.base.BaseActivity

class ForgotPasswordActivity : BaseActivity() {
    private val binding by viewBinding(ActivityForgotPasswordBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setListener()
    }

    private fun setListener(){
        binding.btnReset.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }

        //
    }
}