package com.example.educhat.Functional


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.educhat.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.learning_analysis.*


class Learning_analysis : AppCompatActivity() {

    var trueper: Float = 0f
    var falski: Float = 0f
    var skiped: Float = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.learning_analysis)



        getData()

        backbtn.setOnClickListener() {
            startActivity(Intent(this, Home::class.java))
        }
        val dialogBuilder = AlertDialog.Builder(this)
        val alert = dialogBuilder.create()


    }




    fun getData() {


        val uid: String = FirebaseAuth.getInstance().currentUser!!.uid
//        Log.d("value of child var", "value of child is:" + child)

        val refForGameRoom: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("ProgressReport")
                .child(uid)
        refForGameRoom.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
//                Log.d("data", p0.toString())
                if (p0!!.exists()) {

                    trueper = p0.child("trueMcq").value.toString().toFloat()
                    falski = p0.child("falseMcq").value.toString().toFloat()
                    skiped = p0.child("skipMcq").value.toString().toFloat()

                    val clear = "false"
                    plotData(clear)

                } else
                {
                    Toast.makeText(this@Learning_analysis, "No data available!", Toast.LENGTH_SHORT)
                        .show()
                    trueper= 0f
                    falski= 0f
                    skiped= 0f

                    val clear = "true"
                    plotData(clear)
                }
            }

        })
    }

    fun plotData(clear: String) {
        progBar.visibility = View.GONE

        val pieChart = findViewById<PieChart>(R.id.piechartLearning1)
        val corectLearing = findViewById<TextView>(R.id.corectLearing)
        val incorrectLearning = findViewById<TextView>(R.id.incorrectLearning)
        val unansweredLearning = findViewById<TextView>(R.id.unansweredLearning)


        corectLearing.setText(trueper.toInt().toString() + " Correct")
        incorrectLearning.setText(falski.toInt().toString() + " Incorrect")
        unansweredLearning.setText(skiped.toInt().toString() + " Unanswered")


        val NoOfEmp1 = ArrayList<PieEntry>()


        NoOfEmp1.add(PieEntry(trueper))
        NoOfEmp1.add(PieEntry(falski))
        NoOfEmp1.add(PieEntry(skiped))

        val dataSet = PieDataSet(NoOfEmp1, "Number Of Employees")


        val data1 = PieData(dataSet)
        pieChart.data = data1


        val MY_COLORS = intArrayOf(
            Color.rgb(57, 255, 33),
            Color.rgb(255, 0, 0),
            Color.rgb(255, 156, 0)
        )
        val colors = ArrayList<Int>()

        for (b in MY_COLORS) colors.add(b)

        dataSet.colors = colors
        dataSet.setDrawValues(false)


        pieChart.animateXY(2000, 2000)
        pieChart.legend.isEnabled = false
        pieChart.description.isEnabled = false

        pieChart.holeRadius = 65f


        pieChart.centerText =
            (trueper.toInt().toString() + "/" + (trueper + falski + skiped).toInt()
                .toString()).toString()

        pieChart.setCenterTextColor(Color.rgb(162, 162, 162))

        //-------------------------------piechart2----------------------------------------------------


        val pieChart1 = findViewById<PieChart>(R.id.piechartLearning2)

        val NoOfEmp2 = ArrayList<PieEntry>()

        val percentage = (trueper / (falski + skiped + trueper)) * 100

        NoOfEmp2.add(PieEntry(trueper))
        NoOfEmp2.add(PieEntry(falski + skiped))

        val dataSet1 = PieDataSet(NoOfEmp2, "Number Of Employees")


        val data2 = PieData(dataSet1)
        pieChart1.data = data2


        val MY_COLORS1 = intArrayOf(
            Color.rgb(57, 255, 33),
            Color.rgb(255, 0, 0)
        )
        val colors1 = ArrayList<Int>()

        for (a in MY_COLORS1) colors1.add(a)

        dataSet1.colors = colors1
        dataSet1.setDrawValues(false)


        pieChart1.animateXY(2000, 2000)
        pieChart1.legend.isEnabled = false
        pieChart1.description.isEnabled = false

        pieChart1.holeRadius = 65f


        pieChart1.centerText = (percentage.toInt().toString() + "%").toString()

        pieChart1.setCenterTextColor(Color.rgb(162, 162, 162))

        //--------------------------------------pie3-----------------------------------------------


        val pieChart3 = findViewById<PieChart>(R.id.piechartLearning3)

        val NoOfEmp3 = ArrayList<PieEntry>()
        var avg = 0f
        avg = when (clear) {
            "true" -> {
                0f

            }
            "falseP" -> {
                4f
            }
            "falseC" -> {
                5f
            }
            "falseB" -> {
                7f
            }
            else -> {

                8f
            }
        }
        NoOfEmp3.add(PieEntry(1f))


        val dataSet3 = PieDataSet(NoOfEmp3, "Number Of Employees")


        val data3 = PieData(dataSet3)
        pieChart3.data = data3


        val MY_COLORS3 = intArrayOf(
            Color.rgb(253, 114, 246)

        )
        val colors3 = ArrayList<Int>()

        for (f in MY_COLORS3) colors3.add(f)

        dataSet3.colors = colors3
        dataSet3.setDrawValues(false)


        pieChart3.animateXY(2000, 2000)
        pieChart3.legend.isEnabled = false
        pieChart3.description.isEnabled = false

        pieChart3.holeRadius = 65f


        pieChart3.centerText = (avg.toInt().toString() + " Second").toString()

        pieChart3.setCenterTextColor(Color.rgb(162, 162, 162))


    }

}
