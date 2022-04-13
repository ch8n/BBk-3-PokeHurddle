package io.github.ch8n

import android.view.View

fun View.setVisible(isVisible: Boolean) {
    if (isVisible) {
        visibility = View.VISIBLE
    } else {
        visibility = View.GONE
    }
}