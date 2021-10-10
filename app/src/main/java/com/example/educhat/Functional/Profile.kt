package com.example.educhat.Functional

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.educhat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_mobile_number.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.number
import java.io.ByteArrayOutputStream
import java.io.IOException


class Profile : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    val currentUserDataId = FirebaseAuth.getInstance().currentUser!!.uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val loginWith = intent.getStringExtra("loginWith")
        profile_image.setOnClickListener {
            launchGallery()

        }
        back_profile_CV.setOnClickListener {
            onBackPressed()

        }


        fillProfile()


        val userr = FirebaseAuth.getInstance().currentUser

        if (userr != null) {
            if (userr.photoUrl != null) {

                profile_image.setBackgroundResource(0)

//                    Picasso.get(this).load(userr.photoUrl).into(imageView)
                Glide.with(this).load(userr.photoUrl).into(profile_image)

            }
        }

//        if (number.text.isEmpty() || nameET.text.isEmpty()) {
//            saveProfile.isClickable = false
//            saveProfile.isEnabled = false
//
//
//        } else {
//            saveProfile.isClickable = true
//            saveProfile.isEnabled = true
//        }


        //---------------shared files start------------------------------
        // builder inform profile is complete
        val sharedPreference = getSharedPreferences("profileBuilder", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()


        // to store number
        val SPforNumber = getSharedPreferences("numberStorage", Context.MODE_PRIVATE)
        val SPforNumbereditor = SPforNumber.edit()


        // to store name


        val SPforName = getSharedPreferences("nameStorage", Context.MODE_PRIVATE)
        val SPforNameeditor = SPforName.edit()


        // to store email

        val SPforEmail = getSharedPreferences("emailStorage", Context.MODE_PRIVATE)
        val SPforEmaileditor = SPforEmail.edit()


        // to store city
        val SPforCity = getSharedPreferences("nameStorage", Context.MODE_PRIVATE)
        val SPforCityeditor = SPforCity.edit()

        //---------------shared files end

        saveProfile.setOnClickListener {
            if (number.text.isEmpty() || nameET.text.isEmpty()) {
                Toast.makeText(this, "Please add Number & Name to proceed!", Toast.LENGTH_SHORT)
                    .show()


            } else {



                editor.putString("profileComplete", "yes")
                editor.apply()

                SPforNumbereditor.putString("number", number.text.toString())
                SPforNumbereditor.apply()


                SPforNameeditor.putString("name", nameET.text.toString())
                SPforNameeditor.apply()

                SPforEmaileditor.putString("email", emailET.text.toString())
                SPforEmaileditor.apply()


                SPforCityeditor.putString("city", cityET.text.toString())
                SPforCityeditor.apply()



                saveDataToFireBase()


                if (loginWith == "google" || loginWith == "fb" || loginWith == "mobile") {
                    startActivity(Intent(this@Profile, Home::class.java))

                } else {

                    Toast.makeText(this, "Changes Saved!", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }

            }

        }
    }

    private fun saveDataToFireBase() {

//-----------------

        //________________________________
            val refOfPaid = FirebaseDatabase.getInstance()
                .getReference("NewUsers")
            val userMap = HashMap<Any, Any>()

            userMap["mobileNumber"] = number.text.toString()
            userMap["userName"] = nameET.text.toString()
            userMap["email"] = emailET.text.toString()
            userMap["city"] = cityET.text.toString()
//            userMap["parentalCode"] = parentalCodeTv.text.toString()
//            userMap["gender"] = gender
//            userMap["S_code"] = getSchoolCode.toString()


            refOfPaid.child(currentUserDataId).setValue(userMap)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {


                    } else {
                    }
                }



    }



    private fun fillProfile() {
        val currentUsers = FirebaseAuth.getInstance()?.currentUser
//        val Grade = resources.getStringArray(R.array.Grade)
//
//        val adapter = ArrayAdapter.createFromResource(
//            this,
//            R.array.Gender,
//            android.R.layout.simple_spinner_dropdown_item
//        )
//
//        genderSpinner.setAdapter(adapter)
//        genderSpinner.setSelection(1)

        val loginWith = intent.getStringExtra("loginWith")
        val name = intent.getStringExtra("name")
        val phoneNo = intent.getStringExtra("phoneNo")

        //---------------shared files start------------------------------
//246460
        // to store number
        val SPforNumber = getSharedPreferences("numberStorage", Context.MODE_PRIVATE)
        val SPforNumberSaved = SPforNumber.getString("number", "null")


        // to store name

        val SPforName = getSharedPreferences("nameStorage", Context.MODE_PRIVATE)
        val SPforNameSaved = SPforName.getString("name", "null")

        // to store email

        val SPforEmail = getSharedPreferences("emailStorage", Context.MODE_PRIVATE)
        val SPforEmailSaved = SPforEmail.getString("email", "null")


        // to store city
        val SPforCity = getSharedPreferences("nameStorage", Context.MODE_PRIVATE)
        val SPforCitySaved = SPforCity.getString("city", "null")



        //---------------shared files end

        when (loginWith) {
            "google" -> {
                nameET.setText(name)
            }
            "fb" -> {
                nameET.setText(name)

            }
            "mobile" -> {
                number.setText(phoneNo)
            }


        }

        if (SPforNumberSaved != "null") {
            number.setText(SPforNumberSaved)
        }
        if (SPforNameSaved != "null") {
            nameET.setText(SPforNameSaved)
        }

        if (SPforEmailSaved != "null") {
            emailET.setText(SPforEmailSaved)
        }
        if (SPforCitySaved != "null") {
            cityET.setText(SPforCitySaved)
        }
//         if (SPforGradeSaved != "null") {
//            if (SPforGradeSaved != null) {
//                gradeSpinner.setSelection(SPforGradeSaved.toInt())
//            }
//        }
//

    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
// PICK_IMAGE_REQUEST
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                profile_image.setImageBitmap(bitmap)
                handleimg(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun handleimg(bitmap: Bitmap?) {

        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val ref = FirebaseStorage.getInstance().reference
            .child("profileImages")
            .child(uid + ".jpeg")

        ref.putBytes(baos.toByteArray())
            .addOnSuccessListener { p0 ->
                getDownloadUrl(ref)
            }
            .addOnFailureListener { p0 ->
                Toast.makeText(applicationContext, p0.message, Toast.LENGTH_SHORT).show()

            }


    }

    private fun getDownloadUrl(reference: StorageReference) {
        reference.downloadUrl.addOnSuccessListener { uri: Uri ->
            setUserProfileUrl(uri)
        }
    }

    private fun setUserProfileUrl(uri: Uri) {
        val user = FirebaseAuth.getInstance().currentUser
        val request = UserProfileChangeRequest.Builder()
            .setPhotoUri(uri)
            .build()
        user?.updateProfile(request)
            ?.addOnSuccessListener {

                Toast.makeText(applicationContext, "Updated Successfully", Toast.LENGTH_SHORT)
                    .show()

            }
            ?.addOnFailureListener { p0 ->
                Toast.makeText(applicationContext, p0.message, Toast.LENGTH_SHORT).show()
            }
    }

}