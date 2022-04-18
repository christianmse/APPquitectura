package com.etsisi.appquitectura.presentation.ui.main.game.model

import android.graphics.Bitmap
import com.bluehomestudio.luckywheel.WheelItem

data class ItemRoulette(
    val points: Int,
    val drawable: Bitmap,
    val title: String,
    val backgroundColor: Int
) {
   fun getWidgetItem() = WheelItem(backgroundColor, drawable, title)
}
