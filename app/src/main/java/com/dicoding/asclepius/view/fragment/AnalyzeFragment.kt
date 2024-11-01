package com.dicoding.asclepius.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.FragmentAnalyzeBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.view.activity.ResultActivity
import org.tensorflow.lite.task.vision.classifier.Classifications

class AnalyzeFragment : Fragment() {
    private var _binding: FragmentAnalyzeBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private var currentImageUri: Uri? = null
    private var labelOfImage: String? = null
    private var scoreOfImage: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyzeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let {
                analyzeImage()
            } ?: run { showToast(getString(R.string.empty_image_warning)) }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    private val launcherGallery = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            currentImageUri = it
            showImage()
        } ?: Log.d("Photo Picker", "No media selected")
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage() {
        imageClassifierHelper = ImageClassifierHelper(
            context = requireContext(),
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    requireActivity().runOnUiThread {
                        showToast(error)
                    }
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    requireActivity().runOnUiThread {
                        results?.let {
                            val result = it[0]
                            val label = result.categories[0].label
                            val score = result.categories[0].score

                            fun Float.formatToString(): String {
                                return String.format("%.2f%%", this * 100)
                            }

                            labelOfImage = label
                            scoreOfImage = score.formatToString()
                            moveToResult()
                        }
                    }
                }
            }
        )

        currentImageUri?.let { uri ->
            imageClassifierHelper.classifyStaticImage(uri)
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun moveToResult() {
        if (currentImageUri != null && labelOfImage != null && scoreOfImage != null) {
            val intent = Intent(requireContext(), ResultActivity::class.java).apply {
                putExtra(IMAGE_URI, currentImageUri.toString())
                putExtra(LABEL, labelOfImage)
                putExtra(SCORE, scoreOfImage)
            }
            startActivity(intent)
        } else {
            showToast("Unable to analyze, make sure the data required is available.")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val IMAGE_URI = "IMAGE_URI"
        const val LABEL = "LABEL"
        const val SCORE = "SCORE"
    }
}
