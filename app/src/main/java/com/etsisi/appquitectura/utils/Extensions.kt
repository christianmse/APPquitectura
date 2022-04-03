package com.etsisi.appquitectura.presentation.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.LabeledIntent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import org.json.JSONException
import org.json.JSONObject

val String.Companion.EMPTY: String
    get() = ""

inline fun <reified T: Activity> Activity.startClearActivity(args: Bundle? = null) {
    val intent = Intent(this, T::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    args?.let {
        intent.putExtras(it)
    }
    startActivity(intent)
}

inline val <reified T> T.TAG: String
    get() = T::class.java.canonicalName ?: T::class.simpleName ?: T::class.java.simpleName

fun Context.showKeyboard(view: View) {
    if (view.requestFocus()) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun Context.hideKeyboard(view: View) {
    if (view.hasFocus()) {
        view.clearFocus()
    }
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun FragmentActivity.checkDialogOpened(): Boolean {
    var ret = false
    supportFragmentManager.fragments.filterIsInstance<DialogFragment>().forEach {
        if (it.isCancelable) it.dismiss()
        else ret = true
    }
    return ret
}

fun Bundle.toJson(): JSONObject {
    val json = JSONObject()
    val keys = this.keySet()
    for (key in keys) {
        try {
            json.put(key, this.get(key))
        } catch (e: JSONException) {
            Log.e("toJson",e.toString())
        }
    }

    return json
}

fun List<ResolveInfo>.toLabeledIntentArray(packageManager: PackageManager): Array<LabeledIntent> = map {
    val packageName = it.activityInfo.packageName
    val intent = packageManager.getLaunchIntentForPackage(packageName)
    LabeledIntent(intent, packageName, it.loadLabel(packageManager), it.icon)
}.toTypedArray()