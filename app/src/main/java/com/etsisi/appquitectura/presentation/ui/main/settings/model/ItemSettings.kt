package com.etsisi.appquitectura.presentation.ui.main.settings.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class ItemSettings (
    @StringRes
    val title: Int,
    @DrawableRes
    val icon: Int,
    val action: ItemSettingsAction,
    val hasArrow: Boolean
    )

enum class ItemSettingsAction { LOG_OUT, UPDATE_QUESTIONS, ENABLE_REPEATING_MODE }