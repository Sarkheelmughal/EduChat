package com.example.educhat.Game

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.educhat.R

import androidx.appcompat.app.AppCompatActivity
import com.example.educhat.Functional.Home
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.subject_selection_activity.*

class SubjectSelection : AppCompatActivity() {
    var subject = "null"
    val uid = FirebaseAuth.getInstance().currentUser!!.uid
    var myMarks="0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.subject_selection_activity)


        getMarks()
        backFromSSCV.setOnClickListener { onBackPressed() }

        bioGameSelectCV.setOnClickListener {
            subject = "Bio"

            bioGameSelectCV.setCardBackgroundColor(Color.rgb(181, 237, 250))
            physGameSelectCV.setCardBackgroundColor(Color.rgb(255, 255, 255))
            chemGameSelectCV.setCardBackgroundColor(Color.rgb(255, 255, 255))
        }
        physGameSelectCV.setOnClickListener {
            subject = "Physics"
            physGameSelectCV.setCardBackgroundColor(Color.rgb(181, 237, 250))
            chemGameSelectCV.setCardBackgroundColor(Color.rgb(255, 255, 255))
            bioGameSelectCV.setCardBackgroundColor(Color.rgb(255, 255, 255))
        }
        chemGameSelectCV.setOnClickListener {
            subject = "Chemistry"
            chemGameSelectCV.setCardBackgroundColor(Color.rgb(181, 237, 250))
            physGameSelectCV.setCardBackgroundColor(Color.rgb(255, 255, 255))
            bioGameSelectCV.setCardBackgroundColor(Color.rgb(255, 255, 255))
        }

        playBtn.setOnClickListener {
            val intent = Intent(this, PlayGame::class.java)
            if (subject == "null") {
                Toast.makeText(this, "Please Select a subject", Toast.LENGTH_SHORT).show()
            } else {
                intent.putExtra("subject", subject)
                intent.putExtra("myMarks", myMarks)
                startActivity(intent)
            }
        }
    }


    private fun getMarks() {
        val refForGameRoom: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("QuizGameMarks")
                .child("9th").child(uid)

        refForGameRoom.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {


                    Log.d("marksTotalFromFB", snapshot.child("marks").value.toString())
                    myMarks=snapshot.child("marks").value.toString()
                }
            }

        })
    }

    override fun onBackPressed() {
        super.onBackPressed()

        startActivity(Intent(this, Home::class.java))

    }
}