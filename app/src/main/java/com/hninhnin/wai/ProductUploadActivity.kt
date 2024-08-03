package com.hninhnin.wai

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import com.hninhnin.wai.libby.Helper
import com.hninhnin.wai.libby.Helper.Companion.USER_TOKEN
import com.hninhnin.wai.models.ErrorResponse
import com.hninhnin.wai.models.FileInfo
import com.hninhnin.wai.services.ServiceBuilder
import com.hninhnin.wai.services.WebService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.InputStream

class ProductUploadActivity : AppCompatActivity() {
    private lateinit var uploadProductImg : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Upload Product Image"

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product_upload)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        uploadProductImg = findViewById(R.id.uploadProductImg)
        uploadProductImg.setOnClickListener {
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
            ), 101)
        }
    }

    // check permssion grantend
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            101 -> {
                if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show()
                }else {
                    fileUpload()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    // file upload
    private fun fileUpload() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Choose Image"), 103)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 103 && resultCode == Activity.RESULT_OK && data !=null) {
            val data : Uri = data.data!!
            val inst : InputStream = contentResolver.openInputStream(data)!!
            val bitMap = BitmapFactory.decodeStream(inst)
            uploadProductImg.setImageBitmap(bitMap)
            var imagePath = getImagePath(data)
//            Toast.makeText(this, imagePath, Toast.LENGTH_SHORT).show()
            Helper.debugLog(imagePath)

            var file = File(imagePath)

            val requestBody: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val body: MultipartBody.Part = MultipartBody.Part.createFormData("image", file.name, requestBody)

            val services: WebService = ServiceBuilder.buildService(WebService::class.java)
            val responseUpload: Call<FileInfo> = services.imageUpload("Bearer $USER_TOKEN", body)

            responseUpload.enqueue(object : Callback<FileInfo> {
                override fun onResponse(p0: Call<FileInfo>, p1: Response<FileInfo>) {
                    Helper.debugLog("Upload response")
                    if(p1.isSuccessful) {
                        val info: FileInfo = p1.body()!!
                        Toast.makeText(this@ProductUploadActivity, info.name, Toast.LENGTH_LONG).show()
                        uploadProduct(info.name)
                    }else {
                        Toast.makeText(this@ProductUploadActivity, "Something went wrong", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(p0: Call<FileInfo>, p1: Throwable) {
                    Toast.makeText(this@ProductUploadActivity, p1.message, Toast.LENGTH_LONG).show()
                }

            })
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    // upload product
    private fun uploadProduct(image: String) {
        val cart_id = 1
        val name = "New More Product"
        val price = 20.0
        val description = "This is new product"

        val services: WebService = ServiceBuilder.buildService(WebService::class.java)
        val responseProduct : Call<ErrorResponse> = services.newProductUpload(
            "Bearer $USER_TOKEN",
            cart_id,
            name,
            price,
            image,
            description
        )

        responseProduct.enqueue(object : Callback<ErrorResponse> {
            override fun onResponse(p0: Call<ErrorResponse>, p1: Response<ErrorResponse>) {
                if(p1.isSuccessful) {
                    val message: ErrorResponse = p1.body()!!
                    Helper.debugLog(message.msg)
                    Toast.makeText(this@ProductUploadActivity, message.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(p0: Call<ErrorResponse>, p1: Throwable) {
                Toast.makeText(this@ProductUploadActivity, p1.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    // get image file path
    private fun getImagePath(uri: Uri) :String {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, filePathColumn, null, null, null, null)
        cursor!!.moveToFirst()
        val columnIntex = cursor.getColumnIndex(filePathColumn[0])
        val mediaPath = cursor.getString(columnIntex)
        cursor.close()
        return mediaPath
    }

    // action bar menu item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // app action bar back button
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}