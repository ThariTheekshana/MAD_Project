package com.example.mymad1.activities

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mymad1.R
import com.example.mymad1.models.PaymentModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class PaymentInsertionActivity : AppCompatActivity() {
    private lateinit var etCardNumber: EditText
    private lateinit var etCardHolderName: EditText
    private lateinit var etExpireMonth: EditText
    private lateinit var etExpireYear: EditText
    private lateinit var etCvv: EditText
    private lateinit var btnSavePaymentData: Button


    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_insertion)

        etCardNumber = findViewById(R.id.etCardNumber)
        etCardHolderName = findViewById(R.id.etCardHolderName)
        etExpireMonth = findViewById(R.id.etExpireMonth)
        etExpireYear = findViewById(R.id.etExpireYear)
        etCvv = findViewById(R.id.etCvv)
        btnSavePaymentData = findViewById(R.id.btnSavePayment)
        dbRef = FirebaseDatabase.getInstance().getReference("payments")

        btnSavePaymentData.setOnClickListener {
            savePaymentData()
        }
    }

    private fun savePaymentData() {
// Getting values
        val cardNumber = etCardNumber.text.toString().trim()
        val cardHolderName = etCardHolderName.text.toString().trim()
        val expireMonth = etExpireMonth.text.toString().trim()
        val expireYear = etExpireYear.text.toString().trim()
        val cvv = etCvv.text.toString().trim()

// Perform validation
        var isValid = true

        if (cardNumber.isEmpty()) {
            etCardNumber.error = "Please enter Card Number"
            isValid = false
        } else {
            val formattedCardNumber = cardNumber.replace("\\s".toRegex(), "").chunked(4).joinToString(" ")
            etCardNumber.setText(formattedCardNumber)

            if (!formattedCardNumber.matches("^(\\d{4}\\s){3}\\d{4}$".toRegex())) {
                etCardNumber.error = "Invalid Card Number"
                isValid = false
            }
        }



        if (cardHolderName.isEmpty()) {
            etCardHolderName.error = "Please enter Card Holder Name"
            isValid = false
        } else if (!cardHolderName.matches("[a-zA-Z\\.\\s]+".toRegex())) {
            etCardHolderName.error = "Invalid Card Holder Name"
            isValid = false
        }

        if (expireMonth.isEmpty()) {
            etExpireMonth.error = "Please enter Expire Month"
            isValid = false
        } else if (!expireMonth.matches("^\\d{1,2}$".toRegex()) || expireMonth.toInt() !in 1..12) {
            etExpireMonth.error = "Invalid Expire Month"
            isValid = false
        }

        if (expireYear.isEmpty()) {
            etExpireYear.error = "Please enter Expire Year"
            isValid = false
        } else if (!expireYear.matches("^\\d{4}$".toRegex()) || expireYear.toInt() < Calendar.getInstance()
                .get(Calendar.YEAR)
        ) {
            etExpireYear.error = "Invalid Expire Year"
            isValid = false
        }

        if (cvv.isEmpty()) {
            etCvv.error = "Please enter CVV"
            isValid = false
        } else if (!cvv.matches("^\\d{3}$".toRegex())) {
            etCvv.error = "Invalid CVV"
            isValid = false
        }

// Save payment data if validation passes
        if (isValid) {

            //Get Fee
            val classFee = intent.getStringExtra("ClassFee")
            val paymentId = dbRef.push().key!!

            val payment =
                PaymentModel(paymentId, cardNumber, cardHolderName, expireMonth, expireYear, cvv, classFee)

            dbRef.child(paymentId).setValue(payment)
                .addOnCompleteListener {
                    Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                    etCardNumber.text.clear()
                    etCardHolderName.text.clear()
                    etExpireMonth.text.clear()
                    etExpireYear.text.clear()
                    etCvv.text.clear()
                }
                .addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }
            val intent = Intent(this, PaymentFetchingActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Please fix the errors and try again", Toast.LENGTH_SHORT).show()
        }
    }


}




