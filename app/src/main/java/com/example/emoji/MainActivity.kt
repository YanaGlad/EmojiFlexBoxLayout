package com.example.emoji

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.emoji.databinding.ActivityMainBinding
import com.example.emoji.fragments.BottomSheetFragment


class MainActivity : AppCompatActivity(), ToolbarHolder {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
    }

    private fun setupToolbar() {
        binding.toolBar.title = ""
        binding.toolBar.inflateMenu(R.menu.toolbar)
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    //    supportActionBar?.setBackgroundDrawable(getColor(R.color.teal_200))
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        binding.toolBar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun setToolbarNavigationButtonIcon(resourceId: Int) {
        supportActionBar?.setHomeAsUpIndicator(resourceId)
        supportActionBar?.hide()
    }

    override fun hideToolbar() {
        supportActionBar?.hide()
    }

    override fun showToolbar() {
        supportActionBar?.show()
    }

}