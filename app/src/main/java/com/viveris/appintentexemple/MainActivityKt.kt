package com.viveris.appintentexemple

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivityKt : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_send_mail.setOnClickListener { sendMail() }

        btn_send_sms.setOnClickListener { sendSms() }

        btn_call.setOnClickListener { call() }

        btn_open_browser.setOnClickListener { openBrowser() }

        btn_open_maps.setOnClickListener { openMaps() }

        btn_take_picture.setOnClickListener { takePicture() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            data.extras?.let { extras ->
                (extras["data"] as? Bitmap)?.let {imageBitmap ->
                    img_picture.setImageBitmap(imageBitmap)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PHONE_CALL && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            call()
        }
    }

    private fun sendMail() {
        val mailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/html"
            putExtra(Intent.EXTRA_EMAIL, "email.adress@gmail.com")
            putExtra(Intent.EXTRA_SUBJECT, "Subject")
            putExtra(Intent.EXTRA_TEXT, "Email message")
        }

        startActivity(Intent.createChooser(mailIntent, "Send Email"))
    }

    private fun sendSms() {
        val uri = Uri.parse("smsto:12346556")
        val smsIntent = Intent(Intent.ACTION_SENDTO, uri)
        smsIntent.putExtra("sms_body", "Here you can set the SMS text to be sent")
        startActivity(smsIntent)
    }

    private fun call() {
        val callIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "Your Phone_number"))
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@MainActivityKt, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL)
        } else {
            startActivity(callIntent)
        }
    }

    private fun openBrowser() {
        val url = "http://www.google.com"
        val browserIntent = Intent(Intent.ACTION_VIEW)
        browserIntent.data = Uri.parse(url)
        if (browserIntent.resolveActivity(packageManager) != null) {
            startActivity(browserIntent)
        }
    }

    private fun openMaps() {
        val location = Uri.parse("geo:37.7749,-122.4194")
        val mapIntent = Intent(Intent.ACTION_VIEW, location)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        }
    }

    private fun takePicture() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    companion object {
        private const val REQUEST_PHONE_CALL = 101
        private const val REQUEST_IMAGE_CAPTURE = 1001
    }
}