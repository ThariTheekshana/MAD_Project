package com.example.mymad1.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.mymad1.activities.PaymentFetchingActivity
import com.example.mymad1.models.PaymentModel
import com.example.mymad1.R
import com.google.firebase.database.FirebaseDatabase


class PaymentDetailsActivity : AppCompatActivity() {
    private lateinit var tvPaymentId: TextView
    private lateinit var tvCardNumber: TextView
    private lateinit var tvCardHolderName: TextView
    private lateinit var tvExpireMonth: TextView
    private lateinit var tvExpireYear: TextView
    private lateinit var tvCvv: TextView
    private lateinit var ClassFee: TextView

    private lateinit var btnUpdatePayment: Button
    private lateinit var btnDeletePayment: Button
    private lateinit var btnPaymentDone: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_details)

        initView()
        setValuesToViews()

        btnUpdatePayment.setOnClickListener {
            openPaymentUpdateDialog(
                intent.getStringExtra("PaymentId").toString(),
                intent.getStringExtra("CardNumber").toString(),


                )
        }

        btnDeletePayment.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("PaymentId").toString()
            )
            val intent = Intent(this, EnterPaymentGatewayActivity::class.java)
            startActivity(intent)
        }

        btnPaymentDone = findViewById(R.id.btnDone)

        btnPaymentDone.setOnClickListener {
            val intent = Intent(this, PaymentSuccessfulActivity::class.java)
            startActivity(intent)
        }

    }

    private fun initView() {
        tvPaymentId = findViewById(R.id.tvPaymentId)
        tvCardNumber = findViewById(R.id.tvCardNumber)
        tvCardHolderName = findViewById(R.id.tvCardHolderName)
        tvExpireMonth = findViewById(R.id.tvExpireMonth)
        tvExpireYear = findViewById(R.id.tvExpireYear)
        tvCvv = findViewById(R.id.tvCvv)
        ClassFee = findViewById(R.id.tvClassFee)

        btnUpdatePayment = findViewById(R.id.btnUpdatePayment)
        btnDeletePayment = findViewById(R.id.btnDeletePayment)
    }

    private fun setValuesToViews() {
        tvPaymentId.text = intent.getStringExtra("PaymentId")
        tvCardNumber.text = intent.getStringExtra("CardNumber")
        tvCardHolderName.text = intent.getStringExtra("CardHolderName")
        tvExpireMonth.text = intent.getStringExtra("ExpireMonth")
        tvExpireYear.text = intent.getStringExtra("ExpireYear")
        tvCvv.text = intent.getStringExtra("Cvv")
        ClassFee.text = intent.getStringExtra("ClassFee")

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("payments").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Payment data deleted", Toast.LENGTH_LONG).show()

            //val intent = Intent(this, PaymentFetchingActivity::class.java)
            finish()
            //startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun openPaymentUpdateDialog(
        PaymentId: String,
        CardNumber: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_payment_diolog, null)

        mDialog.setView(mDialogView)

        val etCardNumber = mDialogView.findViewById<EditText>(R.id.etCardNumber)
        val etCardHolderName= mDialogView.findViewById<EditText>(R.id.etCardHolderName)
        val etExpireMonth = mDialogView.findViewById<EditText>(R.id.etExpireMonth)
        val etExpireYear = mDialogView.findViewById<EditText>(R.id.etExpireYear)
        val etCvv = mDialogView.findViewById<EditText>(R.id.etCvv)

        val btnUpdatePayment = mDialogView.findViewById<Button>(R.id.btnUpdatePayment)

        etCardNumber.setText(intent.getStringExtra("CardNumber").toString())
        etCardHolderName.setText(intent.getStringExtra("CardHolderName").toString())
        etExpireMonth.setText(intent.getStringExtra("ExpireMonth").toString())
        etExpireYear.setText(intent.getStringExtra("ExpireYear").toString())
        etCvv.setText(intent.getStringExtra("Cvv").toString())

        mDialog.setTitle("Updating $CardNumber Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdatePayment.setOnClickListener {
            updatePayment(
                PaymentId,
                etCardNumber.text.toString(),
                etCardHolderName.text.toString(),
                etExpireMonth.text.toString(),
                etExpireYear.text.toString(),
                etCvv.text.toString()
            )

            Toast.makeText(applicationContext, "Payment Data Updated", Toast.LENGTH_LONG).show()

//we are setting updated data to our textviews
            tvCardNumber.text = etCardNumber.text.toString()
            tvCardHolderName.text = etCardHolderName.text.toString()
            tvExpireMonth.text = etExpireMonth.text.toString()
            tvExpireYear.text = etExpireYear.text.toString()
            tvCvv.text=etCvv.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updatePayment(
        id:String,
        cardnumber: String,
        cardholdername: String,
        expiremonth: String,
        expireyear: String,
        cvv: String,
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("payments").child(id)
        val PaymentsInfo = PaymentModel(id,cardnumber, cardholdername, expiremonth, expireyear,cvv)
        dbRef.setValue(PaymentsInfo)
    }
}












