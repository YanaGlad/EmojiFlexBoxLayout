package com.example.emoji

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private lateinit var binding: com.example.emoji.databinding.ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = com.example.emoji.databinding.ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}