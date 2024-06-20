package com.example.essy.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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
    private lateinit var photoFile: File
    private var currentQuestionId: Int = 0 // Variable to hold the current question ID

    private val startForResultGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val selectedImage: Uri? = data?.data
            selectedImage?.let {
                binding.imageView.setImageURI(it)
                photoFile = uriToFile(it)
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
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startForResultGallery.launch(galleryIntent)
        }

        binding.btnUpload.setOnClickListener {
            if (this::photoFile.isInitialized || imageFile != null) {
                val selectedFile = imageFile ?: photoFile
                val imagePart = prepareImagePart(selectedFile)
                val jawaban = binding.etDescription.text.toString().trim()
                if (jawaban.isNotEmpty()) {
                    predictImage(imagePart, jawaban)
                } else {
                    Toast.makeText(this, "Silakan kembali ke menu utama untuk memilih jawaban", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Silakan pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
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
        binding.etDescription.setText(response.hasilOcr)
        Toast.makeText(this, "Total Nilai Jawaban: ${response.totalNilaiJawaban}", Toast.LENGTH_SHORT).show()

        // Log untuk memastikan nilai dikirim dengan benar
        Log.d("ScanActivity", "Total Nilai Jawaban dikirim: ${response.totalNilaiJawaban}")

        // Start ResultActivity dan kirim total score dan question ID
        val intent = Intent(this@ScanActivity, ResultActivity::class.java).apply {
            putExtra("TOTAL_NILAI_JAWABAN", response.totalNilaiJawaban)
            putExtra("QUESTION_ID", currentQuestionId)
        }
        startActivity(intent)
    }

    private fun uriToFile(uri: Uri): File {
        val contentResolver = contentResolver
        val myFile = File.createTempFile("temp_image", null, cacheDir)
        contentResolver.openInputStream(uri)?.use { inputStream ->
            myFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return myFile
    }


}
