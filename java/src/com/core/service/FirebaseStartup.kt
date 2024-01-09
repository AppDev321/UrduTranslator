package com.core.service

import android.content.Context
import android.util.Log
import androidx.startup.Initializer


class FirebaseStartup : Initializer<Unit> {
    override fun create(context: Context) {
        Log.e("Service", "FirebaseApp starting")
        /* val firebaseApp = FirebaseApp.initializeApp(context)
         if (firebaseApp == null) {
             Log.e("Service", "FirebaseApp initialization unsuccessful")
         }
         Log.e("Service",  "FirebaseApp initialization successful")*/

    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }

}