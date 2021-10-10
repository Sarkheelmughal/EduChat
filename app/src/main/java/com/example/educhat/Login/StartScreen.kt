package com.example.educhat.Login

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.interfaces.ItemChangeListener
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.educhat.Functional.Home
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_bottom.*
import kotlinx.android.synthetic.main.activity_bottom.view.*
import kotlinx.android.synthetic.main.activity_start_screen1.*
import com.example.educhat.R


class StartScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        setContentView(R.layout.activity_start_screen1)

        val imageList = ArrayList<SlideModel>() // Create image list

// imageList.add(SlideModel("String Url" or R.drawable)
// imageList.add(SlideModel("String Url" or R.drawable, "title") You can add title

        imageList.add(SlideModel(R.drawable.get_start_one_ic, ))
        imageList.add(SlideModel(R.drawable.get_start_second_ic))
        imageList.add(SlideModel(R.drawable.get_start_third_ic))

        val imageSlider = findViewById<ImageSlider>(R.id.image_slider)
        imageSlider.setImageList(imageList)

        imageSlider.setItemChangeListener(object : ItemChangeListener {
            override fun onItemChanged(position: Int) {
                Log.d("itemPosition",position.toString())
                when (position) {
                    0 -> {
                        tv.text="Challenge Friends,\n" +
                                "Make Progress, & \n" +
                                "Earn 10,000 PKR"
                    }
                    1 -> {
                        tv.text="Ask Questions,\n" +
                                "Get Best Answers,\n" +
                                "& Improve Concepts"
                    }
                    2 -> {
                        tv.text="View Progress,\n" +
                                "Get Recommendations,\n" +
                                "Win At Exams & Life."
                    }
                }
            }


        })
        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
                // You can listen here
                Log.d("itemPosition",position.toString())

            }
        })

//
//        val view = layoutInflater.inflate(R.layout.activity_bottom, null)
//        val bottomSheetDialog = BottomSheetDialog(this,
//            R.style.BottomSheetDialog
//        )



//        bottomSheetDialog.setContentView(view)








        started_btn.setOnClickListener {

//            if(bottomSheetDialog.isShowing){
//                Log.d(" bottomSheetDialog  "," is showing")
//            }else{ bottomSheetDialog.show()}

            startActivity(Intent(this,Bottom::class.java))
            //bottomSheetDialog.show()

        }

//        view.ccp.setOnCountryChangeListener { selectedCountry ->
////            Toast.makeText(
////                this,
////                "Updated code is " + view.ccp.selectedCountryCodeWithPlus,
////                Toast.LENGTH_SHORT
////            ).show()
//
//
//        }




    }


    override fun onDestroy() {
        super.onDestroy()

        val bottomSheetDialog = BottomSheetDialog(this,
            R.style.BottomSheetDialog
        )
        val view = layoutInflater.inflate(R.layout.activity_bottom, null)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.dismiss()
    }



}