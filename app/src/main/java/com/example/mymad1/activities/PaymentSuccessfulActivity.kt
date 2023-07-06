package com.example.mymad1.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.mymad1.R

class PaymentSuccessfulActivity : AppCompatActivity() {


    private lateinit var btnPaymentHome: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_successful)

        /**When Press home button in Successful page go back MainActivity*/
        btnPaymentHome = findViewById(R.id.btnHome)

        btnPaymentHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

    }
}

