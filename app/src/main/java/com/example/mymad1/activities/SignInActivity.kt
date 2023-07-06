package com.example.mymad1.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.mymad1.R

import com.example.mymad1.databinding.ActivitySignInBinding
import com.google.android.material.textfield.TextInputLayout

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignInActivity : AppCompatActivity() {

    //Variable to get Auth reference
    private lateinit var auth : FirebaseAuth
    private val emailPattern = "[a-z0-9._-]+@[a-z]+\\.+[a-z]+"       //Assign Email validation pattern


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        //Get auth instance
        auth = FirebaseAuth.getInstance()

        //Assign Edit txt Ids to variables
        val signInEmail : EditText = findViewById(R.id.signInStdEmail)
        val signInPassword : EditText = findViewById(R.id.signInStdPassword)
        val signInPasswordLayout : TextInputLayout = findViewById(R.id.siginInStdPasswordLayout)
        val signInBtn : Button = findViewById(R.id.btnsignInStd)
        val signInProgressbar : ProgressBar = findViewById(R.id.signInProgressbar)

        val signUpText : TextView = findViewById(R.id.signUpTextStd)

        //Change to SignUp activity
        signUpText.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        signInBtn.setOnClickListener{
            signInProgressbar.visibility = View.VISIBLE
            signInPasswordLayout.isPasswordVisibilityToggleEnabled = true

            val stdEmail = signInEmail.text.toString()
            val stdPassword = signInPassword.text.toString()

            //Check whether input fields are empty or not
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

                //Check Email validation
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