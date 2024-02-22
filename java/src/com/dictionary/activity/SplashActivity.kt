package com.dictionary.activity

import android.content.Intent
import com.android.inputmethod.latin.databinding.ActivitySplashScreenBinding
import com.core.base.BaseActivity
import com.core.extensions.hide
import com.core.extensions.invisible
import com.core.extensions.show
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
        viewDataBinding.btnStart.hide()
        activityScope.launch(Dispatchers.Default) {
            delay(2000)
            withContext(Dispatchers.Main)
            {
                viewDataBinding.spinKit.invisible()
                viewDataBinding.btnStart.apply {
                    show()
                    setOnClickListener{
                        val intent = Intent(this@SplashActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    override fun onPause() {
        activityScope.cancel()
        super.onPause()
    }


}