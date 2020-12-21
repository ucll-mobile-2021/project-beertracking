package com.example.beertracking.view

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.beertracking.R
import com.example.beertracking.model.Picture
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storageMetadata
import com.squareup.picasso.Picasso
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

//class to add a beer
//extends baseapp to allow for navigation

class  AddActivityActivity : BaseAppCompatActivity() {

    //Some variables. okay... a LOT of variables.
    var selectedImage: ImageView? = null
    var cameraBtn: Button? = null
    var galleryBtn: Button? = null
    var currentPhotoPath: String? = null
    private var storageReference: StorageReference? = null
    private var dataReference: DatabaseReference? = null
    var mAuth: FirebaseAuth? = null
    var uploadBtn: Button? = null
    var uri: Uri? = null
    var filename: String? = null
    var et_description: EditText? = null
    private var mProgressBar: ProgressDialog? = null

    private var date: String? = null
    private var description: String? = null
    private var user: String? = null
    private var beer: String? = null
    private var url: String? = null
    private var fullname: String? = null
    private var userId: String? = null
    private var spinner: Spinner? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        //set the links to the realtime database and fill in variables
        mAuth = FirebaseAuth.getInstance()
        userId = mAuth!!.currentUser!!.uid
        dataReference = FirebaseDatabase.getInstance("https://beertracker-56e99-default-rtdb.europe-west1.firebasedatabase.app/").getReference("pictures")
        // link to the storage location of pictures
        storageReference = FirebaseStorage.getInstance("gs://beertracker-56e99.appspot.com/").getReference()

        // link the layout
        setContentView(R.layout.activity_add_activity)

        // supposed to show loading message while loading
        mProgressBar = ProgressDialog(this);


        //IF the user is not logged in we redirect to the LoginActivity
        if (mAuth!!.currentUser == null ){

            val intent = Intent(this@AddActivityActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            super.onCreate(savedInstanceState)

        }

        else {
            //Search textview
            val textView = findViewById<View>(R.id.beer_name) as TextView
            textView.setText(intent.getStringExtra(EXTRA_MESSAGE))


//            //for the spinner item (dropdown of beers)
//            val spinner = findViewById<Spinner>(R.id.et_beer_name)
//            //values for beer
//            val items = R.array.beer_list
//
//            val adapter = ArrayAdapter.createFromResource(
//                this,
//                R.array.beer_list,
//                android.R.layout.simple_spinner_dropdown_item
//            )
//            spinner.setAdapter(adapter)
//
//            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(
//                    arg0: AdapterView<*>?,
//                    arg1: View?,
//                    arg2: Int,
//                    arg3: Long
//                ) {
//                    beer = spinner.selectedItem.toString()
//
//                }
//
//                override fun onNothingSelected(arg0: AdapterView<*>?) {}
//            }


            // find the layout items needed
            selectedImage = findViewById(R.id.displayImageView)
            galleryBtn = findViewById(R.id.galleryBtn)
            uploadBtn = findViewById(R.id.upload)
            et_description = findViewById(R.id.et_description_beer)


            // add some basic listeners for the buttons and tell them where to go
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
            })

