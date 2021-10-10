package com.example.educhat.Functional


import android.content.Intent
import android.graphics.Color
import android.graphics.Color.rgb
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.result_neww.*
import com.example.educhat.R

import java.util.*
import kotlin.collections.HashMap


class ResultActivity : AppCompatActivity() {

    lateinit var currentUserDataId: String

    var falseSum: Float = 0f
    var trueSum: Float = 0f
    var skipSum: Float = 0f
    var truevalues: Float = 0f
    var falsevalue: Float = 0f
    var skipvalue: Float = 0f

    lateinit var key: String
   private var calendar: Calendar=  Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_neww)

        val pieChart = findViewById<PieChart>(R.id.piechart1)
        currentUserDataId = FirebaseAuth.getInstance().currentUser!!.uid

        backTopicFromSample.setOnClickListener() {
            onBackPressed()
        }

        truevalues = intent.getFloatExtra("TrueValues", 0f)
        falsevalue = intent.getFloatExtra("FalseValues", 0f)
        skipvalue = intent.getFloatExtra("SkipValues", 0f)

//        Log.d("resultTrueValues",truevalues.toString())
//        Log.d("resultTrueValues",falsevalue.toString())
//
//        Log.d("resultTrueValues",skipvalue.toString())

//        key=intent.getStringExtra("key")

