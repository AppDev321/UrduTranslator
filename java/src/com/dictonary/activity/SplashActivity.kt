package com.dictonary.activity

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.android.inputmethod.latin.databinding.ActivitySplashScreenBinding
import com.core.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashScreenBinding>(ActivitySplashScreenBinding::inflate) {
    private val activityScope = CoroutineScope(Dispatchers.Main)
    override fun initUserInterface() {
        activityScope.launch(Dispatchers.Default) {
            delay(2000)
            withContext(Dispatchers.Main)
            {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onPause() {
        activityScope.cancel()
        super.onPause()
    }
}