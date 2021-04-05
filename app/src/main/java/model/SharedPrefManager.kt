package model

import android.accounts.AccountManager.KEY_PASSWORD
import android.content.Context
import android.content.Intent
import activity.Sign_In

class SharedPrefManager private constructor(context: Context) {

    //this method will checker whether user is already logged in or not
    val isLoggedIn: Boolean
        get() {
            val sharedPreferences =
                ctx?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences?.getString(KEY_NAME, null) != null
        }

    //this method will give the logged in user
    val user: User
        get() {
            val sharedPreferences =
                ctx?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return User(
                sharedPreferences!!.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_NAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_GENDER, null),
                sharedPreferences.getString(KEY_PASSWORD, null),
                sharedPreferences.getString(KEY_MOBILE, null),
                sharedPreferences.getString(KEY_CONFIRMPASS,null),
                sharedPreferences.getString(KEY_ADDRESS,null)


            )
        }



    init {
        ctx = context
    }

    //this method will store the user data in shared preferences
    fun userLogin(user: User) {
        val sharedPreferences = ctx?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        user.id?.let { editor?.putInt(KEY_ID, it) }
        editor?.putString(KEY_NAME, user.name)
        editor?.putString(KEY_EMAIL, user.email)
        editor?.putString(KEY_GENDER, user.gender)
        editor?.putString(KEY_MOBILE, user.mnumber.toString())
        editor?.putString(KEY_PASSWORD, user.password)
editor?.putString(KEY_CONFIRMPASS,user.confirm_password)
        editor?.putString(KEY_ADDRESS, user.address)



        editor?.apply()
    }

    //this method will logout the user
    fun logout() {
        val sharedPreferences = ctx?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.clear()
        editor?.apply()
        ctx?.startActivity(Intent(ctx, Sign_In::class.java))
    }

    companion object {

        private val SHARED_PREF_NAME = "volleyregisterlogin"
       private val KEY_MOBILE="keymnumber"
        private val KEY_EMAIL = "keyemail"
        private val KEY_GENDER = "keygender"
        private val KEY_ID = "keyid"
        private val KEY_NAME="keyname"
        private val KEY_CONFIRMPASS="keyconfirmpassword"
        private val KEY_ADDRESS="keyaddress"


        private var mInstance: SharedPrefManager? = null
        private var ctx: Context? = null

        @Synchronized
        fun getInstance(context: Context): SharedPrefManager {
            if (mInstance == null) {
                mInstance = SharedPrefManager(context)
            }
            return mInstance as SharedPrefManager
        }
    }
}