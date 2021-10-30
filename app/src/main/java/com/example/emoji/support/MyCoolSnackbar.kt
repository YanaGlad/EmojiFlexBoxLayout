package com.example.emoji.support

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.emoji.databinding.SomethingWentWrongToastBinding
import com.google.android.material.snackbar.Snackbar

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
        val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout
        snackBarLayout.addView(snackBinding.root, 0)
        return snackBar
    }
}