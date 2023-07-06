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
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mymad1.R
//import com.example.mymad1.models.Students
import com.example.mymad1.models.Teachers
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class TeacherSignUpActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.teacher_sign_up)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()


        //Assign IDs to variables
        val signUpStdName : EditText = findViewById(R.id.signUpStdName)
        val signUpStdEmail : EditText = findViewById(R.id.signUpStdEmail)
        val signUpStdPhone : EditText = findViewById(R.id.signUpStdPhone)
        val signUpStdPassword : EditText = findViewById(R.id.signUpStdPassword)
        val signUpStdConfiPassword : EditText = findViewById(R.id.signUpStdConfiPassword)
        val siginUpStdPasswordLayout : TextInputLayout = findViewById(R.id.siginUpStdPasswordLayout)
        val siginUpStdConfiPasswordLayout : TextInputLayout = findViewById(R.id.siginUpStdConfiPasswordLayout)
        val signUpStdBtn : Button = findViewById(R.id.btnsignUpStd)
        val signUpStdProgressbar : ProgressBar = findViewById(R.id.signUpProgressbar)

        val signUpSubject : EditText = findViewById(R.id.teacherSubject)
        val signUpFee : EditText = findViewById(R.id.teacherSubjectFee)

        val signInText : TextView = findViewById(R.id.signInTextStd)

        signInText.setOnClickListener{
            val intent = Intent(this, TeacherSignInActivity::class.java)
            startActivity(intent)
        }

        signUpStdBtn.setOnClickListener {
            val name = signUpStdName.text.toString()
            val email = signUpStdEmail.text.toString()
            val phone = signUpStdPhone.text.toString()
            val password = signUpStdPassword.text.toString()
            val confirPassword = signUpStdConfiPassword.text.toString()

            val subject = signUpSubject.text.toString()
            val fee = signUpFee.text.toString()

            signUpStdProgressbar.visibility = View.VISIBLE
            siginUpStdPasswordLayout.isPasswordVisibilityToggleEnabled = true
            siginUpStdConfiPasswordLayout.isPasswordVisibilityToggleEnabled = true

            if(name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || fee.isEmpty()|| subject.isEmpty() || confirPassword.isEmpty()){

                if(name.isEmpty()){
                    signUpStdName.error = "Enter your name"
                }

                if(email.isEmpty()){
                    signUpStdEmail.error = "Enter your email"
                }

                if(phone.isEmpty()){
                    signUpStdPhone.error = "Enter your mobile number"
                }

                if(password.isEmpty()){
                    siginUpStdPasswordLayout.isPasswordVisibilityToggleEnabled = false
                    signUpStdPassword.error = "Enter your password"
                }

                if(confirPassword.isEmpty()){
                    siginUpStdConfiPasswordLayout.isPasswordVisibilityToggleEnabled = false
                    signUpStdConfiPassword.error = "Confirm your password"

                }

                if(subject.isEmpty()){
                    signUpStdPhone.error = "Enter your subject"
                }

                if(fee.isEmpty()){
                    signUpStdPhone.error = "Enter subject fee"
                }

                Toast.makeText(this, "Enter valid details", Toast.LENGTH_SHORT).show()
                signUpStdProgressbar.visibility = View.GONE

            }else if(!email.matches(emailPattern.toRegex())){
                signUpStdProgressbar.visibility = View.GONE
                signUpStdEmail.error = "Enter valid email address"
                Toast.makeText(this, "Enter valid email address", Toast.LENGTH_SHORT).show()

            }else if(phone.length != 10){
                signUpStdProgressbar.visibility = View.GONE
                signUpStdPhone.error = "Enter valid mobile number"
                Toast.makeText(this, "Enter valid mobile number", Toast.LENGTH_SHORT).show()

            }else if(password.length<6){
                siginUpStdPasswordLayout.isPasswordVisibilityToggleEnabled = false
                signUpStdProgressbar.visibility = View.GONE
                signUpStdPassword.error = "password must be more than 6 characters"
                Toast.makeText(this, "password must be more than 6 characters", Toast.LENGTH_SHORT).show()

            }else if(password != confirPassword){
                siginUpStdConfiPasswordLayout.isPasswordVisibilityToggleEnabled = false
                signUpStdProgressbar.visibility = View.GONE
                signUpStdConfiPassword.error = "password not matched"
                Toast.makeText(this, "password not matched", Toast.LENGTH_SHORT).show()

            }else{
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                    if(it.isSuccessful){
                        val databaseRef = database.reference.child("teachers").child(auth.currentUser!!.uid)
                        val teachers : Teachers = Teachers(name, email,phone, subject, fee, auth.currentUser!!.uid)

                        databaseRef.setValue(teachers).addOnCompleteListener{
                            if(it.isSuccessful){
                                val intent = Intent(this, TeacherSignInActivity::class.java)
                                startActivity(intent)
                                showSignUpNotification()
                            }else{
                                Toast.makeText(this, "Something went wrong, try again", Toast.LENGTH_SHORT).show()
                            }

                        }

                    }else{
                        Toast.makeText(this, "Something went wrong, try again", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


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
            .setContentText("You are a member of Web-Brain now")
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
            //    ActivityCompat#requestPermissions

            return
        }
        notificationManager.notify(1, notification)
    }
}