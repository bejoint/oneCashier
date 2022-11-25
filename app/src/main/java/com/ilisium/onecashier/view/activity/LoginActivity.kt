package com.ilisium.onecashier.view.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.ilisium.onecashier.data.viewmodel.AuthViewModel
import com.ilisium.onecashier.data.viewmodel.EmployessViewModel
import com.ilisium.onecashier.data.viewmodel.ViewModelFactory
import com.ilisium.onecashier.databinding.ActivityLoginBinding
import com.ilisium.onecashier.helper.viewBinding
import com.ilisium.onecashier.model.Status
import com.ilisium.onecashier.model.response.EmployeesResponse
import com.ilisium.onecashier.view.base.BaseActivity

class LoginActivity : BaseActivity() {

    private lateinit var authViewModel: AuthViewModel

    private val binding by viewBinding(ActivityLoginBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setOrientation()

        initViewModel()
//        loadEmployees()
        setListener()
    }

    private fun setOrientation() {
//        if (session.isTablet()) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//        } else {
//            requestedOrientation ø= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//        }
    }

    private fun initViewModel() {
        authViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(apiInterface)
        ).get(AuthViewModel::class.java)
    }

    private fun login(email: String, password: String) {
        authViewModel.login(email, password).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        val data = resource.data?.response
                        if (data!!.token.isEmpty()){
                            toast(resource.data.metadata.message.toString())
                            pLoading.dismissDialog()

                        } else {
                            session.user = data.user
                            session.isLogin(true)
                            session.token = data.token

                            toast(resource.data.metadata.message.toString())
                            pLoading.dismissDialog()

                            val i = Intent(this, HomeActivity::class.java)
                            startActivity(i)
                        }
                    }
                    Status.ERROR -> {
//                        toast(resource.data!!.message.toString())
                        pLoading.dismissDialog()
                    }
                    Status.LOADING -> {
                        pLoading.showLoading()
                    }
                }
            }
        }
    }

    private fun setListener() {

        binding.btnLogin.setOnClickListener {
            val username = binding.username.text.toString()
            val pass = binding.password.text.toString()

            if (username.isEmpty()) {
                toast("Username harus diisi!")
                return@setOnClickListener
            } else if (pass.isEmpty()) {
                toast("Password harus diisi!")
                return@setOnClickListener
            } else {
                login(username, pass)
//                val i = Intent(this, PinCodeActivity::class.java)
//                val i = Intent(this, HomeActivity::class.java)
//                startActivity(i)
            }
        }

//        binding.txtRegister?.setOnClickListener {
//            val i = Intent(this, RegisterActivity::class.java)
//            startActivity(i)
//        }
//
//        binding.txtForgotPass?.setOnClickListener {
//            val i = Intent(this, ForgotPasswordActivity::class.java)
//            startActivity(i)
//        }
    }
}