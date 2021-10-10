package com.example.educhat.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.educhat.R
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.safetynet.SafetyNet
import kotlinx.android.synthetic.main.activity_mobile_number.*


class MobileNumber : AppCompatActivity() {
    val SiteKey="6Ldl12McAAAAAKKNTRI0X2LBOKqKSUvL5R9X7vJK"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobile_number)
        back_mobile_number_CV.setOnClickListener { onBackPressed() }
        ccp.setOnCountryChangeListener { selectedCountry ->
////            Toast.makeText(
////                this,
////                "Updated code is " + view.ccp.selectedCountryCodeWithPlus,
////                Toast.LENGTH_SHORT
////            ).show()
//
//
        }

        sendOptBtn.setOnClickListener {
            val mobileN: String = number.text.toString()

            if (mobileN.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Please enter valid mobile number",
                    Toast.LENGTH_SHORT
                ).show()
                // bottomSheetDialog.number.setError("Please enter valid 10 digit mobile number")
                // bottomSheetDialog.number.requestFocus()

            }
//            else if(!recaptchaCB.isChecked){
//                Toast.makeText(
//                    applicationContext,
//                    "Please click on reCAPTCHA",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
            else {

              //  Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()

                intent =
                    Intent(this, DialerOtp::class.java)
                intent.putExtra("mobile", mobileN)
                intent.putExtra("countryCode", ccp.selectedCountryCodeWithPlus.toString())
                startActivity(intent)


            }
        }

        
//        recaptchaCB.setOnClickListener{
//            SafetyNet.getClient(this).verifyWithRecaptcha(SiteKey)
//                .addOnSuccessListener { recaptchaTokenResponse ->
//                    // Indicates communication with reCAPTCHA service was
//                    // successful.
//                    val userResponseToken = recaptchaTokenResponse.tokenResult
//                    if (!userResponseToken.isEmpty()) {
//                        // Validate the user response token using the
//                        // reCAPTCHA siteverify API.
//                        Log.d("recaptchaOnFail", "VALIDATION STEP NEEDED")
//                    }
//                }
//                .addOnFailureListener { e ->
//                    if (e is ApiException) {
//                        // An error occurred when communicating with the
//                        // reCAPTCHA service. Refer to the status code to
//                        // handle the error appropriately.
//                        val apiException = e as ApiException
//                        val statusCode = apiException.statusCode
//                        Log.d(
//                            "recaptchaOnFail", "Error: " + CommonStatusCodes
//                                .getStatusCodeString(statusCode)
//                        )
//                    } else {
//                        // A different, unknown type of error occurred.
//                        Log.d("recaptchaOnFail", "Error: " + e.message)
//                    }
//                }
//        }
    }
   
}