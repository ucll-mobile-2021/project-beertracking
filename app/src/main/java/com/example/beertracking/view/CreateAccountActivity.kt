package com.example.beertracking.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.beertracking.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// Class to create an account
// does NOT extend baseappcompat to not allow navigation.
// implemeted from the following site:
//https://www.appsdeveloperblog.com/firebase-authentication-example-kotlin/

class CreateAccountActivity : AppCompatActivity() {
    //global variables

    private var etFirstName: EditText? = null
    private var etLastName: EditText? = null
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var etPasswordRepeat: EditText? = null
    private var btnCreateAccount: Button? = null
    private var mProgressBar: ProgressDialog? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    private val TAG = "CreateAccountActivity"

    private var firstName: String? = null
    private var lastName: String? = null
    private var email: String? = null
    private var password: String? = null
    private var passwordrepeat: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        // set xml layout file
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        //call function
        initialise()
    }

    private fun initialise() {
        // assign the layout items
        etFirstName = findViewById<View>(R.id.et_first_name) as EditText
        etLastName = findViewById<View>(R.id.et_last_name) as EditText
        etEmail = findViewById<View>(R.id.et_email) as EditText
        etPassword = findViewById<View>(R.id.et_password) as EditText
        etPasswordRepeat = findViewById<View>(R.id.et_passwordrepeat) as EditText
        btnCreateAccount = findViewById<View>(R.id.btn_register) as Button

        // add progressbar and realtime database references
        mProgressBar = ProgressDialog(this)
        mDatabase = FirebaseDatabase.getInstance("https://beertracker-56e99-default-rtdb.europe-west1.firebasedatabase.app/")
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        mAuth = FirebaseAuth.getInstance()

        //listener for the button
        btnCreateAccount!!.setOnClickListener { createNewAccount() }
    }


    private fun createNewAccount() {
        //get the info the user filled in
        firstName = etFirstName?.text.toString()
        lastName = etLastName?.text.toString()
        email = etEmail?.text.toString()
        password = etPassword?.text.toString()
        passwordrepeat = etPasswordRepeat?.text.toString()

        //Check for errors in the userdata or empty fields

        if (TextUtils.isEmpty(firstName) ) {
            Toast.makeText(applicationContext, "Enter first name!", Toast.LENGTH_LONG).show()
        }
        else if (TextUtils.isEmpty(lastName) ) {
            Toast.makeText(applicationContext, "Enter last name!", Toast.LENGTH_LONG).show()
        }
        else if (TextUtils.isEmpty(email) ) {
            Toast.makeText(applicationContext, "Enter email!", Toast.LENGTH_LONG).show()
        }
        else if (TextUtils.isEmpty(password) ) {
            Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_LONG).show()
        }
        else if (TextUtils.isEmpty(passwordrepeat) ) {
            Toast.makeText(applicationContext, "Repeat password!", Toast.LENGTH_LONG).show()
        }
        else if (password != passwordrepeat){
            Toast.makeText(applicationContext,  "Password are not identical!", Toast.LENGTH_LONG).show()
        }
        else if(password!!.length < 6){
            Toast.makeText(applicationContext, "Password is too short!", Toast.LENGTH_LONG).show()
        }

        else {
            //setting progressbar
            mProgressBar!!.setMessage("Registering User...")
            mProgressBar!!.show()
            // creating the user with the authentication utillities of Firebase
            mAuth!!
                .createUserWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener(this) { task ->
                    mProgressBar!!.hide()
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val userId = mAuth!!.currentUser!!.uid
                        //Verify Email
                        verifyEmail();
                        //update user profile information
                        val currentUserDb = mDatabaseReference!!.child(userId)
                        currentUserDb.child("firstName").setValue(firstName)
                        currentUserDb.child("lastName").setValue(lastName)
                        var test = ArrayList<String>()
                        currentUserDb.child("friends").setValue(test)

                        updateUserInfoAndUI()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            this@CreateAccountActivity, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
        }
    }

    private fun updateUserInfoAndUI() {
        //start next activity
        val intent = Intent(this@CreateAccountActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun verifyEmail() {
        // This sends an automatic email to the user to verify his email
        // the settings for this email can be change at
        // https://console.firebase.google.com/project/beertracker-56e99/authentication/users
        val mUser = mAuth!!.currentUser;
        mUser!!.sendEmailVerification()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@CreateAccountActivity,
                                "Verification email sent to " + mUser.getEmail(),
                                Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e(TAG, "sendEmailVerification", task.exception)
                        Toast.makeText(this@CreateAccountActivity,
                                "Failed to send verification email.",
                                Toast.LENGTH_SHORT).show()
                    }
                }
    }
}
