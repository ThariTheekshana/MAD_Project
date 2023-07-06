package com.example.mymad1.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mymad1.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth


class TeacherSignInActivity : AppCompatActivity() {


    private lateinit var auth : FirebaseAuth
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.teacher_sign_in)

        auth = FirebaseAuth.getInstance()


        val signInEmail : EditText = findViewById(R.id.signInStdEmail)
        val signInPassword : EditText = findViewById(R.id.signInStdPassword)
        val signInPasswordLayout : TextInputLayout = findViewById(R.id.siginInStdPasswordLayout)
        val signInBtn : Button = findViewById(R.id.btnsignInStd)
        val signInProgressbar : ProgressBar = findViewById(R.id.signInProgressbar)

        val signUpText : TextView = findViewById(R.id.signUpTextStd)

        signUpText.setOnClickListener{
            val intent = Intent(this, TeacherSignUpActivity::class.java)
            startActivity(intent)
        }

        signInBtn.setOnClickListener{
            signInProgressbar.visibility = View.VISIBLE
            signInPasswordLayout.isPasswordVisibilityToggleEnabled = true

            val stdEmail = signInEmail.text.toString()
            val stdPassword = signInPassword.text.toString()

            if(stdEmail.isEmpty() || stdPassword.isEmpty()){
                if(stdEmail.isEmpty()){
                    signInEmail.error = "Enter email address"
                }
                if(stdPassword.isEmpty()){
                    signInEmail.error = "Enter password"
                    signInPasswordLayout.isPasswordVisibilityToggleEnabled = false
                }

                signInProgressbar.visibility = View.GONE
                Toast.makeText(this, "Enter valid details", Toast.LENGTH_SHORT).show()

            }else if(!stdEmail.matches(emailPattern.toRegex())){
                signInProgressbar.visibility = View.GONE
                signInEmail.error = "Enter valid email address"
                Toast.makeText(this, "Enter valid email address", Toast.LENGTH_SHORT).show()

            }else if(stdPassword.length<6){
                signInPasswordLayout.isPasswordVisibilityToggleEnabled = false
                signInProgressbar.visibility = View.GONE
                signInPassword.error = "password must be more than 6 characters"
                Toast.makeText(this, "password must be more than 6 characters", Toast.LENGTH_SHORT).show()

            }else{
                auth.signInWithEmailAndPassword(stdEmail, stdPassword).addOnCompleteListener{
                    if(it.isSuccessful){
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, "Something went wrong, please try again!", Toast.LENGTH_SHORT).show()
                        signInProgressbar.visibility = View.GONE
                    }
                }
            }
        }


    }
}