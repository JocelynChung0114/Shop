package com.toplogis.shop

import android.app.Activity
import android.content.Context

fun Activity.setPreferences(key: String, value: String){
    getSharedPreferences("Shop", Context.MODE_PRIVATE)
        .edit()
        .putString(key, value)
        .apply()
}

fun Activity.getPreferences(key: String) :String{
    return getSharedPreferences("Shop", Context.MODE_PRIVATE).getString(key, "")
}