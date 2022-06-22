package com.example.weatherforecastapp.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

/**
 * simple function to show a long snackbar
 * @param view : view to show snackbar in
 * @param message : message to show in the snackbar
 */
fun makeSnackbar(view : View, message : String){
    Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        .show()
}