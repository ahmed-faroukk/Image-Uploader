package com.example.retrofitimage.UI

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.retrofitimage.R
import com.example.retrofitimage.Util.MultiPartUtil
import com.example.retrofitimage.data.ApiInterface
import com.example.retrofitimage.data.Image
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody


lateinit var GallaryBtn: Button
lateinit var upload: Button
lateinit var imageView: ImageView
lateinit var imgUri: Uri
var check = false
var i = 0
lateinit var progressBar: ProgressBar
val REQUEST_CODE = 100
var x = 1

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GallaryBtn = findViewById(R.id.Gallary)
        upload = findViewById(R.id.uploadBtn)
        imageView = findViewById(R.id.imageView)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.INVISIBLE

        //object
        GallaryBtn.setOnClickListener(View.OnClickListener {

            openGalleryForImage()
        })

        upload.setOnClickListener(View.OnClickListener {

            if (check == false) {
                Toast.makeText(this, "select image", Toast.LENGTH_SHORT).show()

            } else {
                val image = Image(imgUri)
                uploadImg(image)
            }

        })


    }

    fun uploadImg(image: Image) {

        // TODO replace with image uri
        val multiPartPhoto =
            MultiPartUtil.fileToMultiPart(
                applicationContext, image.Url,
                "uploaded-file"
            )

        //convert String to part
        val mediaType = "multipart/form-data".toMediaType()

        val myText = System.currentTimeMillis().toString()
            .toRequestBody(mediaType)

        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            ApiInterface.invoke().UploadPhoto(multiPartPhoto)
            progressBar.visibility = View.INVISIBLE

        }.invokeOnCompletion {
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show()
            imageView.setImageResource(R.drawable.ic_baseline_person_24)
        }


    }

    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            imageView.setImageURI(data?.data) // handle chosen image
            imgUri = data?.data!!
            check = true


        }

    }

    fun openGalleryForImage() {
        val i = intent
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        this.startActivityForResult(intent, REQUEST_CODE)

    }


}

