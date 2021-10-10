package com.example.educhat.Functional

import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.educhat.R
import com.example.educhat.databinding.ActivityStreakBinding
import kotlinx.android.synthetic.main.streak_text.view.*
import java.util.*


class Streak : AppCompatActivity() {
    private lateinit var calendar: Calendar
    lateinit var binding: ActivityStreakBinding
    var cT: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_streak)
        // timer()
        binding.parentOfTimerView.setOnClickListener {

            var connected = false
            val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            connected =
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED

            if (!connected) {
                Toast.makeText(
                    this,
                    "Make sure you are connected to an Internet!",
                    Toast.LENGTH_LONG
                )
                    .show()
                              binding.parentOfTimerView.isClickable=false
            }
            else {
                //saveRecord()
                startActivity(Intent(this,StreakQuiz::class.java))
        }
        }
        manageStreak()
        binding.backFromStreak.setOnClickListener { onBackPressed() }
        binding.viewTermTv.setOnClickListener { termsAlert() }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    private fun saveRecord() {
       // Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        val year = calendar.get(Calendar.YEAR)

        //------------------ start streak counter 1
        val sPofStreakCounter = getSharedPreferences("streak_pref", MODE_PRIVATE)
        val editorsPofStreakCounter = sPofStreakCounter.edit()
        val steakCount = sPofStreakCounter.getInt(
            "streak_pref",
            0
        )
        editorsPofStreakCounter.putInt("streak_pref",steakCount+1)
        editorsPofStreakCounter.apply()

        //------------------ end streak counter

        //---------------start get last day on test given 2
        val sPofLastTest = getSharedPreferences("streak_pref_last_test", MODE_PRIVATE)
        val editorsPofLastTest = sPofLastTest.edit()
        val lastTestDay = sPofLastTest.getInt(
            "streak_pref_LT",
            0
        )
        editorsPofLastTest.putInt("streak_pref_LT", dayOfYear)
        editorsPofLastTest.apply()

        //---------------end get last day on test given

        //---------------start get last year on test given 3
        val sPofLastTestYear = getSharedPreferences("streak_pref_last_year", MODE_PRIVATE)
        val editorsPofLastTestYear = sPofLastTestYear.edit()
        val lastTestYear = sPofLastTestYear.getInt(
            "streak_pref_LTY",
            0
        )
        editorsPofLastTestYear.putInt("streak_pref_LTY", year)
        editorsPofLastTestYear.apply()
        //---------------end get last year on test given

       // startActivity(Intent(this,StreakQuiz::class.java))
    }
    private fun manageStreak() {
        calendar = Calendar.getInstance()
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        val year = calendar.get(Calendar.YEAR)

        //--------------- start get streak counter 1
        val sPofStreakCounter = getSharedPreferences("streak_pref", MODE_PRIVATE)
        val editorsPofStreakCounter = sPofStreakCounter.edit()
        val steakCount = sPofStreakCounter.getInt(
            "streak_pref",
            0
        )

        ///--------------end get streak counter

        //---------------start get last day on test given 2
        val sPofLastTest = getSharedPreferences("streak_pref_last_test", MODE_PRIVATE)
        val editorsPofLastTest = sPofLastTest.edit()
        val lastTestDay = sPofLastTest.getInt(
            "streak_pref_LT",
            0
        )
        //---------------end get last day on test given

        //---------------start get last year on test given 3
        val sPofLastTestYear = getSharedPreferences("streak_pref_last_year", MODE_PRIVATE)
        val editorsPofLastTestYear = sPofLastTestYear.edit()
        val lastTestYear = sPofLastTestYear.getInt(
            "streak_pref_LTY",
            0
        )
        //---------------end get last year on test given

        Log.d("dayTestDAY_OF_YEAR", dayOfYear.toString())
        Log.d("dayTestYear", year.toString())
        Log.d("dayTestLTD", lastTestDay.toString())
        Log.d("dayTestSteak", steakCount.toString())
        Log.d("dayTestTY", lastTestYear.toString())

        binding.streakTv.text=steakCount.toString()

      if(year-lastTestYear == 0){
          when {
              dayOfYear - lastTestDay == 0 -> {
                //  binding.timerHomeTV.text = "Come Tomorrow!"
                  binding.todaysHomeTV.text = "You are done for today."
                  binding.footerTv.text = "Come back after"
                  binding.streakStartOrEnd.text = "Streak Starts in"
                  timer()
                  binding.parentOfTimerView.isClickable=false
              }
              dayOfYear - lastTestDay >1 -> {
                  editorsPofStreakCounter.putInt("streak_pref",0)
                  editorsPofStreakCounter.apply()

                  binding.parentOfTimerView.isClickable=true

                  timer()
              }
              else -> {
                  binding.parentOfTimerView.isClickable=true

                  timer()
              }
          }
      }
      else if(year - lastTestYear >= 1){
          //editorsPofStreakCounter.putInt("streak_pref",steakCount+1)
          binding.parentOfTimerView.isClickable=true

          timer()
      }

    }
    fun timer() {
        val calendar = Calendar.getInstance()
        // calendar.time
        Log.d("CurrentTime: ", calendar.time.toString())
//        calendar
        val hourCurrent = calendar.get(Calendar.HOUR)
        val minutCurrent = calendar.get(Calendar.MINUTE)
        val secondCurrent = calendar.get(Calendar.SECOND)
        val am_pm = calendar.get(Calendar.AM_PM)

        Log.d("CurrentTime:AM_PM ", am_pm.toString())
        //   Log.d("CurrentTime:ZONE ", am_m.toString())


        var hourSub = 23 - hourCurrent
        val minSub = 59 - minutCurrent
        val secSub = 59 - secondCurrent

        if (am_pm == 1) {
            hourSub = 11 - hourCurrent
        }


        var secZero="0"
        secZero = if(secSub <= 9){
            "0$secSub"
        }else{
            "$secSub"
        }

        var mintZero="0"
        mintZero = if(minSub <= 9){
            "0$minSub"
        }else{
            "$minSub"
        }

        var hourZero="0"
        hourZero = if(hourSub <= 9){
            "0$hourSub"
        }else{
            "$hourSub"
        }


        cT = object : CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                binding.timerHomeTV.text = "$hourZero : $mintZero : $secZero"

            }

            override fun onFinish() {
                timer()
            }
        }
        cT?.start()
    }
    private fun termsAlert() {
        val mDialogView =
            LayoutInflater.from(this@Streak).inflate(R.layout.streak_text, null)


        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        mBuilder.setOnDismissListener {
            (mDialogView.parent as ViewGroup).removeView(mDialogView)
        }
        val mAlertDialog = mBuilder.create()

        mDialogView.agreeBtn.setOnClickListener {


//            val mAlertDialog = mBuilder.show()
            mBuilder.setOnDismissListener {
                (mDialogView.parent as ViewGroup).removeView(mDialogView)
            }

            mAlertDialog.dismiss()

        }

        // val mAlertDialog = mBuilder.show()
        mAlertDialog.show()
        //mAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        mAlertDialog.window!!.setBackgroundDrawableResource(
            R.drawable.feedbck_alertdialog_rounnd
        )

    }
    override fun onDestroy() {
        super.onDestroy()
        cT?.cancel()
    }
}