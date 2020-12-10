package com.example.beertracking.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.beertracking.R
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storageMetadata
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_beer.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class  AddBeerActivity : BaseAppCompatActivity() {

    var selectedImage: ImageView? = null
    var cameraBtn: Button? = null
    var galleryBtn: Button? = null
    var currentPhotoPath: String? = null
    private var storageReference: StorageReference? = null
    var mAuth: FirebaseAuth? = null
    var uploadBtn: Button? = null
    var uri: Uri? = null
    var filename: String? = null
    var et_beer: EditText? = null
    var et_description: EditText? = null
    var beer: String? = null
    var description: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance()

        setContentView(R.layout.activity_add_beer)

        if (mAuth!!.currentUser == null ){

            val intent = Intent(this@AddBeerActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            super.onCreate(savedInstanceState)

        }

        else {
/*
            selectedImage = findViewById(R.id.displayImageView)
            galleryBtn = findViewById(R.id.galleryBtn)
            uploadBtn = findViewById(R.id.upload)
            et_beer = findViewById(R.id.et_beer_name)
            et_description = findViewById(R.id.et_description_beer)


            uploadBtn!!.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    uploadImageToFirebase(filename!!, uri!!)
                }
            })

            this.cameraBtn = findViewById(R.id.cameraBtn)
            cameraBtn!!.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    askCameraPermissions()
                    uploadBtn!!.visibility = View.VISIBLE
                }
            })
            galleryBtn!!.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    val gallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(gallery, GALLERY_REQUEST_CODE)
                    uploadBtn!!.visibility = View.VISIBLE
                }
            }) */
            storageReference = FirebaseStorage.getInstance("gs://beertracker-56e99.appspot.com/").getReference()
            super.onCreate(savedInstanceState)

        }

    }


    private fun uploadImageToFirebase(name: String, contentUri: Uri) {
        val image = storageReference!!.child("pictures/" + name)
        image.putFile(contentUri).addOnSuccessListener(object :
            OnSuccessListener<UploadTask.TaskSnapshot> {
            override fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(object : OnSuccessListener<Uri> {
                    override fun onSuccess(uri: Uri) {
                        Log.d("tag", "onSuccess: Uploaded Image URl is " + uri.toString())
                        Picasso.get().load(uri).into(selectedImage)
                    }
                })
                Toast.makeText(this@AddBeerActivity, "Image Is Uploaded.", Toast.LENGTH_SHORT)
                    .show()
                updateMetaData()
                val intent = Intent(this@AddBeerActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(@NonNull e: Exception) {
                Toast.makeText(this@AddBeerActivity, "Upload Failled.", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun updateMetaData() {
        // Create a reference to the file whose metadata we want to change
        var storage =
            FirebaseStorage.getInstance("gs://beertracker-56e99.appspot.com/").getReference()
        var forestRef = storage!!.child("pictures/${filename}");

        // Create file metadata to update
        val date = SimpleDateFormat("ddMMyyyy").format(Date())
        val mUser = mAuth!!.currentUser
        beer = et_beer?.text.toString()
        description = et_description?.text.toString()


        var metadata = storageMetadata {
            setCustomMetadata("user", "${mUser!!.uid}")
            setCustomMetadata("beer", "${beer}")
            setCustomMetadata("description", "${description}")
            setCustomMetadata("date", "${date}")
        }


// Update metadata properties
        forestRef.updateMetadata(metadata)
    }

    private fun askCameraPermissions() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.CAMERA),
                CAMERA_PERM_CODE
            )
        } else {
            dispatchTakePictureIntent()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<String>,
        @NonNull grantResults: IntArray
    ) {

        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                Toast.makeText(
                    this,
                    "Camera Permission is Required to Use camera.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val f = File(currentPhotoPath)
                selectedImage!!.setImageURI(Uri.fromFile(f))
                Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f))
                val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                val contentUri = Uri.fromFile(f)
                mediaScanIntent.setData(contentUri)
                this.sendBroadcast(mediaScanIntent)
                uri = contentUri
                filename = f.name
            }
        }
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val contentUri = data!!.getData()
                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri!!)
                Log.d("tag", "onActivityResult: Gallery Image Uri: " + imageFileName)
                selectedImage!!.setImageURI(contentUri)
                uri = contentUri
                filename = imageFileName
            }
        }

    }

    private fun getFileExt(contentUri: Uri): String {
        val c = getContentResolver()
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(c.getType(contentUri))!!
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        // File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath()
        return image
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(
                    this,
                    "net.smallacademy.android.fileprovider",
                    photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
            }
        }
    }

    companion object {
        val CAMERA_PERM_CODE = 101
        val CAMERA_REQUEST_CODE = 102
        val GALLERY_REQUEST_CODE = 105
    }

}
