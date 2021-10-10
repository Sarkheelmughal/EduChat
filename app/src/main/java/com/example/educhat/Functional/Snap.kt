package com.example.educhat.Functional

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.educhat.Adapter.CustomeAdapter
import com.example.educhat.Adapter.OnChapterClick
import com.example.educhat.Model.Model
import com.example.educhat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class Snap : AppCompatActivity(), OnChapterClick {
    private val PERMISSION_CODE = 1000;
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null
    lateinit var ref: DatabaseReference
    lateinit var recycleView: RecyclerView
    lateinit var chapterlist: ArrayList<Model>
    private val user = FirebaseAuth.getInstance().currentUser
    var currentUserDataId = user!!.uid

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //button click
        val button = findViewById<CardView>(R.id.capture_btn)
        //val upload_button = findViewById<View>(R.id.upload_btn)

        callAll()

        button.setOnClickListener {
            //if system os is Marshmallow or Above, we need to request runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                    //permission was not enabled
                    val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    //show popup to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    openCamera()
                }
            }
            else{
                //system os is < marshmallow
                openCamera()
            }
        }
//        upload_button.setOnClickListener {
//            uploadImage()
//        }

        backFromSnap.setOnClickListener{onBackPressed()}
    }



    @RequiresApi(Build.VERSION_CODES.N)
    private fun uploadImage() {
        val image = findViewById<ImageView>(R.id.image_view)
        val progressDialog=ProgressDialog(this)
        progressDialog.setMessage("Uploading Query ...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")

        image_uri?.let {
            storageReference.putFile(it).addOnSuccessListener {
                image.setImageURI(null)
                Toast.makeText(applicationContext,"Successfully Uploaded! Answer will be Uploaded Soon.",Toast.LENGTH_SHORT).show()
                if (progressDialog.isShowing) progressDialog.dismiss()
                sendToDB(it,fileName)


            }.addOnFailureListener {

                if (progressDialog.isShowing) progressDialog.dismiss()
                Toast.makeText(applicationContext,"Failed",Toast.LENGTH_SHORT).show()


            }
        }

    }
    private fun callAll() {

        val recyclerView = findViewById(R.id.recyclerView) as RecyclerView
//        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.setLayoutManager(GridLayoutManager(this, 1))
//        recyclerView

        // val ProgressBar = findViewById<com.github.ybq.android.spinkit.SpinKitView>(R.id.progress_bar_rv)



        recycleView = findViewById(R.id.recyclerView)
        chapterlist = ArrayList<Model>()
        ref = FirebaseDatabase.getInstance().getReference("SnapQuery").child(currentUserDataId)


        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
//                Log.d("data", p0.toString())
                if (p0!!.exists()) {
                    chapterlist.clear()

                    // notPaid.visibility = View.GONE

                    for (h in p0.children) {
                        val chap = Model(
                            h.child("fileName").value.toString(),
                            h.child("date").value.toString(),
                            h.key!!
                        )
//                        Log.d("For loop", h.child("name").toString())
                        chapterlist.add(chap!!)

                    }
//                    Log.d("outside For loop", "outside from for loop")


                    val adapter =
                        CustomeAdapter(
                            applicationContext,
                            this@Snap,
                            chapterlist
                        )
                    recycleView.adapter = adapter


                }
            }

        })

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun sendToDB(taskSnapshot: UploadTask.TaskSnapshot, fileName: String) {
        val ref=FirebaseDatabase.getInstance().reference.child("SnapQuery")

        val formatter = SimpleDateFormat("dd_MM_yyyy", Locale.getDefault())
        val now = Date()
        val date = formatter.format(now)
val randomNum= (10 until 99999).random()

        val dataMap = HashMap<Any, Any>()
        //dataMap["taskSnapshot"] = taskSnapshot
        dataMap["fileName"] = "Question ID: $randomNum"
        dataMap["date"] = date.toString()
        dataMap["fullDate"] = fileName.toString()



        ref.child(currentUserDataId).child(randomNum.toString()).setValue(dataMap)

        recreate()


    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)

        Toast.makeText(this, "Click and wait few seconds!", Toast.LENGTH_SHORT).show()

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //called when user presses ALLOW or DENY from Permission Request Popup
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted
                    openCamera()
                }
                else{
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //called when image was captured from camera intent
        val image = findViewById<ImageView>(R.id.image_view)
        if (resultCode == Activity.RESULT_OK){
            //set image captured to image view
             image.setImageURI(image_uri)

            uploadImage()
        }
    }

    override fun onItemClick(list: Model, position: Int) {
        val intent=Intent(this,SnapAnswer::class.java)

        intent.putExtra("model",list)

        startActivity(intent)

    }

}
