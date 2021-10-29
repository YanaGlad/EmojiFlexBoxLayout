package com.example.emoji

interface ToolbarHolder {
    fun setToolbarTitle(title : String)
    fun setToolbarNavigationButtonIcon(resourceId : Int)
    fun hideToolbar()
    fun showToolbar()
}