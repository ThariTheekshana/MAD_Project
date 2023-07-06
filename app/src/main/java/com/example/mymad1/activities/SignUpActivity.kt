package com.example.mymad1.activities

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mymad1.R
import com.example.mymad1.databinding.ActivitySignUpBinding
import com.example.mymad1.models.Students
import com.google.android.material.textfield.TextInputLayout

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class SignUpActivity : AppCompatActivity() {

    //DB reference for variables
    private lateinit var auth : FirebaseAuth
    private lateinit var database: FirebaseDatabase

    //Email validation
    private val emailPattern = "[a-z0-9._-]+@[a-z]+\\.+[a-z]+"
    //Mobile number pattern for validation
    private val phonePattern = "^\\d{10}$"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //Get instances from FirebaseAuth and Realtime database
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

        val signInText : TextView = findViewById(R.id.signInTextStd)

        //Change activity when click text
        signInText.setOnClickListener{
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        signUpStdBtn.setOnClickListener {
            val stdName = signUpStdName.text.toString()
            val stdEmail = signUpStdEmail.text.toString()
            val stdPhone = signUpStdPhone.text.toString()
            val stdPassword = signUpStdPassword.text.toString()
            val stdConfirPassword = signUpStdConfiPassword.text.toString()

            signUpStdProgressbar.visibility = View.VISIBLE
            siginUpStdPasswordLayout.isPasswordVisibilityToggleEnabled = true
            siginUpStdConfiPasswordLayout.isPasswordVisibilityToggleEnabled = true

            //Check whether input fields are empty or not
            if(stdName.isEmpty() || stdEmail.isEmpty() || stdPhone.isEmpty() || stdPassword.isEmpty() || stdConfirPassword.isEmpty()){

                if(stdName.isEmpty()){
                    signUpStdName.error = "Enter your name"
                }

                if(stdEmail.isEmpty()){
                    signUpStdEmail.error = "Enter your email"
                }

                if(stdPhone.isEmpty()){
                    signUpStdPhone.error = "Enter your mobile number"
                }

                if(stdPassword.isEmpty()){
                    siginUpStdPasswordLayout.isPasswordVisibilityToggleEnabled = false
                    signUpStdPassword.error = "Enter your password"
                }

                if(stdConfirPassword.isEmpty()){
                    siginUpStdConfiPasswordLayout.isPasswordVisibilityToggleEnabled = false
                    signUpStdConfiPassword.error = "Confirm your password"

                }
                Toast.makeText(this, "Enter valid details", Toast.LENGTH_SHORT).show()
                signUpStdProgressbar.visibility = View.GONE

                //Check email valid or not
            }else if(!stdEmail.matches(emailPattern.toRegex())){
                signUpStdProgressbar.visibility = View.GONE
                signUpStdEmail.error = "Enter valid email address"
                Toast.makeText(this, "Enter valid email address", Toast.LENGTH_SHORT).show()

                //Check mobile number valid or not
            }else if(stdPhone.length != 10 || !stdPhone.matches(phonePattern.toRegex())){
                signUpStdProgressbar.visibility = View.GONE
                signUpStdPhone.error = "Enter valid mobile number"
                Toast.makeText(this, "Enter valid mobile number", Toast.LENGTH_SHORT).show()

                //Make min password length as 6
            }else if(stdPassword.length<6){
                siginUpStdPasswordLayout.isPasswordVisibilityToggleEnabled = false
                signUpStdProgressbar.visibility = View.GONE
                signUpStdPassword.error = "password must be more than 6 characters"
                Toast.makeText(this, "password must be more than 6 characters", Toast.LENGTH_SHORT).show()

            }else if(stdPassword != stdConfirPassword){
                siginUpStdConfiPasswordLayout.isPasswordVisibilityToggleEnabled = false
                signUpStdProgressbar.visibility = View.GONE
                signUpStdConfiPassword.error = "password not matched"
                Toast.makeText(this, "password not matched", Toast.LENGTH_SHORT).show()

            }else{
                auth.createUserWithEmailAndPassword(stdEmail, stdPassword).addOnCompleteListener{
                    if(it.isSuccessful){

                        //Create database collection students if details are correct
                        val databaseRef = database.reference.child("students").child(auth.currentUser!!.uid)
                        val students : Students = Students(stdName, stdEmail,stdPhone, auth.currentUser!!.uid)

                        databaseRef.setValue(students).addOnCompleteListener{
                            if(it.isSuccessful){
                                val intent = Intent(this, SignInActivity::class.java)
                                startActivity(intent)

                                //Display Notification
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