//        var GetTopicName=intent.getStringExtra("GetTopicName")
//        topicName.setText(GetTopicName)

        val b = intent.getIntExtra("b", 0)
        val sumTime = intent.getIntExtra("sumTime", 0)

        val ecat = intent.getIntExtra("ecat", 0)

        val screen1 = intent.getIntExtra("screen1", 0)
        val screen2 = intent.getIntExtra("screen2", 0)
        val screen3 = intent.getIntExtra("screen3", 0)
        val screen4 = intent.getIntExtra("screen4", 0)
        val screen5 = intent.getIntExtra("screen5", 0)
        val screen6 = intent.getIntExtra("screen6", 0)
        val screen7 = intent.getIntExtra("screen7", 0)
        val screen8 = intent.getIntExtra("screen8", 0)
        val screen9 = intent.getIntExtra("screen9", 0)
        val screen10 = intent.getIntExtra("screen10", 0)
        val entryTest = intent.getStringExtra("entryTest")
        val streakQuiz = intent.getStringExtra("streakQuiz")

        val correctTxt = findViewById<TextView>(R.id.corectTextView)
        correctTxt.setText(truevalues.toInt().toString() + " Correct")

        val incorectText = findViewById<TextView>(R.id.incorectText)
        incorectText.setText(falsevalue.toInt().toString() + " Incorrect")

        val unanswered = findViewById<TextView>(R.id.unanswered)
        unanswered.setText(skipvalue.toInt().toString() + " Unanswered")


        val NoOfEmp = ArrayList<PieEntry>()


        NoOfEmp.add(PieEntry(truevalues))
        NoOfEmp.add(PieEntry(falsevalue))
        NoOfEmp.add(PieEntry(skipvalue))


        val dataSet = PieDataSet(NoOfEmp, "Number Of Employees")


        val data = PieData(dataSet)
        pieChart.data = data


        val MY_COLORS = intArrayOf(
            Color.rgb(57, 255, 33),
            Color.rgb(255, 0, 0),
            Color.rgb(255, 186, 96)
        )
        val colors = ArrayList<Int>()

        for (c in MY_COLORS) colors.add(c)

        dataSet.colors = colors
        dataSet.setDrawValues(false)


        pieChart.animateXY(2000, 2000)
        pieChart.legend.isEnabled = false
        pieChart.description.isEnabled = false
        pieChart.centerText = (truevalues.toInt().toString() + "/" + b.toString()).toString()

        pieChart.setCenterTextColor(rgb(162, 162, 162))

        pieChart.holeRadius = 65f

        //////////////////////////////////////piechart2///////////////////////////////


        val pieChart2 = findViewById<PieChart>(R.id.piechart2)

        val trueper = truevalues
        val falski = falsevalue + skipvalue

        val percent = (trueper / b) * 100

        val NoOfEmp2 = ArrayList<PieEntry>()


        NoOfEmp2.add(PieEntry(trueper))
        NoOfEmp2.add(PieEntry(falski))

        val dataSet2 = PieDataSet(NoOfEmp2, "Number Of Employees")


        val data2 = PieData(dataSet2)
        pieChart2.data = data2


        val MY_COLORS2 = intArrayOf(
            Color.rgb(57, 255, 33),
            Color.rgb(255, 0, 0)
        )
        val colors2 = ArrayList<Int>()

        for (b in MY_COLORS2) colors2.add(b)

        dataSet2.colors = colors2
        dataSet2.setDrawValues(false)


        pieChart2.animateXY(2000, 2000)
        pieChart2.legend.isEnabled = false
        pieChart2.description.isEnabled = false

        pieChart2.holeRadius = 65f


        pieChart2.centerText = (percent.toInt().toString() + " %").toString()

        pieChart2.setCenterTextColor(rgb(162, 162, 162))


        /////////////////////////////////pie3/////////////////////////////////////////////////


        val pieChart3 = findViewById<PieChart>(R.id.piechart3)

        // val trueper=truevalues
        //  val falski=falsevalue+skipvalue


        val scren1 = screen1
        val scren2 = screen2
        val scren3 = screen3
        val scren4 = screen4
        val scren5 = screen5
        val scren6 = screen6
        val scren7 = screen7
        val scren8 = screen8
        val scren9 = screen9
        val scren10 = screen10

        val avgtime = (sumTime / b)

        //  Log.d("time of total timer","sum timer time"+sumTime)
        val timerGraph = sumTime

        val NoOfEmp3 = ArrayList<PieEntry>()

        when (ecat) {
            1 -> {
                NoOfEmp3.add(PieEntry(scren1.toFloat()))
                NoOfEmp3.add(PieEntry(scren2.toFloat()))
                NoOfEmp3.add(PieEntry(scren3.toFloat()))
                NoOfEmp3.add(PieEntry(scren4.toFloat()))
                NoOfEmp3.add(PieEntry(scren5.toFloat()))
                NoOfEmp3.add(PieEntry(scren6.toFloat()))
                NoOfEmp3.add(PieEntry(scren7.toFloat()))
                NoOfEmp3.add(PieEntry(scren8.toFloat()))
                NoOfEmp3.add(PieEntry(scren9.toFloat()))
                NoOfEmp3.add(PieEntry(scren10.toFloat()))


            }
            2 -> {
                NoOfEmp3.add(PieEntry(4f))


            }
            else -> {
                NoOfEmp3.add(PieEntry(4f))


            }
        }

        val dataSet3 = PieDataSet(NoOfEmp3, "Number Of Employees")


        val data3 = PieData(dataSet3)
        pieChart3.data = data3

//
        val MY_COLORS3 = intArrayOf(
            Color.rgb(255, 237, 214),
            Color.rgb(255, 227, 192),
            Color.rgb(255, 215, 164),
            Color.rgb(255, 202, 133),
            Color.rgb(255, 188, 102),
            Color.rgb(255, 237, 214),
            Color.rgb(255, 227, 192),
            Color.rgb(255, 215, 164),
            Color.rgb(255, 202, 133),
            Color.rgb(255, 188, 102)

        )
        val colors3 = ArrayList<Int>()

        for (d in MY_COLORS3) colors3.add(d)

        dataSet3.colors = colors3
        dataSet3.setDrawValues(false)

//        dataSet3.setColor(ColorTemplate.VORDIPLOM_COLORS.toString().toInt())


        pieChart3.animateXY(2000, 2000)
        pieChart3.legend.isEnabled = false
        pieChart3.description.isEnabled = false

        pieChart3.holeRadius = 65f


        pieChart3.centerText = (avgtime.toString() + " Second").toString()

        pieChart3.setCenterTextColor(rgb(162, 162, 162))


