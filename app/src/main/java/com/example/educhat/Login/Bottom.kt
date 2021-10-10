package com.example.educhat.Login

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.educhat.Functional.Home
import com.example.educhat.Functional.Profile
import com.example.educhat.R
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

class Bottom : AppCompatActivity() {
    //    val codePicker=findViewById<CountryCodePicker>(R.id.ccp)
//    val phoneNumber=findViewById<EditText>(R.id.number)
    var user = FirebaseAuth.getInstance().currentUser
    lateinit var auth: FirebaseAuth
    val currentUsers = FirebaseAuth.getInstance().currentUser
    lateinit var credential: AuthCredential
    var callbackManager: CallbackManager = CallbackManager.Factory.create()


    var a: Int = 1

    val currentUser = FirebaseAuth.getInstance().currentUser
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions

    private val RC_SIGN_IN = 9001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom)
        configureGoogleSignIn()
        auth = FirebaseAuth.getInstance()

        //fb code1
        FacebookSdk.sdkInitialize(getApplicationContext())

        sen_otp_btn.setOnClickListener {


            intent = Intent(this, MobileNumber::class.java)
            startActivity(intent)

//            val mobileN: String = bottomSheetDialog.number.text.toString()
//
//
//            if (mobileN.isEmpty()) {
//                Toast.makeText(
//                    applicationContext,
//                    "Please enter valid mobile number",
//                    Toast.LENGTH_SHORT
//                ).show()
//                // bottomSheetDialog.number.setError("Please enter valid 10 digit mobile number")
//                // bottomSheetDialog.number.requestFocus()
//
//            } else {
//
//
//                bottomSheetDialog.dismiss()
//
//                intent = Intent(this, DialerOtp::class.java)
//                intent.putExtra("mobile", mobileN)
//                intent.putExtra("countryCode", view.ccp.selectedCountryCodeWithPlus.toString())
//                startActivity(intent)
//
//
//            }


        }
        gmailCV.setOnClickListener {
            progressBarSS.visibility= View.VISIBLE
            signInWithGoogle()
        }

        fb_login_button_Cv.setOnClickListener {
         login_button.performClick()
        }
        login_button.setOnClickListener {



            Log.d("fb button pressed", "fb button startSCreen")

            signIn()

        }
    }

    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }

    private fun signInWithGoogle() {

        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }
    var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
//        super.onBackPressed()
//        val dialog = BottomSheetDialog(this)
//        dialog.dismiss()
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            finishAffinity()
            return
        }

        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 10000)
    }
    private fun updateUI(user: FirebaseUser?) {


//        Log.d("update ui fun", "function of update ui")

        FirebaseAuth.getInstance().currentUser?.reload()?.addOnSuccessListener { void ->
            //            var user = FirebaseAuth.getInstance().currentUser


            if (currentUser != null) {
//                Log.d("facebook user not null", "updateUI")
//                Toast.makeText(this,"Inside of if",Toast.LENGTH_SHORT).show()
//                finish()
                val bottomSheetDialog = BottomSheetDialog(this,
                    R.style.BottomSheetDialog
                )
                val view = layoutInflater.inflate(R.layout.activity_bottom, null)
                bottomSheetDialog.setContentView(view)
                bottomSheetDialog.cancel()

                startActivity(Intent(this, Home::class.java))
//                dialog.show()
            } else {
                Toast.makeText(this, "User is Null!", Toast.LENGTH_SHORT).show()
//                Log.d("else", "else updateUI")
            }
        }

    }


    private fun signIn() {
        Log.d("registercallback", "registercallabck ss1")

        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
                override fun onSuccess(result: LoginResult?) {
                    Log.d("registercallback", "registercallabck onsuccess")
                    handleFacebookAccessToken(result?.accessToken)


                }

                override fun onCancel() {

                    Log.d("registercallback", "registercallabck oncancel")
                }

                override fun onError(error: FacebookException?) {
                    Log.d("fb", error.toString())

                    Log.d("registercallback", "registercallabck onerror")
                }

            })


//        Log.d("registercallback after","registercallabck after fun")

//        handleFacebookAccessToken(accessToken: AccessToken?)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
        val view = layoutInflater.inflate(R.layout.activity_bottom, null)
        val bottomSheetDialog = BottomSheetDialog(this,
            R.style.BottomSheetDialog
        )
        bottomSheetDialog.setContentView(view)


        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account)
                    view.progressBarSS.visibility=View.GONE
                    Toast.makeText(this, "Google sign in successfully done!", Toast.LENGTH_LONG).show()
                }
            } catch (e: ApiException) {
                view.progressBarSS.visibility=View.GONE
                Toast.makeText(this, "Google sign in failed due to "+e, Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                checkProfile(acct)

            } else {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun handleFacebookAccessToken(accessToken: AccessToken?) {
        //Get Credentials

        Log.d("handleFacebooktoken", "handlefacebooktoken")
        credential = FacebookAuthProvider.getCredential(accessToken!!.token)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this@Bottom)
            { task ->
                if (task.isSuccessful) {
                    //Sign in success, update UI with the signed-in user's information


                    val bottomSheetDialog = BottomSheetDialog(this,
                        R.style.BottomSheetDialog
                    )
                    val view = layoutInflater.inflate(R.layout.activity_bottom, null)
                    bottomSheetDialog.setContentView(view)
                    bottomSheetDialog.dismiss()
//                    dialog.show()
                    val pd = ProgressDialog(this)
                    pd.setMessage("Signing in..")
                    pd.show()
                    //moveMainPage(task.result?.user)

                    checkProfileFB(task.result?.user)
                    pd.cancel()


                    Toast.makeText(this,"Signing in through Facebook",Toast.LENGTH_LONG).show()


//                    if (currentUsers != null) {
//                      Log.d("fb button"," currentUser!= null")


// updateUI(user)

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()


                    //updateUI(null)
                }

//                }
            }

    }

    private fun checkProfileFB(user: FirebaseUser?) {
        val sharedPreference =  getSharedPreferences("profileBuilder", Context.MODE_PRIVATE)

        val news= sharedPreference.getString("profileComplete","null")

        if (news=="null"){
            val intent=Intent(this@Bottom, Profile::class.java)
            intent.putExtra("name",user?.displayName)
            intent.putExtra("loginWith", "fb")

            startActivity(intent)

        }
        else {
            startActivity(Intent(this@Bottom, Home::class.java))
        }
    }


    private fun checkProfile(acct: GoogleSignInAccount) {
        val sharedPreference =  getSharedPreferences("profileBuilder", Context.MODE_PRIVATE)
//        var editor = sharedPreference.edit()
//        editor.putString("profileComplete","yes")
//        editor.commit()
        val news= sharedPreference.getString("profileComplete","null")

        if (news=="null"){
            val intentP=Intent(this@Bottom, Profile::class.java)
            intentP.putExtra("name", acct.displayName)
            intentP.putExtra("acct", acct)
            intentP.putExtra("loginWith", "google")

            startActivity(intentP)
        }
        else {
            startActivity(Intent(this@Bottom, Home::class.java))
        }

    }


    private fun moveMainPage(user: FirebaseUser?) {
        Log.d("moveMainPage fun", "fun")

        if (user != null) {

            val bottomSheetDialog = BottomSheetDialog(this,
                R.style.BottomSheetDialog
            )
            val view = layoutInflater.inflate(R.layout.activity_bottom, null)
            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.dismiss()

            val intent = Intent(this, Home::class.java)
            intent.putExtra("name", 2)

            startActivity(intent)
            finish()
        }
    }


}

