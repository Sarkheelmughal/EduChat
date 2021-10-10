package com.example.educhat.Login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.educhat.Functional.Home
import com.example.educhat.Functional.Profile
import com.example.educhat.R
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import kotlinx.android.synthetic.main.activity_dialer_otp.*
import java.util.concurrent.TimeUnit


class DialerOtp : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var verificationIde = ""
    private val KEY_VERIFICATION_ID = "key_verification_id"

    val currentUser = FirebaseAuth.getInstance().currentUser
    var mResendToken: ForceResendingToken? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialer_otp)
        auth = FirebaseAuth.getInstance()
        auth.useAppLanguage()

        val phoneNo = intent.getStringExtra("mobile")
        val countryCode = intent.getStringExtra("countryCode")
        back_dailerOPT_CV.setOnClickListener { onBackPressed() }
        Log.d("countryCode", countryCode.toString())
        Log.d("countryCodePhone", phoneNo.toString())
        sendVerificationCode(phoneNo!!.toString(), countryCode!!)
//        val intent = intent
//      val mob = intent.getStringExtra("mobile")
//        sendVerificationCode(mob.toString())


        resendTV.setOnClickListener { sendVerificationCode(phoneNo!!.toString(), countryCode!!) }
        mobileNumberTv.text = "$countryCode $phoneNo"

        verifyBTn.setOnClickListener {
            val codeOtp = view7.text.toString()
            if (codeOtp.isEmpty() || codeOtp.length < 6) {
                Toast.makeText(
                    applicationContext,
                    "Please Enter OTP code to proceed!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                //verifying the code entered manually
                verifyVerificationCode(codeOtp)
            }
        }

        if (verificationIde == null && savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
    }


    private fun sendVerificationCode(phoneNo: String, countryCode: String) {
        verificationCallbacks()

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            countryCode + phoneNo, // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            this, // Activity (for callback binding)
            // TaskExecutors.MAIN_THREAD,
            mCallbacks
        ) // OnVerificationStateChangedCallbacks
    }

    private fun verificationCallbacks() {
        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                //Getting the code sent by SMS
                val cod: String? = credential.smsCode

                Log.d("codeTestingVCBOutSide", cod.toString())

                if (cod != null) {
                    view7.setText(cod)
                    //verifying the code
                    Log.d("codeTestingVCB", cod)
                    verifyVerificationCode(cod)
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(applicationContext, p0.message, Toast.LENGTH_SHORT).show()
                if (p0 is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(applicationContext, "Invalid request", Toast.LENGTH_SHORT).show()
                    // ...
                } else if (p0 is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                    Toast.makeText(
                        applicationContext,
                        "SMS quota for the project has been exceeded",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onCodeSent(
                verification: String,
                p1: ForceResendingToken
            ) {
                super.onCodeSent(verification, p1)
                //storing the verification id that is sent to the user
                verificationIde = verification
                mResendToken = p1
                Toast.makeText(
                    applicationContext,
                    "Verification code has been send on your phone!",
                    Toast.LENGTH_SHORT
                ).show()

                Log.d("codeTestingOCS", verification)
                Log.d("codeTestingOCS", p1.toString())


            }
        }
    }

    fun verifyVerificationCode(code: String) {
        //creating the credential
        if (verificationIde != "" || verificationIde.isNotEmpty()) {
            val credential = PhoneAuthProvider.getCredential(verificationIde, code)
            //signing the user
            Log.d("codeTestingVVC", credential.toString())
            signInWithPhoneAuthCredential(credential)
        } else Toast.makeText(
            this,
            "Try Again! Don't close app after getting OTP!",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun checkProfile() {
        val sharedPreference = getSharedPreferences("profileBuilder", Context.MODE_PRIVATE)
//        var editor = sharedPreference.edit()
//        editor.putString("profileComplete","yes")
//        editor.commit()
        val phoneNo = intent.getStringExtra("mobile")
        val news = sharedPreference.getString("profileComplete", "null")

        if (news == "null") {
            val intent = Intent(this@DialerOtp, Profile::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            intent.putExtra("loginWith", "mobile")
            intent.putExtra("phoneNo", phoneNo)


            startActivity(intent)
            finish()

        } else {
            startActivity(Intent(this@DialerOtp, Home::class.java))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            finish()

        }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Your Phone number has been registered successfully!",
                        Toast.LENGTH_SHORT
                    ).show();
                    //verification successful we will start the profile activity

                    checkProfile()
//                    val intent = Intent(this, Home::class.java)
//                    intent.putExtra("name",1)
//                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    startActivity(intent)
                } else {
                    // Sign in failed, display a message and update the UI
//                    Toast.makeText(this, "Wrong Code!", Toast.LENGTH_SHORT).show()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(
                            this,
                            "verification code entered was invalid!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }

    override fun onStart() {
        super.onStart()
        if (currentUser != null) {
            intent = Intent(applicationContext, Home::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_VERIFICATION_ID, verificationIde)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        verificationIde = savedInstanceState.getString(KEY_VERIFICATION_ID).toString()
    }
}