package com.dicoding.asclepius.view.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.databinding.FragmentAnalyzeBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.view.activity.ResultActivity
import com.dicoding.asclepius.viewmodels.HistoryIndicatedViewModel
import com.dicoding.asclepius.viewmodels.HistoryIndicatedViewModelFactory
import androidx.navigation.fragment.findNavController
import com.dicoding.asclepius.createImageUri
import com.dicoding.asclepius.viewmodels.TopHeadlinesViewModel
import com.dicoding.asclepius.viewmodels.TopHeadlinesViewModelFactory
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File

class AnalyzeFragment : Fragment() {
    private var _binding: FragmentAnalyzeBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private var currentImageUri: Uri? = null
    private var labelOfImage: String? = null
    private var scoreOfImage: String? = null

    private val viewModel: HistoryIndicatedViewModel by viewModels {
        HistoryIndicatedViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyzeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let {
                analyzeImage()
            } ?: run { showToast(getString(R.string.empty_image_warning)) }
        }

        binding.iconHistory.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_analyze_to_navigation_history)
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
            startCrop(it)
        } ?: Log.d("Photo Picker", "No media selected")
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private val launcherUCrop = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val resultUri = UCrop.getOutput(result.data!!)
            if (resultUri != null) {
                currentImageUri = resultUri
                showImage()
            } else {
                showToast("Crop gagal, coba lagi.")
                Log.e("AnalyzeFragment", "Error: URI hasil crop null.")
            }
        } else if (result.resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(result.data!!)
            showToast("Error: ${cropError?.message}")
            Log.e("AnalyzeFragment", "UCrop Error: ${cropError?.message}")
        } else {
            Log.e("AnalyzeFragment", "Result code tidak dikenali: ${result.resultCode}")
        }
    }

    private fun startCrop(uri: Uri) {
        // Panggil createImageUri untuk mendapatkan URI baru
        val destinationUri = createImageUri(requireContext())
        if (destinationUri != null) {
            // Melanjutkan dengan UCrop
            val uCropIntent = UCrop.of(uri, destinationUri)
                .withAspectRatio(1f, 1f)
                .withMaxResultSize(1080, 1080)
                .getIntent(requireContext())
            launcherUCrop.launch(uCropIntent)
        } else {
            Log.e("AnalyzeFragment", "Gagal membuat URI untuk gambar.")
            showToast("Gagal membuat URI untuk gambar. Silakan coba lagi.")
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

                            val history = HistoryEntity(
                                historyOfImage = currentImageUri.toString(),
                                historyOfLabel = label,
                                historyOfScore = score.formatToString()
                            )
                            viewModel.insert(history)
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
