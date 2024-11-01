package com.dicoding.asclepius.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val showImage = intent.getStringExtra(MainActivity.IMAGE_URI)
        val showLabel = intent.getStringExtra(MainActivity.LABEL)

        binding.resultImage.setImageURI(showImage?.toUri())
        binding.resultText.text = showLabel
    }

}