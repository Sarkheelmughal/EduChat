package com.example.educhat.Game

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_game_result.*
import com.example.educhat.R


class GameResult : AppCompatActivity() {

    val uid = FirebaseAuth.getInstance().currentUser!!.uid

    var myTotalMArks="0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_result)
        val myMarks = intent.getStringExtra("myMarks")

        val trueMcq = intent.getIntExtra("trueMcq", 0)
        val falseMcq = intent.getIntExtra("falseMcq", 0)
        val skipMcq = intent.getIntExtra("skipMcq", 0)
        val autoboot = intent.getStringExtra("autoboot")

        myTotalMArks=(myMarks.toString().toInt()+trueMcq).toString()
        pasteMarks()

        pasteForReport(trueMcq,falseMcq,skipMcq)

            resultBack.setOnClickListener { startActivity(Intent(this, SubjectSelection::class.java)) }
//        playAgainBtn.setOnClickListener {
//            startActivity(
//                Intent(
//                    this,
//                    SubjectSelection::class.java
//                )
//            )
//        }
       // viewLeaderBtn.setOnClickListener { startActivity(Intent(this, LeaderBoard::class.java)) }
        if (autoboot == "yes") {
            autobootMarks(trueMcq, falseMcq, skipMcq)
        } else if (autoboot == "null") {
            opponentMarks(trueMcq, falseMcq, skipMcq)
        }

//        trueTV.setText("True: " +trueMcq)
//        falseTV.setText("False: " +falseMcq)
//        skipTV.setText("Skip: " +skipMcq)

        assignMarks(trueMcq, falseMcq, skipMcq)

        val sharedPrefFile = "opponentName"
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)

        val name=sharedPreferences.getString("name_key","Opponent")

        opponentNameTV.setText(name)

    }

    private fun pasteForReport(trueMcq: Int, falseMcq: Int, skipMcq: Int) {
        val refForGameRoom: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("ProgressReport")
              .child(uid)

        val userMap1 = HashMap<Any, Any>()

        userMap1["trueMcq"] = trueMcq
        userMap1["falseMcq"] = falseMcq
        userMap1["skipMcq"] = skipMcq

        refForGameRoom.setValue(userMap1)
    }

    private fun assignMarks(trueMcq: Int, falseMcq: Int, skipMcq: Int) {

        when (trueMcq) {
            0 -> {
                myMarksTV.setText("0 Marks")
            }
            1 -> {
                myMarksTV.setText("10 Marks")
            }
            2 -> {
                myMarksTV.setText("20 Marks")
            }
            3 -> {
                myMarksTV.setText("30 Marks")
            }
            4 -> {
                myMarksTV.setText("40 Marks")
            }
            5 -> {
                myMarksTV.setText("50 Marks")
            }
        }
    }

    private fun autobootMarks(trueMcq: Int, falseMcq: Int, skipMcq: Int) {

//        val number = arrayOf(0, 10, 20, 30, 40, 50)

        val ran = (0 until 5).random()

        when (ran) {
            0 -> {
                opponentMarksTV.setText("0 Marks")
            }
            1 -> {
                opponentMarksTV.setText("10 Marks")
            }
            2 -> {
                opponentMarksTV.setText("20 Marks")
            }
            3 -> {
                opponentMarksTV.setText("30 Marks")
            }
            4 -> {
                opponentMarksTV.setText("40 Marks")
            }
            5 -> {
                opponentMarksTV.setText("50 Marks")
            }
        }

//        skipOptTV.setText("Skip 0")

//       val falseRandon=(0 until  5).random()
//
//
//        falseOptTV.setText("False: $falseRandon") // 2
//
//        val trueOp=5-falseRandon
//        trueOptTV.setText("True: "+ trueOp) //3
//
//        skipOptTV.setText("Skip: 0")
//      //  skipOptTV.setText("Skip 0") //0
//
//        if (trueMcq < trueOp){
//            finalResultTV.setText("You lose!")
//            loseAnim.visibility=View.VISIBLE
//        }
//        else if(trueMcq > trueOp){
//            finalResultTV.setText("You Win!")
//            winAnim.visibility=View.VISIBLE
//
//
//        }
//        else if(trueMcq == trueOp){
//            finalResultTV.setText("Game Tie!")
//            equalAnim.visibility=View.VISIBLE
//
//
//        }


        if (trueMcq < ran) {
            imageResult.setImageResource(R.drawable.ic_lost_pic)
            headerResultTV.setText("Alas! You Lost.")


        } else if (trueMcq > ran) {
            imageResult.setImageResource(R.drawable.ic_won_pic)
            headerResultTV.setText("Hurry! You Won.")

        } else if (trueMcq == ran) {
            imageResult.setImageResource(R.drawable.game_draw_ic)

            headerResultTV.setText("Game Draw!")
        }
    }
    fun pasteMarks(){

        val refForGameRoom: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("QuizGameMarks")
                .child("9th").child(uid)

        val userMap1 = HashMap<Any, Any>()

        userMap1["marks"] = myTotalMArks

        refForGameRoom.setValue(userMap1)
    }
    fun opponentMarks(trueMcq: Int, falseMcq: Int, skipMcq: Int) {
        val sumOfCode = intent.getIntExtra("sumOfCode", 0)
        val subject = intent.getStringExtra("subject")
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        var opponentTrue = 0
        var opponentFalse = 0
        var opponentSkip = 0
        val refForGameRoom: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("QuizGame").child("9th")
                .child("GameRoom").child(subject!!).child(sumOfCode.toString())
                .child("Mcqs").child("Results")

        refForGameRoom.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    for (a in snapshot.children) {

                        val key = a.key.toString()
                        Log.d("keyofuid", key)

                        if (key != uid) {

                            opponentTrue = a.child("true").value.toString().toInt()
                            opponentFalse = a.child("false").value.toString().toInt()
                            opponentSkip = a.child("skip").value.toString().toInt()

                            Log.d("trueValueOpp", a.child("true").value.toString())
//                            trueOptTV.setText(opponentTrue.toString())
//                            falseOptTV.setText(opponentFalse.toString())
//                            skipOptTV.setText(opponentSkip.toString())


                            when (opponentTrue) {
                                0 -> {
                                    opponentMarksTV.setText("0 Marks")
                                }
                                1 -> {
                                    opponentMarksTV.setText("10 Marks")
                                }
                                2 -> {
                                    opponentMarksTV.setText("20 Marks")
                                }
                                3 -> {
                                    opponentMarksTV.setText("30 Marks")
                                }
                                4 -> {
                                    opponentMarksTV.setText("40 Marks")
                                }
                                5 -> {
                                    opponentMarksTV.setText("50 Marks")
                                }
                            }


                            if (trueMcq < opponentTrue) {
//                                finalResultTV.setText("You lose!")
//                                loseAnim.visibility=View.VISIBLE
                                headerResultTV.setText("Alas! You Lost.")
                                imageResult.setImageResource(R.drawable.ic_lost_pic)
                            } else if (trueMcq > opponentTrue) {
//                                finalResultTV.setText("You Win!")
//                                winAnim.visibility=View.VISIBLE
                                headerResultTV.setText("Hurry! You Won.")
                                imageResult.setImageResource(R.drawable.ic_won_pic)

                            } else if (trueMcq == opponentTrue) {
//                                finalResultTV.setText("Game Tie!")
//                                equalAnim.visibility=View.VISIBLE
                                imageResult.setImageResource(R.drawable.game_draw_ic)

                                headerResultTV.setText("Game Draw!")

                            }

                        }

                    }
                    // if (snapshot.child(uid))


                }
            }

        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, SubjectSelection::class.java))
    }
}