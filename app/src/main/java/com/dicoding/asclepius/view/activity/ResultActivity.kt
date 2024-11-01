package com.dicoding.asclepius.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ResultBinding
import com.dicoding.asclepius.view.fragment.AnalyzeFragment

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backFromDiagnose.setOnClickListener {
            finish()
        }

        val showImage = intent.getStringExtra(AnalyzeFragment.IMAGE_URI)
        val showLabel = intent.getStringExtra(AnalyzeFragment.LABEL)
        val showCofScore = intent.getStringExtra(AnalyzeFragment.SCORE)

        binding.resultImage3.setImageURI(showImage?.toUri())
        binding.resultLabel.text = getString(R.string.result_label, showLabel)
        binding.resultLabelCover.text = showLabel
        binding.resultScore.text = getString(R.string.confidence_score, showCofScore)

    }

}