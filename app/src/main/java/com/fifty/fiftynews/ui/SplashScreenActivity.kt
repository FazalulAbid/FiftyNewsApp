package com.fifty.fiftynews.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.fifty.fiftynews.R
import com.fifty.fiftynews.Session
import com.fifty.fiftynews.models.User
import com.fifty.fiftynews.repository.UserRepository
import com.fifty.fiftynews.util.Constants.Companion.IS_USER_DEFAULT_VALUE
import com.fifty.fiftynews.util.Constants.Companion.USER_DEFAULT_COUNTRY_VALUE
import com.fifty.fiftynews.util.UiState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Session.user = User("", "", "", "us", IS_USER_DEFAULT_VALUE)
        val auth = FirebaseAuth.getInstance()

        Handler(Looper.getMainLooper()).postDelayed({
            //Open the next activity.
            val intent: Intent
            if (auth.currentUser == null) {
                startActivity(Intent(this@SplashScreenActivity, UserActivity::class.java))
                finish()
            } else {
                getUserDataToSessionAndGotoNews(auth.uid.toString())
            }
        }, 2000)
    }

    private fun getUserDataToSessionAndGotoNews(uid: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val repository = UserRepository(application)
            repository.getUserFromFireStore(uid) {
                when (it) {
                    is UiState.Failure -> {
                        com.fifty.fiftynews.Session.user = User("", "", "", USER_DEFAULT_COUNTRY_VALUE, IS_USER_DEFAULT_VALUE)
                        startActivity(
                            Intent(this@SplashScreenActivity, UserActivity::class.java)
                        )
                        finish()
                    }
                    is UiState.Loading -> {}
                    is UiState.Success -> {
                        com.fifty.fiftynews.Session.user = it.data
                        startActivity(
                            Intent(this@SplashScreenActivity, NewsActivity::class.java)
                        )
                        finish()
                    }
                }
            }
        }
    }
}