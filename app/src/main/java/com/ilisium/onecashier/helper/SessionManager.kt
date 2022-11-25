package com.ilisium.onecashier.helper

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.ilisium.onecashier.BuildConfig
import com.ilisium.onecashier.model.response.EmployeesResponse
import com.ilisium.onecashier.model.response.LoginResponse
import com.ilisium.onecashier.model.response.Owner

class SessionManager(context: Context) {

    private val PRIVATE_MODE = 0

    private val PREF_NAME = BuildConfig.APPLICATION_ID
    private val PREF_IS_LOGIN = "is_login"
    private val PREF_IS_TABLET = "is_tablet"

    private val PREF_IS_LOGIN_FIRST = "is_login_first"
    private val PREF_IS_OPEN = "is_open_cashier"

    private val PREF_DATA_MEMBER = "data_member"
    private val PREF_DATA_KARYAWAN = "data_karyawan"
    private val PREF_DATA_OWNER = "data_owner"

    private val PREF_IMEI = "imei"
    private val PREF_DEVICE_ID = "device_id"
    private val PREF_USERNAME = "username"
    private val PREF_PASSWORD = "password"

    private val TOKEN = "token"

    lateinit var pref: SharedPreferences
    private val editor: SharedPreferences.Editor

    fun isLogin(bol: Boolean) {
        editor.putBoolean(PREF_IS_LOGIN, bol).apply()
    }

    fun isLogin(): Boolean {
        return pref.getBoolean(PREF_IS_LOGIN, false)
    }

    fun isLoginFirst(bol: Boolean) {
        editor.putBoolean(PREF_IS_LOGIN_FIRST, bol).apply()
    }

    fun isLoginFirst(): Boolean {
        return pref.getBoolean(PREF_IS_LOGIN_FIRST, true)
    }

    fun isTablet(bol: Boolean) {
        editor.putBoolean(PREF_IS_TABLET, bol).apply()
    }

    fun isTablet(): Boolean {
        return pref.getBoolean(PREF_IS_TABLET, false)
    }

    var isOpenCashier: Boolean
        get() {
            return pref.getBoolean(PREF_IS_OPEN, true)
        }
        set(data) {
            editor.putBoolean(PREF_IS_OPEN, data).apply()
        }

    var token: String
        get() {
            return pref.getString(TOKEN, "")?: ""
        }
        set(data) {
            editor.putString(TOKEN, data).apply()
        }

    var user: LoginResponse.Response.User?
        get() {
            return get(PREF_DATA_MEMBER)
        }
        set(data) {
            put(data, PREF_DATA_MEMBER)
        }

    var karyawan: EmployeesResponse.Karyawan?
        get() {
            return get(PREF_DATA_KARYAWAN)
        }
        set(data) {
            put(data, PREF_DATA_KARYAWAN)
        }

    var owner: Owner ?
        get() {
            return get(PREF_DATA_OWNER)
        }
        set(data) {
            put(data, PREF_DATA_OWNER)
        }

    fun saveUsername(username: String) {
        editor.putString(PREF_USERNAME, username).apply()
    }

    fun getUsername(): String {
        return pref.getString(PREF_USERNAME, "")!!
    }

    fun savePassword(pass: String) {
        editor.putString(PREF_PASSWORD, pass).apply()
    }

    fun getPassword(): String {
        return pref.getString(PREF_PASSWORD, "")!!
    }


    init {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    /**
     * Saves object into the Preferences.
     *
     * @param `object` Object of model class (of type [T]) to save
     * @param key Key with which Shared preferences to
     **/
    fun <T> put(`object`: T, key: String) {
        //Convert object to JSON String.
        val jsonString = GsonBuilder().create().toJson(`object`)
        //Save that String in SharedPreferences
        pref.edit().putString(key, jsonString).apply()
    }

    /**
     * Used to retrieve object from the Preferences.
     *
     * @param key Shared Preference key with which object was saved.
     **/
    inline fun <reified T> get(key: String): T? {
        //We read JSON String which was saved.
        val value = pref.getString(key, null)
        //JSON String was found which means object can be read.
        //We convert this JSON String to model object. Parameter "c" (of
        //type Class < T >" is used to cast.
        return GsonBuilder().create().fromJson(value, T::class.java)
    }
}
