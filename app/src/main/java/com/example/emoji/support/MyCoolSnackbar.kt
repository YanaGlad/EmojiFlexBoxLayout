package com.example.emoji.support

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.emoji.databinding.SomethingWentWrongToastBinding
import com.google.android.material.snackbar.Snackbar

/**
 * @author y.gladkikh
 */
class MyCoolSnackbar(
    private val layoutInflater: LayoutInflater,
    private val root: ViewGroup,
    private val message: String
) {

    fun makeSnackBar() : Snackbar {
        val snackBar = Snackbar.make(root, message, Snackbar.LENGTH_LONG)
        val snackBinding = SomethingWentWrongToastBinding.inflate(layoutInflater)
        snackBinding.somethingWentWrongText.text = message
        snackBar.view.setBackgroundColor(Color.TRANSPARENT)
        val params = snackBar.view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        snackBar.view.layoutParams = params

        val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout
        snackBarLayout.addView(snackBinding.root, 0)
        return snackBar
    }
}
