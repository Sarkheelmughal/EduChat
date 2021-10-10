package com.example.educhat.Functional

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.educhat.Model.Model
import com.example.educhat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_snap_answer.*

class SnapAnswer : AppCompatActivity() {
    private val user = FirebaseAuth.getInstance().currentUser
    var currentUserDataId = user!!.uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snap_answer)

        backFromSnapAns.setOnClickListener {
            onBackPressed()
        }

      //  quesPic()

        val model=intent.getParcelableExtra<Model>("model")

        val ref=FirebaseDatabase.getInstance().reference.child("ExpertAnswer")
            .child(currentUserDataId).child(model!!.fileName)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                     if (snapshot.exists()){

                         answer.text=snapshot.child("answer").value.toString()
                         val url=snapshot.child("url").value.toString()
                         quesPic(url)
                     }
                    else
                        ToastFun()
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })

    }

    fun ToastFun(){
        progress_bar.visibility=View.INVISIBLE

        Toast.makeText(this, "No Response!", Toast.LENGTH_SHORT).show()
    }

    private fun quesPic(url: String) {

        if (user != null) {
            progress_bar.visibility=View.INVISIBLE
                quesPicIV.setBackgroundResource(0)
                Glide.with(this).load(url).into(quesPicIV)

        }
    }
}


