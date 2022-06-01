package com.kotlin.currentlocation.utils

import android.app.Activity
import android.widget.Toast

object GeneralUtils {

    const val TAG = "MyTag"

    fun Activity.showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}