            // as usual declined at the end to now initiate the navbar
            super.onCreate(savedInstanceState)

        }

    }

    // Some helper functions.


    // upload image to firebase storage
    private fun uploadImageToFirebase(name: String, contentUri: Uri) {
        //show loading message while we are busy
        mProgressBar!!.setMessage("Uploading Image...")
        mProgressBar!!.show()
        // create a new location for the image using the userid of current user and the unique imagename
        val image = storageReference!!.child("pictures/" +userId+ "/" + name)

        // adding the image with a listener
        image.putFile(contentUri).addOnSuccessListener(object :
            OnSuccessListener<UploadTask.TaskSnapshot> {

            //called if the image has been succesfully uploaded
            override fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot) {

                //getting the download link where we can download the image from later on
                image.getDownloadUrl().addOnSuccessListener(object : OnSuccessListener<Uri> {

                    //called if we succesfully got the download link
                    override fun onSuccess(uri: Uri) {
                        //save the download link as variable url, log the link, and load the image
                        url = uri.toString()
                            Log.d("tag", "onSuccess: Uploaded Image URl is " + uri.toString())
                        //not sure why we load the image but cant be bothered removing it and possibly breaking stuff..
                        Picasso.get().load(uri).into(selectedImage)
                        //call the function
                        writeNewImageToDatabase()

                    }
                })

                //We show a small Toast message that the image is uploaded
                Toast.makeText(this@AddActivityActivity, "Image Is Uploaded.", Toast.LENGTH_SHORT)
                    .show()
                //call the function
                updateMetaData()
                //hide the status message since it is uploaded now
                mProgressBar!!.hide()
                //load the overview (mainactivity) activity
                val intent = Intent(this@AddActivityActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            //gets called upon failure of uploading
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(@NonNull e: Exception) {
                //some logging. who needs REAL errorhandling, right?
                mProgressBar!!.hide()

                Toast.makeText(this@AddActivityActivity, "Upload Failled.", Toast.LENGTH_SHORT).show()
            }
        })



    }

    // function to store the new image in the realtime database as well
    private fun writeNewImageToDatabase() {
        //creating the new Picture class with the correct info (which has been set in other functions)
        val info = Picture(date!!, description!!, user!!, intent.getStringExtra(EXTRA_MESSAGE)!!, url!!, userId!!)
        val key = dataReference!!.push().key
        // pushing the image to the database
        dataReference!!.child(key!!).setValue(info)
    }

    // Set some nice metadata for the picture so we at least now a bit about it.
    private fun updateMetaData() {
        //getting some instances again
        var mFirebaseInstance = FirebaseDatabase.getInstance("https://beertracker-56e99-default-rtdb.europe-west1.firebasedatabase.app/")
        var mFireDatabase = mFirebaseInstance!!.getReference("Users")
        var userId = mAuth!!.currentUser!!.uid

        //listening ONCE to the users node in the database so we get the data pulled in
        mFireDatabase!!.child(userId!!).addListenerForSingleValueEvent(object : ValueEventListener {

            //called when data changes AND once on initialisation
            //notice that we never can change it later on since we added a singleValueEvent instead of
            // a regular valueEvent
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                //setting the fullname variable
                fullname =
                    snapshot.child("firstName").value.toString() + " " + snapshot.child("lastName").value.toString()
                // Create a reference to the file whose metadata we want to change
                var storage =
                    FirebaseStorage.getInstance("gs://beertracker-56e99.appspot.com/")
                        .getReference()
                var forestRef = storage!!.child("pictures/${userId}/${filename}");


                // Create file metadata to update
                var temp = LocalDateTime.now()
                date = temp.format(DateTimeFormatter.ofPattern("yMdHms")).toString()
                println("date")
                println(date)
                var mUser = mAuth!!.currentUser
                user = fullname
                description = et_description?.text.toString()


                var metadata = storageMetadata {
                    setCustomMetadata("user", "${mUser!!.uid}")
                    setCustomMetadata("beer", "${beer}")
                    setCustomMetadata("description", "${description}")
                    setCustomMetadata("date", "${date}")
                }
                // Update picture metadata properties
                forestRef.updateMetadata(metadata)
            }

            //called when there is a database error
            override fun onCancelled(error: DatabaseError) {
                Log.e("error", "Failed to read user", error.toException())
            }
        })

    }

    //The next functions are used to upload an image from device storage, taking a picture,
    // asking permissions to the device owner, and some other stuff
    // Code should be self-explanatory and otherwise...

    // good luck. i just got this from some online guides + stackoverflow and it works
    // SO NO TOUCHY TOUCHY
    // link guides:
    // https://smallacademy.co/blog/android/capture-display-image-in-android-imageview/
    // https://smallacademy.co/blog/android/upload-images-to-firebase-cloud-storage/
    // Thanks smallacademy. very cool!
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
