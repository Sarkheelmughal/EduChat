package com.example.educhat.Functional

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.educhat.Model.McqsModel
import com.example.educhat.R
import com.example.educhat.databinding.ActivityStreakQuizBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StreakQuiz : AppCompatActivity(), View.OnTouchListener {
    lateinit var binding: ActivityStreakQuizBinding
    var dY = 0f
    var lastAction = 0
    var stoptimer = object : CountDownTimer(0, 0) {
        override fun onFinish() {}
        override fun onTick(p0: Long) {}
    }
    var time = 0
    var pp: Long = 0
    var hh: Long = 0
    var mm: Long = 0
    val fulllistofqus = ArrayList<McqsModel>()
    var random: Int? = null
    var getTotalmcqs: Int? = null
    var btnCounter = 1
    lateinit var button_change2: LinearLayout
    lateinit var button_change3: LinearLayout
    lateinit var button_change4: LinearLayout
    lateinit var button_change1: LinearLayout
    var nextmcq = 0
    var b: Int = 1
    var screen1 = 0
    var screen2 = 0
    var screen3 = 0
    var screen4 = 0
    var screen5 = 0
    var screen6 = 0
    var screen7 = 0
    var screen8 = 0
    var screen9 = 0
    var screen10 = 0
    var trueOpt: Float = 0f
    var falseOpt: Float = 0f
    var skipOpt: Float = 10f
    var sumTime = 0

    var firstFalse = 0f
    var secondFalse = 0f
    var thirdFalse = 0f
    var fourthFalse = 0f
    var firstTrue = 0f
    var secondTrue = 0f
    var thirdTrue = 0f
    var fourthTrue = 0f
    var a: Int = -1

    //---------------
    lateinit var btnAa: RadioButton
    lateinit var btnBb: RadioButton
    lateinit var btnCc: RadioButton
    lateinit var btnDd: RadioButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_streak_quiz)
        binding.moveLL.setOnTouchListener(this)
       // binding.moveLL.performClick()

        btnAa = binding.btnAa
        btnBb = binding.btnBb
        btnCc = binding.btnCc
        btnDd = binding.btnDd

        getTotalmcqs = 520
        binding.mcqsBackIV.setOnClickListener { onBackPressed() }

        val showMcqs = 10

        Log.d("totalSelectedMcqs", showMcqs.toString())

        stoptimer = object : CountDownTimer(1000000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                binding.setTimerOpt.setText(time.toString())
                time++
//                Log.d("timer log", "log for timer" + time)

            }

            override fun onFinish() {
                //  startActivity(Intent(this@Options, ResultActivity::class.java))
//                Log.d("timer log", "log for setText")
            }
        }.start()


        fetchPhysicsMcqs()

        binding.sampleTextBtn.setOnClickListener {
            if (btnCounter == 1) {
                btnCounter++
                Log.d("ifcondit ", "testingNET")

                random?.let { it1 -> animNotifyRandom(it1) }
            } else //if (btnCounter == 2)
            {
                binding.moveLL.visibility = View.GONE
                binding.sampleTextBtn.text = "Check"
//                    btnCounter--
                time = 0
                stoptimer.start()
                netNext()
            }
        }
        button_change1 = binding.linearOne
        button_change2 = binding.linearTwo
        button_change3 = binding.linearThree
        button_change4 = binding.linearFour

        button_change1.setOnClickListener {
            uncheckSelected(1)
        }
        button_change2.setOnClickListener {
            uncheckSelected(2)
        }

        button_change3.setOnClickListener {
            uncheckSelected(3)
        }
        button_change4.setOnClickListener {
            uncheckSelected(4)
        }


        btnAa.setOnClickListener {
            uncheckSelected(1)
        }
        btnBb.setOnClickListener {
            uncheckSelected(2)
        }

        btnCc.setOnClickListener {
            uncheckSelected(3)
        }
        btnDd.setOnClickListener {
            uncheckSelected(4)
        }
    }

    private fun fetchPhysicsMcqs() {
        binding.fetchingMcqsTv.text = "Fetching Physics Mcqs..."
        val ref =
            FirebaseDatabase.getInstance().getReference("contents").child("topic_questions")
                .child("class9th").child("subjects").child("physics")

        Log.d("totalChildren ", "touched")
        ref.addValueEventListener(object : ValueEventListener {
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
                                Log.d("totalChildrenCountQue", m.child("question").value.toString())


                            }
                        }
                    }
                    //    Log.d("totalChildrenCountsub", subject.toString())
                    // random = (0 until (getTotalmcqs!!)).random()
                    // textSetter(random!!)

                    fetchChemistryMcqs()
                }


            }

        })

    }

    private fun fetchChemistryMcqs() {

        binding.fetchingMcqsTv.text = "Fetching Chemistry Mcqs..."
        val ref =
            FirebaseDatabase.getInstance().getReference("contents").child("topic_questions")
                .child("class9th").child("subjects").child("chemistry")

        Log.d("totalChildren ", "touched")
        ref.addValueEventListener(object : ValueEventListener {
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
                                Log.d("totalChildrenCountQue", m.child("question").value.toString())


                            }
                        }
                    }
                    //    Log.d("totalChildrenCountsub", subject.toString())
                    // random = (0 until (getTotalmcqs!!)).random()

                    fetchBiologyMcqs()
                }


            }

        })

    }

    private fun fetchBiologyMcqs() {

        binding.fetchingMcqsTv.text = "Fetching Biology Mcqs..."
        val ref =
            FirebaseDatabase.getInstance().getReference("contents").child("topic_questions")
                .child("class9th").child("subjects").child("biology")

        Log.d("totalChildren ", "touched")
        ref.addValueEventListener(object : ValueEventListener {
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
                                Log.d("totalChildrenCountQue", m.child("question").value.toString())
                                binding.fetchingMcqsTv.visibility=View.GONE

                            }
                        }
                    }
                    //    Log.d("totalChildrenCountsub", subject.toString())
                    random = (0 until (getTotalmcqs!!)).random()
                    binding.progressBarSQ.visibility=View.GONE
                    textSetter(random!!)

                }


            }

        })

    }


    override fun onTouch(view: View, event: MotionEvent): Boolean {


        val moveLL = binding.moveLL

        when (event!!.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                moveLL.alpha = 0.5f
//                dX = view.x - event.rawX
                dY = view.y - event!!.rawY
                lastAction = MotionEvent.ACTION_DOWN

            }
            MotionEvent.ACTION_MOVE -> {
                moveLL.alpha = 0.5f
                view.y = event.rawY + dY
//                view.x = event.rawX + dX
                lastAction = MotionEvent.ACTION_MOVE
            }
            MotionEvent.ACTION_UP -> if (lastAction == MotionEvent.ACTION_DOWN) Toast.makeText(
                this@StreakQuiz,
                "Clicked!",
                Toast.LENGTH_SHORT
            ).show()
            else -> return false
        }
        if (event.action == MotionEvent.ACTION_UP) {
            moveLL.alpha = 1f
        }

        return true
    }

    private fun tickGone() {
        binding.tickOneIV.visibility = View.GONE
        binding.tickTwoIV.visibility = View.GONE
        binding.tickthreeIV.visibility = View.GONE
        binding.tickFourIV.visibility = View.GONE
    }

    private fun textSetter(random: Int) {

        binding.sampleQuestionText.setText(fulllistofqus[random].question)
        btnAa.setText(fulllistofqus[random].optionA)
        btnBb.setText(fulllistofqus[random].optionB)
        btnCc.setText(fulllistofqus[random].optionC)
        btnDd.setText(fulllistofqus[random].optionD)
        fulllistofqus[random].correctOption
    }

    private fun uncheckSelected(mcqNum: Int) {
        when (mcqNum) {

            1 -> {

                btnAa.isChecked = true
                btnBb.isChecked = false
                btnCc.isChecked = false
                btnDd.isChecked = false

//                btnBb.isClickable = false
//                btnCc.isClickable = false
//                btnDd.isClickable = false
//
//                binding.linearOne.isClickable = false
//                binding.linearTwo.isClickable = false
//                binding.linearThree.isClickable = false
//                binding.linearFour.isClickable = false

            }
            2 -> {
                btnBb.isChecked = true
                btnAa.isChecked = false
                btnCc.isChecked = false
                btnDd.isChecked = false

//                btnAa.isClickable = false
//                btnCc.isClickable = false
//                btnDd.isClickable = false
//
//                binding.linearOne.isClickable = false
//                binding.linearTwo.isClickable = false
//                binding.linearThree.isClickable = false
//                binding.linearFour.isClickable = false
            }
            3 -> {
                btnCc.isChecked = true

                btnAa.isChecked = false
                btnBb.isChecked = false
                btnDd.isChecked = false

//                btnAa.isClickable = false
//                btnBb.isClickable = false
//                btnDd.isClickable = false
//
//                binding.linearOne.isClickable = false
//                binding.linearTwo.isClickable = false
//                binding.linearThree.isClickable = false
//                binding.linearFour.isClickable = false
            }
            4 -> {

                btnDd.isChecked = true

                btnAa.isChecked = false
                btnBb.isChecked = false
                btnCc.isChecked = false
//                btnAa.isClickable = false
//                btnBb.isClickable = false
//                btnCc.isClickable = false
//
//                binding.linearOne.isClickable = false
//                binding.linearTwo.isClickable = false
//                binding.linearThree.isClickable = false
//                binding.linearFour.isClickable = false
            }
        }
    }

    private fun netNext() {
        tickGone()
        val showMcqs = 10

        Log.d("netNext", "testingNET")
        btnCounter = 1


//        moveLL.visibility=View.VISIBLE
        val container = ArrayList<Int>()
        Log.d("valueofrandom", random.toString())

        nextmcq++
//        if (random!! < fulllistofqus.size) {
        if (nextmcq < showMcqs) {
            b++
            binding.ScreenNumber.setText("Question " + b.toString() + " / " + showMcqs)

            random = (0..this.getTotalmcqs!!).random()

            if (random!! in container) {
                random = (0..this.getTotalmcqs!!).random()
                textSetter(random!!)
//                    calculateResult(random!!)
                uncheckRadio()

            } else {
                container.add(random!!)
                textSetter(random!!)
//                    calculateResult(random!!)
                uncheckRadio()

            }

        } else {

            moveToResult()
        }
//


    }

    private fun animNotifyRandom(random: Int) {

        Log.d("animNotifyRandom", "testingNET")
        val notifyTV = findViewById<TextView>(R.id.notifyTV)
        val slide = Slide()
        slide.slideEdge = Gravity.START
        stoptimer.cancel()

        disableChecks()

        binding.sampleTextBtn.text = "Next Question"
//        TransitionManager.beginDelayedTransition(mainSV, slide)
//        moveLL.setVisibility(View.VISIBLE)
//        notifyTV.setText("True")

        if (!btnAa.isChecked && !btnBb.isChecked && !btnCc.isChecked && !btnDd.isChecked) {
            TransitionManager.beginDelayedTransition(binding.mainSV, slide)
            binding.moveLL.setBackgroundResource(R.color.red)
            binding.moveLL.visibility = View.VISIBLE
            notifyTV.text = "No Option selected"
        }

        if (btnAa.isChecked && btnAa.text != fulllistofqus[random].correctOption) {
            TransitionManager.beginDelayedTransition(binding.mainSV, slide)
            binding.moveLL. visibility = View.VISIBLE
            binding.moveLL.setBackgroundResource(R.color.red)
            notifyTV.text = "Oops! That's not correct."
        } else if (btnBb.isChecked && btnBb.text != fulllistofqus[random].correctOption) {
            TransitionManager.beginDelayedTransition(binding.mainSV, slide)
            binding.moveLL.visibility = View.VISIBLE
            binding.moveLL.setBackgroundResource(R.color.red)
            notifyTV.text = "Oops! That's not correct."
        } else if (btnCc.isChecked && btnCc.text != fulllistofqus[random].correctOption) {
            TransitionManager.beginDelayedTransition(binding.mainSV, slide)
            binding.moveLL.visibility = View.VISIBLE
            binding.moveLL.setBackgroundResource(R.color.red)

            notifyTV.text = "Oops! That's not correct."
        } else if (btnDd.isChecked && btnDd.text != fulllistofqus[random].correctOption) {
            TransitionManager.beginDelayedTransition(binding.mainSV, slide)
            binding.moveLL.setBackgroundResource(R.color.red)
            binding.moveLL.visibility = View.VISIBLE
            notifyTV.text = "Oops! That's not correct."
        }


        //

        if (btnAa.isChecked && btnAa.text == fulllistofqus[random].correctOption) {
            TransitionManager.beginDelayedTransition(binding.mainSV, slide)
            binding.moveLL.visibility = View.VISIBLE
            binding.moveLL.setBackgroundResource(R.color.green)

            notifyTV.setText("Great work!")
        } else if (btnBb.isChecked && btnBb.text == fulllistofqus[random].correctOption) {
            TransitionManager.beginDelayedTransition(binding.mainSV, slide)
            binding.moveLL.setBackgroundResource(R.color.green)

            binding.moveLL.visibility = View.VISIBLE
            notifyTV.text = "Great work!"
        } else if (btnCc.isChecked && btnCc.text == fulllistofqus[random].correctOption) {
            TransitionManager.beginDelayedTransition(binding.mainSV, slide)
            binding.moveLL.setBackgroundResource(R.color.green)

            binding.moveLL.visibility = View.VISIBLE
            notifyTV.text = "Great work!"
        } else if (btnDd.isChecked && btnDd.text == fulllistofqus[random].correctOption) {
            TransitionManager.beginDelayedTransition(binding.mainSV, slide)
            binding.moveLL.setBackgroundResource(R.color.green)

            binding.moveLL.visibility = View.VISIBLE
            notifyTV.text = "Great work!"
        }


        when {
            btnAa.text == fulllistofqus[random].correctOption -> {
                binding.tickOneIV.visibility = View.VISIBLE
            }
            btnBb.text == fulllistofqus[random].correctOption -> {
                binding.tickTwoIV.visibility = View.VISIBLE

            }
            btnCc.text == fulllistofqus[random].correctOption -> {
                binding.tickthreeIV.visibility = View.VISIBLE

            }
            btnDd.text == fulllistofqus[random].correctOption -> {
                binding.tickFourIV.visibility = View.VISIBLE

            }
        }

        calculateResult(random)


    }

    private fun disableChecks() {

        btnBb.isClickable = false
        btnAa.isClickable = false
        btnCc.isClickable = false
        btnDd.isClickable = false

        binding.linearOne.isClickable = false
        binding.linearTwo.isClickable = false
        binding.linearThree.isClickable = false
        binding.linearFour.isClickable = false

    }

    private fun uncheckRadio() {

        btnAa.isChecked = false;
        btnBb.isChecked = false;
        btnCc.isChecked = false;
        btnDd.isChecked = false;

        btnBb.isClickable = true
        btnCc.isClickable = true
        btnDd.isClickable = true
        btnAa.isClickable = true

        binding.linearOne.isClickable = true
        binding.linearTwo.isClickable = true
        binding.linearThree.isClickable = true
        binding.linearFour.isClickable = true
    }

    private fun moveToResult() {

        stoptimer.cancel()
        val intent = Intent(this, ResultActivity::class.java)
        screen10 = time
//            Log.d("value of a","value of a is :4 and value of time is :"+time)
        var mock = intent.getIntExtra("mock", 0)



        intent.putExtra("TrueValues", trueOpt)
        intent.putExtra("FalseValues", falseOpt)
        intent.putExtra("SkipValues", skipOpt)
        intent.putExtra("b", b)
        intent.putExtra("sumTime", sumTime)

        intent.putExtra("screen1", screen1)
        intent.putExtra("screen2", screen2)
        intent.putExtra("screen3", screen3)
        intent.putExtra("screen4", screen4)
        intent.putExtra("screen5", screen5)
        intent.putExtra("screen6", screen6)
        intent.putExtra("screen7", screen7)
        intent.putExtra("screen8", screen8)
        intent.putExtra("screen9", screen9)
        intent.putExtra("screen10", screen10)
        intent.putExtra("streakQuiz", "yesStreakQuiz")

        startActivity(intent)

        stoptimer.cancel()
    }

    private fun calculateResult(random: Int) {


        sumTime += time
//        Log.d("testing of sum","the sum of timer is :"+sumTime)

//    skipOpt=
        if (btnAa.isChecked && btnAa.text != fulllistofqus[random].correctOption) {
            firstFalse++
        } else if (btnBb.isChecked && btnBb.text != fulllistofqus[random].correctOption) {
            secondFalse++
        } else if (btnCc.isChecked && btnCc.text != fulllistofqus[random].correctOption) {
            thirdFalse++
        } else if (btnDd.isChecked && btnDd.text != fulllistofqus[random].correctOption) {
            fourthFalse++
        }

        if (btnAa.isChecked && btnAa.text == fulllistofqus[random].correctOption) {
            firstTrue++
        } else if (btnBb.isChecked && btnBb.text == fulllistofqus[random].correctOption) {
            secondTrue++
        } else if (btnCc.isChecked && btnCc.text == fulllistofqus[random].correctOption) {
            thirdTrue++
        } else if (btnDd.isChecked && btnDd.text == fulllistofqus[random].correctOption) {
            fourthTrue++
        }


        trueOpt = firstTrue + secondTrue + thirdTrue + fourthTrue

        falseOpt = firstFalse + secondFalse + thirdFalse + fourthFalse

        Log.d("trueFalse true", trueOpt.toString())
        Log.d("trueFalse false", falseOpt.toString())
        val showMcqs = 10

        skipOpt = showMcqs.toFloat() - (falseOpt + trueOpt)


//        Log.d("value of a","value of a ouside the if is: "+a)
        a++


        if (a < fulllistofqus.size) {


            when (a) {
                1 -> {
                    screen1 = time
                }
                2 -> {
                    screen2 = time
                }
                3 -> {
                    screen3 = time
                }
                4 -> {
                    screen4 = time
                }
                5 -> {
                    screen5 = time
                }
                6 -> {
                    screen6 = time
                }
                7 -> {
                    screen7 = time
                }
                8 -> {
                    screen8 = time
                }
                9 -> {
                    screen9 = time
                }
            }


            stoptimer.cancel()


        }
    }

}