//        getLearning()

        if (entryTest == "entryTest") {
            getLearning() // for nust
        }

        if(streakQuiz=="yesStreakQuiz"){
            saveRecord()
            pasteStreakData()

        }

        Log.d("LearningAnalysisEntryTe",entryTest.toString())

    }

    private fun pasteStreakData() {

        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        val year = calendar.get(Calendar.YEAR)
        val date = calendar.get(Calendar.DATE)
        val month = calendar.get(Calendar.MONTH)
        val alldate="$date : $month : $year"
        val userDataRef: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("RecordsOFStreakTests")

        val userMap = HashMap<Any, Any>()
        userMap["uid"] = currentUserDataId
        userMap["trueValue"] = truevalues
        userMap["falseValue"] = falsevalue
        userMap["skipValue"] = skipvalue
        userMap["d_m_y"] = alldate
        userMap["dayOfYear"] = "$dayOfYear _ $year"

        val dayAndYear="$dayOfYear _ $year"
        userDataRef.child(currentUserDataId).child(dayAndYear).setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    // Toast.makeText(this,"Account has been created successfully:)",Toast.LENGTH_SHORT).show()

                } else {
                    val message = task.exception!!.toString()
                    Toast.makeText(this, "Something Went Wrong:(", Toast.LENGTH_LONG).show()
                    //  FirebaseAuth.getInstance().signOut()
                }
            }
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

        val userDataRef: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("RecordsOFStreakTests")
                .child(currentUserDataId).child("0_Streaks")
        val userMap = HashMap<Any, Any>()
        userMap["totalStreaks"] = steakCount+1

        userDataRef.setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    // Toast.makeText(this,"Account has been created successfully:)",Toast.LENGTH_SHORT).show()

                } else {

                    Toast.makeText(this, "Something Went Wrong:(", Toast.LENGTH_LONG).show()
                    //  FirebaseAuth.getInstance().signOut()
                }
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, Home::class.java))
    }

    //
    fun getLearning() {
        var count: Int = 1
        val child: String = FirebaseAuth.getInstance().currentUser!!.uid
//        Log.d("value of child var", "value of child is:" + child)

        val ref: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("LearningAnalysisEntryTest").child(child)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
//                Log.d("data", p0.toString())
                if (p0!!.exists()) {


                    if (count == 1) {
                        count++

                        p0.child("truevalue").value.toString()
                        p0.child("falsevalue").value.toString()
                        p0.child("skipvalue").value.toString()


                        trueSum = truevalues + (p0.child("truevalue").value.toString().toFloat())
                        falseSum = falsevalue + (p0.child("falsevalue").value.toString().toFloat())
                        skipSum = skipvalue + (p0.child("skipvalue").value.toString().toFloat())

                        sumLearning(trueSum, falseSum, skipSum)
//                        Log.d("value of trueSum","value of trueSum"+trueSum)

                    }
//                    for (h in p0.children) {

                } else {
                    sumLearning(trueSum, falseSum, skipSum)
                }


            }

        })


    }

    //
    fun sumLearning(trueSum: Float, falseSum: Float, skipSum: Float) {
        val userDataRef: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("LearningAnalysisEntryTest")

        val userMap = HashMap<Any, Any>()
        userMap["uid"] = currentUserDataId
        userMap["truevalue"] = trueSum
        userMap["falsevalue"] = falseSum
        userMap["skipvalue"] = skipSum

        userDataRef.child(currentUserDataId).setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    // Toast.makeText(this,"Account has been created successfully:)",Toast.LENGTH_SHORT).show()

                } else {
                    val message = task.exception!!.toString()
                    Toast.makeText(this, "Something Went Wrong:(", Toast.LENGTH_LONG).show()
                    //  FirebaseAuth.getInstance().signOut()
                }
            }
    }
}

