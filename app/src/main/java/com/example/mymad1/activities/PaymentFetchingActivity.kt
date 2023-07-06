package com.example.mymad1.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymad1.R
import com.example.mymad1.models.PaymentModel
import com.google.firebase.database.*

class PaymentFetchingActivity : AppCompatActivity() {
    private lateinit var paymentRecyclerView: RecyclerView
    private lateinit var tvLoadingData : TextView
    private lateinit var paymentList: ArrayList<PaymentModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_fetching)
        paymentRecyclerView = findViewById(R.id.tvPayment)
        paymentRecyclerView.layoutManager = LinearLayoutManager(this)
        paymentRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        paymentList = arrayListOf<PaymentModel>()
        dbRef = FirebaseDatabase.getInstance().getReference("payments")
        getCustomersData()

    }
    private fun getCustomersData(){
        paymentRecyclerView.visibility = View.GONE
        tvLoadingData.visibility= View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("payments")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                paymentList.clear()
                if (snapshot.exists()){
                    for (PaymentSnap in snapshot.children){
                        val paymentsData = PaymentSnap.getValue(PaymentModel::class.java)
                        paymentList.add(paymentsData!!)
                    }
                    val mAdapter = PaymentAdapter(paymentList)
                    paymentRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : PaymentAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@PaymentFetchingActivity, PaymentDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("PaymentId", paymentList[position].PaymentId)
                            intent.putExtra("CardNumber", paymentList[position].CardNumber)
                            intent.putExtra("CardHolderName", paymentList[position].CardHolderName)
                            intent.putExtra("ExpireMonth", paymentList[position].ExpireMonth)
                            intent.putExtra("ExpireYear", paymentList[position].ExpireYear)
                            intent.putExtra("Cvv", paymentList[position].Cvv)

                            //Class Fee
                            intent.putExtra("ClassFee", paymentList[position].ClassFee)
                            startActivity(intent)
                        }

                    })

                    paymentRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}




//class PaymentFetchingActivity : AppCompatActivity() {
//    private lateinit var paymentRecyclerView: RecyclerView
//    private lateinit var tvLoadingData : TextView
//    private lateinit var paymentList: ArrayList<PaymentModel>
//    private lateinit var dbRef: DatabaseReference
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_payment_fetching)
//        paymentRecyclerView = findViewById(R.id.tvPayment)
//        paymentRecyclerView.layoutManager = LinearLayoutManager(this)
//        paymentRecyclerView.setHasFixedSize(true)
//        tvLoadingData = findViewById(R.id.tvLoadingData)
//
//        paymentList = arrayListOf<PaymentModel>()
//        dbRef = FirebaseDatabase.getInstance().getReference("payments")
//        getCustomersData()
//
//    }
//    private fun getCustomersData(){
//        paymentRecyclerView.visibility = View.GONE
//        tvLoadingData.visibility= View.VISIBLE
//
//        dbRef = FirebaseDatabase.getInstance().getReference("payments")
//
//        dbRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                paymentList.clear()
//                if (snapshot.exists()){
//                    for (PaymentSnap in snapshot.children){
//                        val PaymentsData = PaymentSnap.getValue(PaymentModel::class.java)
//                        paymentList.add(PaymentsData!!)
//                    }
//                    val mAdapter = PaymentAdapter(paymentList)
//                    paymentRecyclerView.adapter = mAdapter
//
//                    mAdapter.setOnItemClickListener(object : PaymentAdapter.onItemClickListener{
//                        override  fun onItemClick(position: Int) {
//
//                            val intent = Intent(this@PaymentFetchingActivity, PaymentDetailsActivity::class.java)
//
//                            //put extras
//                            intent.putExtra("PaymentId", paymentList[position].PaymentId)
//                            intent.putExtra("CardNumber", paymentList[position].CardNumber)
//                            intent.putExtra("CardHolderName", paymentList[position].CardHolderName)
//                            intent.putExtra("ExpireMonth", paymentList[position].ExpireMonth)
//                            intent.putExtra("ExpireYear", paymentList[position].ExpireYear)
//                            intent.putExtra("Cvv", paymentList[position].Cvv)
//                            startActivity(intent)
//                        }
//
//                    })
//
//                    paymentRecyclerView.visibility = View.VISIBLE
//                    tvLoadingData.visibility = View.GONE
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })
//    }
//}