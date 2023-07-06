package com.example.mymad1.activities

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mymad1.R
import com.example.mymad1.models.EnrolModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class TeacherDetailsActivity : AppCompatActivity(){
    private lateinit var tvSubjectName: TextView
    private lateinit var tvTeacherName : TextView
    private lateinit var tvClassFee : TextView

    private lateinit var btnEnrolSubject : Button

    private lateinit var dbRef : DatabaseReference
    private lateinit var dbAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_subject_info)

        btnEnrolSubject = findViewById(R.id.btnEnrolSubject)

        initView()
        setValuesToViews()

        //check if student/teacher
        //get current user (student/ teacher)
        val currentUser = FirebaseAuth.getInstance().currentUser
        val teacherUser = FirebaseDatabase.getInstance().getReference("teachers")

        teacherUser
            .orderByChild("uid")
            .equalTo(currentUser?.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        //user is a teacher
                        btnEnrolSubject.visibility = View.GONE
                    } else {
                        //user is a student
                        isEnrolled()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@TeacherDetailsActivity, "Error ${error.message}", Toast.LENGTH_LONG).show()
                }
            })

        dbRef = FirebaseDatabase.getInstance().getReference("Enrollment")
    }

    private fun isEnrolled(){

        val currentUser = FirebaseAuth.getInstance().currentUser
        val enrolUser = FirebaseDatabase.getInstance().getReference("Enrollment")
        val subjectName = tvSubjectName.text.toString()
        val teacherName = tvTeacherName.text.toString()

        //check enrollment and handle btn 'enrol' accordingly
        enrolUser
            .orderByChild("stuId")
            .equalTo(currentUser?.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        //user is in enrollment collection
                        enrolUser
                            .orderByChild("subjectName")
                            .equalTo(subjectName)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        //user already enrolled under the subject
                                        enrolUser
                                            .orderByChild("teacherName")
                                            .equalTo(teacherName)
                                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                                override fun onDataChange(snapshot: DataSnapshot) {
                                                    if (snapshot.exists()) {
                                                        //user enrolled under the teacher
                                                        btnEnrolSubject.setOnClickListener {
                                                            btnEnrolSubject.isEnabled = false
                                                            Toast.makeText(this@TeacherDetailsActivity, "You are already enrolled!", Toast.LENGTH_LONG).show()
                                                        }
                                                    }
                                                    else {
                                                        // user not enrolled in this subject
                                                        btnEnrolSubject.setOnClickListener {
                                                            enrolStudent()
                                                        }
                                                    }
                                                }
                                                override fun onCancelled(error: DatabaseError) {
                                                    Toast.makeText(this@TeacherDetailsActivity, "Error ${error.message}", Toast.LENGTH_LONG).show()
                                                }
                                            })
                                    } else {
                                        // user not enrolled in this subject
                                        btnEnrolSubject.setOnClickListener {
                                            enrolStudent()
                                        }
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(this@TeacherDetailsActivity, "Error ${error.message}", Toast.LENGTH_LONG).show()
                                }
                            })
                    }
                    else{
                        // user not enrolled in this subject
                        btnEnrolSubject.setOnClickListener {
                            enrolStudent()
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@TeacherDetailsActivity, "Error ${error.message}", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun enrolStudent(){

        //get current userId
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        //get values
        val stuId = userId.toString()
        val subjectName = tvSubjectName.text.toString()
        val teacherName = tvTeacherName.text.toString()
        val classFee = tvClassFee.text.toString()



        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to enrol?")
            .setTitle("Enrollment")
            .setPositiveButton("Yes") { dialog, which ->
                //new enrollment
                val enrolCode = dbRef.push().key!!

                //pass data to variable
                val enrol = EnrolModel(enrolCode, stuId, subjectName, teacherName, classFee)

                //add data to database
                val mTask = dbRef.child(enrolCode).setValue(enrol)



                mTask.addOnSuccessListener {
                    showSignUpNotification()
                    Toast.makeText(this@TeacherDetailsActivity, "Please make payment to complete enrollment", Toast.LENGTH_LONG).show()

                    val intent = Intent(this@TeacherDetailsActivity, EnterPaymentGatewayActivity::class.java)
                    //Pass Class Fee
                    intent.putExtra("ClassFee", classFee)
                    startActivity(intent)

                    finish()
                    startActivity(intent)
                }.addOnFailureListener { error ->
                    Toast.makeText(this@TeacherDetailsActivity, "Enrollment Error ${error.message}", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("No") { dialog, which ->
                dialog.cancel()
            }

        val dialog = builder.create()
        dialog.show()
    }

    private fun initView() {
        tvSubjectName = findViewById(R.id.tvSubjectName)
        tvTeacherName = findViewById(R.id.tvTeacherName)
        tvClassFee = findViewById(R.id.tvClassFee)
    }

    private fun setValuesToViews(){
        tvSubjectName.text = intent.getStringExtra("subject")
        tvTeacherName.text = intent.getStringExtra("name")
        tvClassFee.text = intent.getStringExtra("fee")
    }

    //Function to display notification
    private fun showSignUpNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Notification Channel"
            val descriptionText = "Channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("my_channel_id", name, importance).apply {
                description = descriptionText
            }
            // register the notification channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // create a notification
        val notification = NotificationCompat.Builder(this, "my_channel_id")
            .setSmallIcon(R.drawable.sign_up_notificaion_icon)
            .setContentTitle("Welcome to Web-Brain !")
            .setContentText("Please note that the payment should be done to complete the enrollment process")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        // create an intent for the notification
        val intent = Intent(this, SignInActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        // add the intent to the notification
        notification.contentIntent = pendingIntent

        // show the notification
        val notificationManager = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            // ActivityCompat#requestPermissions
            return
        }
        notificationManager.notify(1, notification)
    }
}