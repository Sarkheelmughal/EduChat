package com.example.educhat.Game

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_play_game.*
import com.example.educhat.Model.McqsModel
import com.example.educhat.R


class PlayGame : AppCompatActivity() {
    val uid = FirebaseAuth.getInstance().currentUser!!.uid
    var stopper = 0
    var pp: Long = 0
    var hh: Long = 0
    var mm: Long = 0
    var mcqCounter = 0
    var fulllistofqus = ArrayList<McqsModel>()
    var listToDisplay = ArrayList<McqsModels>()
    var storeRandomofFull = ArrayList<McqsModel>(fulllistofqus.size)
    var random: Int? = null
    var getTotalmcqs: Int? = null
    var skipMcq: Int = 0
    var trueMcq: Int = 0
    var falseMcq: Int = 0
    var statusChecker = 0
    var counter = 1
    var sumOfCode = 0
    var code: Int = 0

    var autoboot: String = "null"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_game)
        val subject = intent.getStringExtra("subject")
        code = (1000 until 9999).random()


        backIV.setOnClickListener { onBackPressed() }
        hideView()
        val userDataRef: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("QuizGame").child("9th")
                .child("waitingRoom").child(subject!!)


        var connected = false
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        connected =
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED

        if (!connected) {
            Toast.makeText(this, "Make sure you are connected to an Internet!", Toast.LENGTH_LONG).show()

            finish()
        }

        else {

            checkTextBtn.setOnClickListener {
                timer.cancel()


                Log.d("mcqCounterCheckText", mcqCounter.toString())
                Log.d("marksTestingskip", skipMcq.toString())
                Log.d("marksTestingtrue", trueMcq.toString())
                Log.d("marksTestingfalse", falseMcq.toString())

                if (mcqCounter < 4) {
                    checkMcqs()
                    pasteEmptyResults(subject)
                    textsetter()
                    pasteResults(subject)


                    Log.d("marksTestingif", "if")

                } else {
                    Log.d("marksTestingif", "else")

                    pasteEmptyResults(subject)
                    checkMcqs()


                    //  skipMcq=5 -(trueMcq+falseMcq)

                    pasteResults(subject)
                    val myMarks = intent.getStringExtra("myMarks")

//                Toast.makeText(this, "Test End", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, GameResult::class.java)
                    intent.putExtra("trueMcq", trueMcq)
                    intent.putExtra("falseMcq", falseMcq)
                    intent.putExtra("skipMcq", skipMcq)
                    intent.putExtra("subject", subject)
                    intent.putExtra("myMarks", myMarks)
                    intent.putExtra("sumOfCode", sumOfCode)
                    intent.putExtra("autoboot", autoboot)
                    timer.cancel()
                    startActivity(intent)
                }
//            mcqCounter++

            }
            waitingRoom(subject, code)


            Handler().postDelayed({

                if (statusChecker == 0) {
                    statusChecker++

                    checkOnlinePlayers(userDataRef, code)
                    Log.d("handler", "handler")
                    //Toast.makeText(this, "handler", Toast.LENGTH_SHORT).show()
                }
            }, 10000)

            mcqOneRb.setOnClickListener {
                mcqTwoRb.isChecked = false
                mcqThreeRb.isChecked = false
                mcqFourRb.isChecked = false

                mcqTwoRb.isClickable = false
                mcqThreeRb.isClickable = false
                mcqFourRb.isClickable = false
            }
            mcqTwoRb.setOnClickListener {
                mcqOneRb.isChecked = false
                mcqThreeRb.isChecked = false
                mcqFourRb.isChecked = false

                mcqOneRb.isClickable = false
                mcqThreeRb.isClickable = false
                mcqFourRb.isClickable = false
            }
            mcqThreeRb.setOnClickListener {
                mcqTwoRb.isChecked = false
                mcqOneRb.isChecked = false
                mcqFourRb.isChecked = false
             mcqTwoRb.isClickable = false
                mcqOneRb.isClickable = false
                mcqFourRb.isClickable = false
            }
            mcqFourRb.setOnClickListener {
                mcqTwoRb.isChecked = false
                mcqThreeRb.isChecked = false
                mcqOneRb.isChecked = false
                 mcqTwoRb.isClickable = false
                mcqThreeRb.isClickable = false
                mcqOneRb.isClickable = false
            }

        }
    }

     var timer: CountDownTimer= object : CountDownTimer(21000, 1500) {
        override fun onTick(millisUntilFinished: Long) {
//            time--
//            set_timer_opt_game.text = time.toString()
        }

        override fun onFinish() {
//            checkTextBtn.performClick()
        }
    }

    private fun timerText() {
        set_timer_opt_game
        var time = 21
        timer = object : CountDownTimer(21000, 1500) {
            override fun onTick(millisUntilFinished: Long) {
                time--
                set_timer_opt_game.text = time.toString()
            }

            override fun onFinish() {
                checkTextBtn.performClick()
            }
        }
        timer.start()
    }

    fun hideView() {
        Log.i("funplay", "hideView")
        headerTV.visibility = View.GONE
        screenNumber.visibility = View.GONE
        parentLL.visibility = View.GONE
    }

    fun unHideView() {
        Log.i("funplay", "unHideView")
        waitingAnim.visibility = View.GONE
        pleaseWaitTV.visibility = View.GONE
        headerTV.visibility = View.VISIBLE
        screenNumber.visibility = View.VISIBLE
        parentLL.visibility = View.VISIBLE
    }

    private fun uncheckRadioBtn() {
        Log.i("funplay", "uncheckRadioBtn")

        mcqOneRb.isChecked = false
        mcqTwoRb.isChecked = false
        mcqThreeRb.isChecked = false
        mcqFourRb.isChecked = false
    }

    private fun pasteResults(subject: String?) {
        Log.i("funplay", "pasteResults")

        val refForGameRoom: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("QuizGame").child("9th")
                .child("GameRoom").child(subject!!)

        // skipMcq=5 -(trueMcq+falseMcq)


        val userMap = HashMap<Any, Any>()
        userMap["true"] = trueMcq
        userMap["false"] = falseMcq
        userMap["skip"] = skipMcq

        refForGameRoom.child(sumOfCode.toString()).child("Mcqs").child("Results").child(uid)
            .setValue(userMap)

    }

    private fun pasteEmptyResults(subject: String?) {
        Log.i("funplay", "pasteEmptyResults")

        val refForGameRoom: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("QuizGame").child("9th")
                .child("GameRoom").child(subject!!)

        // skipMcq=5 -(trueMcq+falseMcq)


        val userMap = HashMap<Any, Any>()
        userMap["true"] = trueMcq
        userMap["false"] = falseMcq
        userMap["skip"] = skipMcq

        refForGameRoom.child(sumOfCode.toString()).child("Mcqs").child("Results").child(uid)
            .setValue(userMap)

    }

    private fun checkMcqs() {
        Log.i("funplay", "checkMcqs")

        Log.d("correctAns", listToDisplay[mcqCounter].correctOption)
        Log.d("checkMcqTest", "checkMcqTest")

        counter++
        Log.d("questionCounter", counter.toString())
        screenNumber.setText("Question $counter")


        if (mcqOneRb.isChecked == false && mcqTwoRb.isChecked == false && mcqThreeRb.isChecked == false && mcqFourRb.isChecked == false) {
            skipMcq++
        }

        if (mcqOneRb.isChecked == true && listToDisplay[mcqCounter].correctOption == mcqOneRb.text) {
            trueMcq++
        } else if (mcqTwoRb.isChecked == true && listToDisplay[mcqCounter].correctOption == mcqTwoRb.text) {
            trueMcq++
        } else if (mcqThreeRb.isChecked == true && listToDisplay[mcqCounter].correctOption == mcqThreeRb.text) {
            trueMcq++
        } else if (mcqFourRb.isChecked == true && listToDisplay[mcqCounter].correctOption == mcqFourRb.text) {
            trueMcq++
        } else if (mcqOneRb.isChecked == true && listToDisplay[mcqCounter].correctOption != mcqOneRb.text) {
            falseMcq++
        } else if (mcqTwoRb.isChecked == true && listToDisplay[mcqCounter].correctOption != mcqTwoRb.text) {
            falseMcq++
        } else if (mcqThreeRb.isChecked == true && listToDisplay[mcqCounter].correctOption != mcqThreeRb.text) {
            falseMcq++
        } else if (mcqFourRb.isChecked == true && listToDisplay[mcqCounter].correctOption != mcqFourRb.text) {
            falseMcq++
        }
        Log.d("checkMcqTrue", trueMcq.toString())
        Log.d("checkMcqFalse", falseMcq.toString())
        Log.d("checkMcqSkip", skipMcq.toString())
        Log.d("checkMcqQuestion", listToDisplay[mcqCounter].question)
        Log.d("checkMcqCorrect", listToDisplay[mcqCounter].correctOption)

        uncheckRadioBtn()
        mcqCounter++

    }

    private fun waitingRoom(subject: String?, code: Int) {
        Log.i("funplay", "waitingRoom")

        val PREFERENCE_FILE_KEY_MENU = "AppPreferenceMENU"
        val sharedPrefMenu =
            this.getSharedPreferences(PREFERENCE_FILE_KEY_MENU, Context.MODE_PRIVATE)
        val name = sharedPrefMenu.getString("menu_name", "Player")

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val userDataRef: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("QuizGame").child("9th")
                .child("waitingRoom").child(subject!!)


        val userMap = HashMap<Any, Any>()
        userMap["uid"] = uid
        userMap["subject"] = subject
        userMap["code"] = code
        userMap["playerName"] = name.toString()

        userDataRef.child(uid).setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    checkOnlinePlayers(userDataRef, code)
                    // Toast.makeText(this,"Account has been created successfully:)",Toast.LENGTH_SHORT).show()

                } else {

                    Toast.makeText(this, "Something Went Wrong:(", Toast.LENGTH_LONG).show()
                    //  FirebaseAuth.getInstance().signOut()
                }
            }
    }

    private fun checkOnlinePlayers(userDataRef: DatabaseReference, code: Int) {
        Log.i("funplay", "checkOnlinePlayers")

        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        userDataRef.limitToFirst(1).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0!!.exists()) {

//                    if (p0.childrenCount >= 2){
//                        Log.d("playerStatus",p0.childrenCount.toString())


                    for (a in p0.children) {

                        val playerUid = a.child("uid").value.toString()

                        if (playerUid != uid) {
                            if (stopper == 0) {
                                stopper++
                                Log.d("stopper", "stopper == 0")
//                                goForMatch(a, userDataRef,code)


//                                    if(statusChecker==0) {
//                                        statusChecker++

                                // Handler().postDelayed({
                                goForMatch(a, userDataRef, code)

                                // },5000)

                                // }
//                                else{
//                                        goForMatch(a, userDataRef,code)
//
//                                    }
                            }
                            Log.d("uidOfPlayers", a.child("uid").value.toString())
                            Log.d("uidOfPlayersKey", a.toString())
                        } else if (playerUid == uid) {


                            sameUidResolver(userDataRef, code)


                        }

                    }


//                    }
//                    else{
//
//                        toastFun()
//                    }


                } else {
//
//                    toastFun()
                }

            }

        })
    }

    private fun sameUidResolver(userDataRef: DatabaseReference, code: Int) {
        Log.i("funplay", "sameUidResolver")

        var loopCounter = 0
        userDataRef.limitToFirst(2).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (a in snapshot.children) {
                        val playerUid = a.child("uid").value.toString()

                        loopCounter++

                        Log.d("loopCounter", loopCounter.toString())
                        Log.d("sameUidResolver", playerUid)
                        if (playerUid != uid) {

                            Log.d("sameUidResolver2", playerUid)
                            // goForMatch(a, userDataRef,code)

//                            if(statusChecker==0) {
//                                statusChecker++

                            // Handler().postDelayed({
                            goForMatch(a, userDataRef, code)

                            //  },5000)

                            // }
//                            else{
//                                goForMatch(a, userDataRef,code)
//
//                            }
                        }
//                        else if (statusChecker==1){
//                           if(playerUid==uid) {
//                              // noPlayerToast()
//                               goForMatchWithBoot()
//                           }
//                        }
                    }
                    if (statusChecker == 1) {
                        if (loopCounter == 1) {

                            goForMatchWithBoot()
                        }
                    }
                } else {
                    // noPlayerToast()
                }
            }
        })
    }

    private fun goForMatchWithBoot() {
        Log.i("funplay", "goForMatchWithBoot")

        val name = arrayOf(
            "Ali",
            "Akbar",
            "Nadia",
            "Ahmed",
            "Farooq",
            "Saif",
            "Kamran",
            "Umar",
            "Ayesha",
            "Rubab",
            "Sana",
            "Merab",
            "Fatima",
            "Umair",
            "Danish",
            "Haider",
            "Aqsa",
            "Maryam",
            "Mahira",
            "Hoorain",
            "Laiba",
            "Aamir",
            "Ayaan",
            "Arham",
            "Anabia",
            "Azhar",
            "Sohail",
            "Afnan",
            "Aleeza",
            "Kashif",
            "Shahzaib",
            "Areesha",
            "Areeba",
            "Hiba",
            "Manahil",
            "Rimsha",
            "Noman",
            "Haris",
            "Babar",
            "Sufian",
            "Junaid",
            "Zeeshan",
            "Hamza",
            "Neha"
        )
        val nameRandom = (0 until name.size - 1).random()

        val sharedPrefFile = "opponentName"
        val sharedPreferences: SharedPreferences =
            this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)


        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("name_key", name[nameRandom])
        //  editor.apply()
        editor.commit()

        Toast.makeText(
            this,
            "Match is about to start with " + name[nameRandom] + " !",
            Toast.LENGTH_SHORT
        ).show()
        val subject = intent.getStringExtra("subject")

        val refForGameRoom: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("QuizGame").child("9th")
                .child("GameRoom").child(subject!!)
        //textsetter(mcqCounter)
        val timestamp: Long = System.currentTimeMillis()
        autoboot = "yes"
        sumOfCode = code

        removeFromWaitingRoom()
        getAllMcqs(refForGameRoom, timestamp, sumOfCode)

    }

    private fun noPlayerToast() {
        Toast.makeText(this, "No player online", Toast.LENGTH_SHORT).show()

    }

    private fun removePlayerFromWaiting(a: DataSnapshot?, userDataRef: DatabaseReference) {
        Log.i("funplay", "removePlayerFromWaiting")

        val uidOfOther = a?.child("uid")?.value.toString()
        userDataRef.child(uidOfOther).setValue(null)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {


                    // Toast.makeText(this,"Account has been created successfully:)",Toast.LENGTH_SHORT).show()

                } else {
                    val message = task.exception!!.toString()
                    Toast.makeText(this, "Something Went Wrong:(", Toast.LENGTH_LONG).show()
                    //  FirebaseAuth.getInstance().signOut()
                }
            }

        userDataRef.child(uid).setValue(null)
    }

    private fun goForMatch(a: DataSnapshot, userDataRef: DatabaseReference, code: Int) {
        Log.i("funplay", "goForMatch")

        val subject = intent.getStringExtra("subject")
        val timestamp: Long = System.currentTimeMillis()
        val refForGameRoom: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("QuizGame").child("9th")
                .child("GameRoom").child(subject!!)

        val otherPlayerCode = a.child("code").value.toString().toInt()

        sumOfCode = code + otherPlayerCode


        val userMap = HashMap<Any, Any>()
        userMap["PlayerOne"] = a.child("uid").value.toString()
        userMap["PlayerTwo"] = uid

        if (statusChecker == 0) {
            refForGameRoom.child(sumOfCode.toString()).setValue(userMap)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val sharedPrefFile = "opponentName"
                        val sharedPreferences: SharedPreferences =
                            this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)


                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putString("name_key", a.child("playerName").value.toString())
                        // editor.apply()
                        editor.commit()
//                        removePlayerFromWaiting(a, userDataRef)
                        Toast.makeText(
                            this,
                            "Go for Match with " + a.child("playerName").value.toString(),
                            Toast.LENGTH_SHORT
                        ).show()

                        getAllMcqs(refForGameRoom, timestamp, sumOfCode)

                        if (statusChecker == 1) {
                            removePlayerFromWaiting(a, userDataRef)
                        }

                        // Toast.makeText(this,"Account has been created successfully:)",Toast.LENGTH_SHORT).show()


                    } else {
                        val message = task.exception!!.toString()
                        Toast.makeText(this, "Something Went Wrong:(", Toast.LENGTH_LONG).show()
                        //  FirebaseAuth.getInstance().signOut()
                    }
                }

        } else {
//            removePlayerFromWaiting(a, userDataRef)
            Toast.makeText(
                this,
                "Go for Match with " + a.child("playerName").value.toString(),
                Toast.LENGTH_SHORT
            ).show()

            getMcqsToDisplay(refForGameRoom, timestamp, sumOfCode)

        }


    }

    private fun getAllMcqs(refForGameRoom: DatabaseReference, timestamp: Long, sumOfCode: Int) {
        Log.i("funplay", "getAllMcqs")
        val subject = intent.getStringExtra("subject")
        var ref = FirebaseDatabase.getInstance().getReference("contents")

        var checker = 0

        var refSubject = "null"
        if (subject == "Bio") {
            refSubject = "biology"
            getTotalmcqs = 139

        } else if (subject == "Physics") {
            refSubject = "physics"
            getTotalmcqs = 143

        } else if (subject == "Chemistry") {
            refSubject = "chemistry"
            getTotalmcqs = 239

        }
        if (refSubject != "null") {
            checker++
            ref =
                FirebaseDatabase.getInstance().getReference("contents").child("topic_questions")
                    .child("class9th").child("subjects").child(refSubject)
        } else {
            Toast.makeText(this, "Path is incomplete!", Toast.LENGTH_SHORT).show()
        }

        if (checker == 1) {
            ref?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
                }

                override fun onDataChange(p0: DataSnapshot) {

                    if (p0.exists()) {
                        Log.d("totalChildrenEXits", "totalChildren")
                        pp = p0.childrenCount
                        Log.d("totalChildrenCountpp :", pp.toString())

                        for (h in p0.children) {
                            hh = h.childrenCount
                            Log.d("totalChildrenCounthh :", hh.toString())



                            for (s in h.children) {

                                Log.d("totalChildrenCounts", s.childrenCount.toString())
                                for (m in s.children) {
                                    mm = m.childrenCount
                                    Log.d("totalChildrenCountmm :", mm.toString())

                                    val opt =
                                        McqsModel(
                                            m.child("optionA").value.toString(),
                                            m.child("optionB").value.toString(),
                                            m.child("optionC").value.toString(),
                                            m.child("optionD").value.toString(),
                                            m.child("correctOption").value.toString(),
                                            m.child("question").value.toString()
                                        )
                                    fulllistofqus.add(opt)
                                    Log.d(
                                        "totalChildrenCountQue",
                                        m.child("question").value.toString()
                                    )


                                }
                            }
                        }
                        Log.d("totalChildrenCountsub", subject.toString())
                        Log.d("fulllistofqus", fulllistofqus.toString())
//                        if (statusChecker==0) {
                        pasteAllMcqsToBackend(refForGameRoom, timestamp, sumOfCode)
//                        }
//                        else{
//                            getMcqsToDisplay(refForGameRoom,timestamp,sumOfCode)

//                        }

                    }


                }

            })

        } else {
            Toast.makeText(this, "Fail to getting Mcqs!", Toast.LENGTH_SHORT).show()

        }


    }

    private fun pasteAllMcqsToBackend(
        refForGameRoom: DatabaseReference,
        timestamp: Long,
        sumOfCode: Int
    ) {
//        random = (0 until 5).random()
        Log.i("funplay", "pasteAllMcqsToBackend")

        for (a in 1..5) {
            random = (0 until (getTotalmcqs!!)).random()

            Log.d("random1", random.toString())
            Log.d("random2", fulllistofqus.size.toString())
            storeRandomofFull.add(fulllistofqus[random!!])

        }
        Log.d("storeRandomofFulllist1", storeRandomofFull.size.toString())
        Log.d("storeRandomofFulllist2", storeRandomofFull.toString())


        val userMap = HashMap<Any, Any>()
        userMap["McqsList"] = storeRandomofFull

        refForGameRoom.child(sumOfCode.toString()).child("Mcqs").setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    getMcqsToDisplay(refForGameRoom, timestamp, sumOfCode)

                } else {
//                    Toast.makeText(this, "Something Went Wrong:(", Toast.LENGTH_LONG).show()
                }
            }

    }

    private fun getMcqsToDisplay(
        refForGameRoom: DatabaseReference,
        timestamp: Long,
        sumOfCode: Int
    ) {
        Log.i("funplay", "getMcqsToDisplay")

        refForGameRoom.child(sumOfCode.toString()).child("Mcqs")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {


                    if (snapshot.exists()) {

                        snapshot.child("McqsList").value.toString()
                        Log.d("listToDisplay5", snapshot.child("McqsList").value.toString())
                        Log.d(
                            "listToDisplay6",
                            snapshot.child("McqsList").child("0").child("optionA").value.toString()
                        )

                        for (n in snapshot.children) {

                            for (m in n.children) {
                                val mcqData = McqsModels(
                                    m.child("optionA").value.toString(),
                                    m.child("optionB").value.toString(),
                                    m.child("optionC").value.toString(),
                                    m.child("optionD").value.toString(),
                                    m.child("correctOption").value.toString(),
                                    m.child("question").value.toString()
                                )
                                listToDisplay.add(mcqData!!)
                                Log.d("listToDisplay", mcqData.toString())

                            }
                        }
//                   var mcqData:McqsModels?= snapshot.child("McqsList").getValue(McqsModels::class.java)
                        Log.d("listToDisplay1", listToDisplay.toString())

                        //removePlayerFromWaiting(a, userDataRef)

                        textsetter()



                        Log.d("listToDisplay2", listToDisplay.toString())
                    } else {
                        Log.i("elseOfFunData", "getMcqsToDisplay")
                    }
                }

            })
    }


    private fun textsetter() {
        Log.i("funplay", "textsetter")

        Log.d("mcqCounterTextSetter", mcqCounter.toString())
        unHideView()
        timerText()
        val subject = intent.getStringExtra("subject")

        mcqTwoRb.isClickable = true
        mcqThreeRb.isClickable = true
        mcqOneRb.isClickable = true
        mcqFourRb.isClickable = true


        if (mcqCounter < 5) {
            questionMainText.setText(listToDisplay[mcqCounter].question)
            mcqOneRb.setText(listToDisplay[mcqCounter].optionA)
            mcqTwoRb.setText(listToDisplay[mcqCounter].optionB)
            mcqThreeRb.setText(listToDisplay[mcqCounter].optionC)
            mcqFourRb.setText(listToDisplay[mcqCounter].optionD)

        }
        if (statusChecker == 1) {
            // removePlayerFromWaiting(a, userDataRef)

            removeFromWaitingRoom()
        }
        statusChecker++

//        mcqCounter++

    }

    private fun toastFun() {
        Toast.makeText(
            this,
            "It may took a while we are finding the best match!",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        timer.cancel()
        removeFromWaitingRoom()
    }

    private fun removeFromWaitingRoom() {
        Log.i("funplay", "removeFromWaitingRoom")
        val subject = intent.getStringExtra("subject")
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val userDataRef: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("QuizGame").child("9th")
                .child("waitingRoom").child(subject!!)


        userDataRef.child(uid).setValue(null)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    // Toast.makeText(this,"Account has been created successfully:)",Toast.LENGTH_SHORT).show()

                } else {
                    val message = task.exception!!.toString()
                    Toast.makeText(this, "Something Went Wrong:(", Toast.LENGTH_LONG).show()
                    //  FirebaseAuth.getInstance().signOut()
                }
            }

        // finish()
    }


}

data class McqsModels(
    val optionA: String = "",
    val optionB: String = "",
    val optionC: String = "",
    val optionD: String = "",
    val correctOption: String = "",
    val question: String = ""
)