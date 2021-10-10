package com.example.educhat.Functional

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.educhat.Game.SubjectSelection
import com.example.educhat.Login.StartScreen
import com.example.educhat.Model.VideoModel
import com.example.educhat.R
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*

class Home : AppCompatActivity() {

    private lateinit var calendar: Calendar
    private val user = FirebaseAuth.getInstance().currentUser
    var currentUserDataId = user!!.uid

    var cT: CountDownTimer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        manageStreak()

        name()
        streakFun()
        profilePic()
        setDataVideo()

        progressReportCv.setOnClickListener {
            startActivity(Intent(this, Learning_analysis::class.java))

        }
        homeNameTV.setOnClickListener {
            Toast.makeText(this, "Logging out..", Toast.LENGTH_SHORT).show()
            SignOut()
            //customViewLogout.visibility = View.INVISIBLE
            startActivity(Intent(this, StartScreen::class.java))

        }

        streakStartCV.setOnClickListener {
            startActivity(Intent(this, Streak::class.java))
        }
        samp.setOnClickListener {
            PB.visibility=View.VISIBLE
            Toast.makeText(this, "Creating Recommendations!", Toast.LENGTH_SHORT).show()
            getDataVideo()
        }
        askCV.setOnClickListener {
            startActivity(Intent(this, Snap::class.java))
        }
        streakCV.setOnClickListener {
            startActivity(Intent(this, Streak::class.java))
        }
        profileImage.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
        }
        quizGameCV.setOnClickListener {

            val intent = Intent(this, SubjectSelection::class.java)
            startActivity(intent)

        }

    }

    private fun getDataVideo() {

        var list= ArrayList<VideoModel>()
        val intent = Intent(this, PlayerActivity::class.java)


        val ref = FirebaseDatabase.getInstance().reference.child("Recommended")


        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    PB.visibility=View.GONE
                    for (a in snapshot.children) {
                      list.add(
                          VideoModel(
                              a.child("name").value.toString(),
                              a.child("url").value.toString()
                      )
                      )
                    }
                    val random=(0 until 5).random()
                    intent.putExtra(
                        "url",
                        list[random].url
                    )
                    intent.putExtra("name", list[random].name)
                    startActivity(intent)

                }
                else

                    Log.d("recommended","error")
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
    private fun setDataVideo() {

        var list= ArrayList<VideoModel>()
        val intent = Intent(this, PlayerActivity::class.java)


        val ref = FirebaseDatabase.getInstance().reference.child("Recommended")


        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    PB.visibility=View.GONE
                    for (a in snapshot.children) {
                        list.add(
                            VideoModel(
                                a.child("name").value.toString(),
                                a.child("url").value.toString()
                            )
                        )
                    }
                    val random=(0 until 5).random()

                    homeRevisionTopinName.text=  list[random].name


                }
                else

                    Log.d("recommended","error")
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
    private fun name() {
        val SPforName = getSharedPreferences("nameStorage", Context.MODE_PRIVATE)
        val SPforNameSaved = SPforName.getString("name", "null")

        homeNameTV.setText("Hey, $SPforNameSaved.\nWhat would You \nlike to learn today?")

    }

    private fun profilePic() {

        if (user != null) {
            if (user.photoUrl != null) {
                profile_image.setBackgroundResource(0)
                Glide.with(this).load(user.photoUrl).into(profile_image)
            }
        }
    }

    private fun streakFun() {
        val calendar = Calendar.getInstance()
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)

        val sPofStreakCounter = getSharedPreferences("streak_pref", MODE_PRIVATE)
        val editorsPofStreakCounter = sPofStreakCounter.edit()

        val lastDay = sPofStreakCounter.getInt(
            "streak_pref",
            0
        )

        //---------------start get last day on test given 2
        val sPofLastTest = getSharedPreferences("streak_pref_last_test", MODE_PRIVATE)
        val editorsPofLastTest = sPofLastTest.edit()
        val lastTestDay = sPofLastTest.getInt(
            "streak_pref_LT",
            0
        )
        //---------------end get last day on test given
        val userDataRef: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("RecordsOFStreakTests")
                .child(currentUserDataId).child("0_Streaks")


        if (dayOfYear - lastTestDay > 1) {
            streakTv.text = "Streak 0"
        } else {
            userDataRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val streak = snapshot.child("totalStreaks").value.toString()
//                    streakTv.text = "Streak " + "${lastDay.toString()}"
                        streakTv.text = "Streak " + "$streak"
                        editorsPofStreakCounter.putInt("streak_pref", streak.toInt())
                        editorsPofStreakCounter.apply()
                    } else {
                        streakTv.text = "Streak " + "${lastDay.toString()}"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }
    }

    private fun SignOut() {
        val auth = FirebaseAuth.getInstance()
        auth.signOut()
        LoginManager.getInstance().logOut()

        val currentUsers = FirebaseAuth.getInstance().currentUser
        currentUsers === null
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



        if (year - lastTestYear == 0) {
            when {
                dayOfYear - lastTestDay == 0 -> {
                    //  binding.timerHomeTV.text = "Come Tomorrow!"
//                    binding.todaysHomeTV.text = "You are done for today."
                    start_now_tv.text = "Come back after"
//                    streakStartOrEnd.text = "Streak Starts in"
                    streakStartOrEnd.visibility = View.INVISIBLE
                    timer()
                    // streakStartCV.isClickable=false
                }
                dayOfYear - lastTestDay > 1 -> {
                    editorsPofStreakCounter.putInt("streak_pref", 0)
                    editorsPofStreakCounter.apply()

                    //streakStartCV.isClickable=true

                    timer()
                }
                else -> {
                    //streakStartCV.isClickable=true

                    timer()
                }
            }
        } else if (year - lastTestYear >= 1) {
            //editorsPofStreakCounter.putInt("streak_pref",steakCount+1)
            streakStartCV.isClickable = true

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


        var secZero = "0"
        secZero = if (secSub <= 9) {
            "0$secSub"
        } else {
            "$secSub"
        }

        var mintZero = "0"
        mintZero = if (minSub <= 9) {
            "0$minSub"
        } else {
            "$minSub"
        }

        var hourZero = "0"
        hourZero = if (hourSub <= 9) {
            "0$hourSub"
        } else {
            "$hourSub"
        }


        cT = object : CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                timerHomeTV.text = "$hourZero : $mintZero : $secZero"

            }

            override fun onFinish() {
                timer()
            }
        }
        cT?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        cT?.cancel()
    }
}