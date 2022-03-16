package com.etsisi.appquitectura.presentation.common

import androidx.navigation.NavController
import com.etsisi.appquitectura.LoginDirections
import com.etsisi.appquitectura.MainDirections
import com.etsisi.appquitectura.domain.enums.LoginNavType
import com.etsisi.appquitectura.presentation.dialog.model.DialogConfig
import com.etsisi.appquitectura.presentation.ui.login.view.formscreen.LoginFormFragmentDirections
import com.etsisi.appquitectura.presentation.ui.main.model.ItemHome
import com.etsisi.appquitectura.presentation.ui.main.model.ItemHomeAction
import com.etsisi.appquitectura.presentation.ui.main.view.HomeFragmentDirections
import com.etsisi.appquitectura.presentation.ui.main.view.PlayFragmentDirections
import com.etsisi.appquitectura.presentation.ui.main.view.SettingsFragmentDirections

class Navigator (private val navController: NavController){

    fun openDialog(config: DialogConfig) {
        val directions = LoginFormFragmentDirections.navigateLoginDialog(config)
        navController.navigate(directions)
    }
    fun openRegisterFragment() {
        val directions = LoginFormFragmentDirections.navigateToForm(navType = LoginNavType.REGISTER)
        navController.navigate(directions)
    }

    fun openVerifyEmailFragment(emailVerified: Boolean = false) {
        val directions = LoginFormFragmentDirections.navigateToEmailVerificationFragment(emailVerified)
        navController.navigate(directions)
    }

    fun navigateFromLoginToMain() {
        val directions = LoginDirections.navigateLoginToMain()
        navController.navigate(directions)
    }

    fun navigateFromMainToLogin() {
        val directions = MainDirections.actionMainToLogin()
        navController.navigate(directions)
    }

    fun startGame() {
        val directions = PlayFragmentDirections.actionGameModeToPlay()
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

}