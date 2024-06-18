package com.example.essy.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.example.essy.R
import com.example.essy.data.model.PredictResponse
import com.example.essy.data.network.ApiConfig
import com.example.essy.databinding.ActivityScanBinding
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ScanActivity : AppCompatActivity() {

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }

    private lateinit var binding: ActivityScanBinding
    private var imageFile: File? = null
    private var currentQuestionId: Int = 0 // Variable to hold the current question ID

    private val startForResultGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedImage: Uri? = result.data?.data
                selectedImage?.let {
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = contentResolver.query(it, filePathColumn, null, null, null)
                    cursor?.moveToFirst()
                    val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
                    val imagePath = cursor?.getString(columnIndex ?: 0)
                    cursor?.close()

                    imagePath?.let { path ->
                        imageFile = File(path)
                        binding.imageView.load(imageFile) {
                            transformations(CircleCropTransformation())
                        }
                    }
                }
            }
        }

    private val startForResultCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as? Bitmap
                imageBitmap?.let {
                    imageFile = bitmapToFile(it)
                    binding.imageView.setImageBitmap(it)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scan)

        val questionText = intent.getStringExtra("QUESTION_ANSWER") ?: ""
        binding.etDescription.setText(questionText)

        // Get question ID from intent
        currentQuestionId = intent.getIntExtra("QUESTION_ID", 0)

        initUi()
    }

    private fun initUi() {
        binding.customToolbar.btnBack.setOnClickListener {
            finish()
        }
        binding.customToolbar.txtTitle.text = "Scan Jawaban"

        binding.btnCamera.setOnClickListener {
            if (hasCameraPermission()) {
                dispatchTakePictureIntent()
            } else {
                requestCameraPermission()
            }
        }

        binding.btnGallery.setOnClickListener {
            dispatchPickImageIntent()
        }

        binding.btnUpload.setOnClickListener {
            imageFile?.let { file ->
                val imagePart = prepareImagePart(file)
                val jawaban = binding.etDescription.text.toString().trim()
                if (jawaban.isNotEmpty()) {
                    predictImage(imagePart, jawaban)
                } else {
                    Toast.makeText(this, "Please enter an answer", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startForResultCamera.launch(takePictureIntent)
            }
        }
    }

    private fun dispatchPickImageIntent() {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also { pickIntent ->
            pickIntent.resolveActivity(packageManager)?.also {
                startForResultGallery.launch(pickIntent)
            }
        }
    }

    private fun bitmapToFile(bitmap: Bitmap): File {
        val filesDir = applicationContext.filesDir
        val file = File(filesDir, "temp_image.jpg")
        file.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        return file
    }

    private fun prepareImagePart(file: File): MultipartBody.Part {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }

    private fun predictImage(imagePart: MultipartBody.Part, jawaban: String) {
        // Show ProgressBar
        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val response = ApiConfig.getApiServiceSecond().predict(imagePart, jawaban.toRequestBody())
                handlePredictResponse(response)
            } catch (e: Exception) {
                Toast.makeText(this@ScanActivity, "Prediction failed", Toast.LENGTH_SHORT).show()
            } finally {
                // Hide ProgressBar after completion
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun handlePredictResponse(response: PredictResponse) {
        binding.etDescription.setText(response.jawaban)
        Toast.makeText(this, "Total Nilai Jawaban: ${response.totalNilaiJawaban}", Toast.LENGTH_SHORT).show()

        // Start ResultActivity and pass the total score and question ID
        val intent = Intent(this@ScanActivity, ResultActivity::class.java)
        intent.putExtra("TOTAL_NILAI_JAWABAN", response.totalNilaiJawaban)
        intent.putExtra("QUESTION_ID", currentQuestionId) // Pass the current question ID
        startActivity(intent)
    }
}
