package com.example.mymad1.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.mymad1.R


class EnterPaymentGatewayActivity : AppCompatActivity() {

    private lateinit var btnInsertionPayment : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_payment_gateway)

        //Get Fee
        val ClassFee = intent.getStringExtra("ClassFee")
        btnInsertionPayment = findViewById(R.id.btnInsertPayment)

        btnInsertionPayment.setOnClickListener {
            val intent = Intent(this, PaymentInsertionActivity::class.java)
            intent.putExtra("ClassFee", ClassFee)
            startActivity(intent)
        }
    }
}