package com.etsisi.appquitectura.presentation.common

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.navigation.NavController
import com.etsisi.appquitectura.LoginDirections
import com.etsisi.appquitectura.MainDirections
import com.etsisi.appquitectura.presentation.dialog.enums.DialogType
import com.etsisi.appquitectura.presentation.dialog.model.DialogConfig
import com.etsisi.appquitectura.presentation.ui.login.view.formscreen.LoginFormFragmentDirections
import com.etsisi.appquitectura.presentation.ui.main.game.model.ItemGameModeAction
import com.etsisi.appquitectura.presentation.ui.main.MainActivity
import com.etsisi.appquitectura.presentation.ui.main.game.view.PlayFragmentDirections
import com.etsisi.appquitectura.presentation.ui.main.home.model.ItemHome
import com.etsisi.appquitectura.presentation.ui.main.home.model.ItemHomeAction
import com.etsisi.appquitectura.presentation.ui.main.home.view.HomeFragmentDirections
import com.etsisi.appquitectura.presentation.utils.startClearActivity
import com.etsisi.appquitectura.presentation.utils.toLabeledIntentArray

class Navigator (private val navController: NavController){

    fun openNavigationDialog(config: DialogConfig) {
        val directions = LoginDirections.navigateLoginDialog(config)
        navController.navigate(directions)
    }
    fun openEditTextDialog(config: DialogConfig, dialogType: DialogType) {
        val directions = LoginFormFragmentDirections.openInputTextDialog(dialogType,config)
        navController.navigate(directions)
    }
    fun openRegisterFragment() {
        val directions = LoginFormFragmentDirections.navigateToRegisterFormFragment()
        navController.navigate(directions)
    }

    fun openVerifyEmailFragment(name: String) {
        val directions = LoginDirections.navigateToEmailVerificationFragment(name)
        navController.navigate(directions)
    }

    fun navigateFromLoginToMain(activity: Activity) {
        with(activity) {
            startClearActivity<MainActivity>()
        }
    }

    fun navigateFromMainToLogin() {
        val directions = MainDirections.actionMainToLogin()
        navController.navigate(directions)
    }

    fun startGame(gameMode: ItemGameModeAction) {
        val directions = PlayFragmentDirections.actionGameModeToPlay(gameMode = gameMode)
        navController.navigate(directions)
    }

    fun openSection(item: ItemHome) {
        val directions = when(item.action) {
            ItemHomeAction.ABOUT -> { HomeFragmentDirections.actionHomeToSettings() }
            ItemHomeAction.ANALYTICS -> { HomeFragmentDirections.actionHomeToSettings() }
            ItemHomeAction.PROFILE -> { HomeFragmentDirections.actionHomeToSettings() }
            ItemHomeAction.RANKING -> { HomeFragmentDirections.actionHomeToSettings() }
            ItemHomeAction.CONFIGURATION -> { HomeFragmentDirections.actionHomeToSettings() }
            ItemHomeAction.START_GAME -> { HomeFragmentDirections.actionHomeToPlay() }
        }
        navController.navigate(directions)
    }

    fun openInboxMail(activity: Activity) {
        with(activity) {
            val emailIntent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"))
            val resInfo = packageManager.queryIntentActivities(emailIntent, PackageManager.MATCH_ALL)
            val intentChooser = packageManager.getLaunchIntentForPackage(resInfo.first().activityInfo.packageName)
            val openChooser = Intent.createChooser(intentChooser, "Selecciona")
            val emailApps = resInfo.toLabeledIntentArray(packageManager)

            openChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, emailApps)
            startActivity(openChooser)
        }
    }

    fun openLeavingGameDialog() {

    }

}