//class PaymentDetailsActivity : AppCompatActivity() {
//    private lateinit var tvPaymentId: TextView
//    private lateinit var tvCardNumber: TextView
//    private lateinit var tvCardHolderName: TextView
//    private lateinit var tvExpireMonth: TextView
//    private lateinit var tvExpireYear: TextView
//    private lateinit var tvCvv: TextView
//    private lateinit var btnUpdatePayment: Button
//    private lateinit var btnDeletePayment: Button
//    private lateinit var btnPaymentDone: Button
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_payment_details)
//
//        initView()
//        setValuesToViews()
//
//        btnUpdatePayment.setOnClickListener {
//            openPaymentUpdateDialog(
//                intent.getStringExtra("PaymentId").toString(),
//                intent.getStringExtra("CardNumber").toString(),
//
//
//                )
//        }
//
//        btnDeletePayment.setOnClickListener {
//            deleteRecord(
//                intent.getStringExtra("PaymentId").toString()
//            )
//        }
//
//        btnPaymentDone = findViewById(R.id.btnDone)
//
//        btnPaymentDone.setOnClickListener {
//            val intent = Intent(this, PaymentSuccessfulActivity::class.java)
//            startActivity(intent)
//        }
//
//    }
//
//    private fun initView() {
//        tvPaymentId = findViewById(R.id.tvPaymentId)
//        tvCardNumber = findViewById(R.id.tvCardNumber)
//        tvCardHolderName = findViewById(R.id.tvCardHolderName)
//        tvExpireMonth = findViewById(R.id.tvExpireMonth)
//        tvExpireYear = findViewById(R.id.tvExpireYear)
//        tvCvv = findViewById(R.id.tvCvv)
//
//        btnUpdatePayment = findViewById(R.id.btnUpdatePayment)
//        btnDeletePayment = findViewById(R.id.btnDeletePayment)
//    }
//
//    private fun setValuesToViews() {
//        tvPaymentId.text = intent.getStringExtra("PaymentId")
//        tvCardNumber.text = intent.getStringExtra("CardNumber")
//        tvCardHolderName.text = intent.getStringExtra("CardHolderName")
//        tvExpireMonth.text = intent.getStringExtra("ExpireMonth")
//        tvExpireYear.text = intent.getStringExtra("ExpireYear")
//        tvCvv.text = intent.getStringExtra("Cvv")
//
//    }
//
//    private fun deleteRecord(
//        id: String
//    ){
//        val dbRef = FirebaseDatabase.getInstance().getReference("payments").child(id)
//        val mTask = dbRef.removeValue()
//
//        mTask.addOnSuccessListener {
//            Toast.makeText(this, "Payment data deleted", Toast.LENGTH_LONG).show()
//
//            val intent = Intent(this, PaymentFetchingActivity::class.java)
//            finish()
//            startActivity(intent)
//        }.addOnFailureListener{ error ->
//            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
//        }
//    }
//
//    private fun openPaymentUpdateDialog(
//        PaymentId: String,
//        CardNumber: String
//    ) {
//        val mDialog = AlertDialog.Builder(this)
//        val inflater = layoutInflater
//        val mDialogView = inflater.inflate(R.layout.update_payment_diolog, null)
//
//        mDialog.setView(mDialogView)
//
//        val etCardNumber = mDialogView.findViewById<EditText>(R.id.etCardNumber)
//        val etCardHolderName= mDialogView.findViewById<EditText>(R.id.etCardHolderName)
//        val etExpireMonth = mDialogView.findViewById<EditText>(R.id.etExpireMonth)
//        val etExpireYear = mDialogView.findViewById<EditText>(R.id.etExpireYear)
//        val etCvv = mDialogView.findViewById<EditText>(R.id.etCvv)
//
//        val btnUpdatePayment = mDialogView.findViewById<Button>(R.id.btnUpdatePayment)
//
//        etCardNumber.setText(intent.getStringExtra("CardNumber").toString())
//        etCardHolderName.setText(intent.getStringExtra("CardHolderName").toString())
//        etExpireMonth.setText(intent.getStringExtra("ExpireMonth").toString())
//        etExpireYear.setText(intent.getStringExtra("ExpireYear").toString())
//        etCvv.setText(intent.getStringExtra("Cvv").toString())
//
//        mDialog.setTitle("Updating $CardNumber Record")
//
//        val alertDialog = mDialog.create()
//        alertDialog.show()
//
//        btnUpdatePayment.setOnClickListener {
//            updatePayment(
//                PaymentId,
//                etCardNumber.text.toString(),
//                etCardHolderName.text.toString(),
//                etExpireMonth.text.toString(),
//                etExpireYear.text.toString(),
//                etCvv.text.toString()
//            )
//
//            Toast.makeText(applicationContext, "Payment Data Updated", Toast.LENGTH_LONG).show()
//
//            //we are setting updated data to our textviews
//            tvCardNumber.text = etCardNumber.text.toString()
//            tvCardHolderName.text = etCardHolderName.text.toString()
//            tvExpireMonth.text = etExpireMonth.text.toString()
//            tvExpireYear.text =  etExpireYear.text.toString()
//            tvCvv.text=etCvv.toString()
//
//            alertDialog.dismiss()
//        }
//    }
//
//    private fun updatePayment(
//        id:String,
//        cardnumber: String,
//        cardholdername: String,
//        expiremonth: String,
//        expireyear: String,
//        cvv: String,
//    ) {
//        val dbRef = FirebaseDatabase.getInstance().getReference("payments").child(id)
//        val PaymentsInfo = PaymentModel(id,cardnumber, cardholdername, expiremonth, expireyear,cvv)
//        dbRef.setValue(PaymentsInfo)
//    }
//}