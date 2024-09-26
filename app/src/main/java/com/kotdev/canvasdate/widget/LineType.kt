package com.kotdev.canvasdate.widget

sealed class LineType {
    object Normal : LineType()
    object FiveStep : LineType()
    object TenStep : LineType()
}