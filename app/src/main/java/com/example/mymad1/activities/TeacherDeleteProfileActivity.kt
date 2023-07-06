package com.example.mymad1.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mymad1.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class TeacherDeleteProfileActivity:AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.teacher_view_profile)

        showDeleteAlert()


    }


    private fun showDeleteAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete profile?")
            .setTitle("Delete Profile")
            .setPositiveButton("Yes") { dialog, which ->

                val userId = FirebaseAuth.getInstance().currentUser?.uid
                val userRef = FirebaseDatabase.getInstance().getReference("teachers/$userId")

                // Use the removeValue() method to delete the node from the database
                userRef.removeValue()
                    .addOnSuccessListener {
                        // Display a success message to the user
                        Toast.makeText(this, "Profile deleted successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { error ->
                        // Display an error message to the user
                        Toast.makeText(this, "Error deleting Profile: ${error.message}", Toast.LENGTH_SHORT).show()
                    }

                //Delete from Firebase Authentication
                FirebaseAuth.getInstance().currentUser?.delete()
                    ?.addOnSuccessListener {
                        Toast.makeText(this, "Profile deleted successfully", Toast.LENGTH_SHORT).show()
                    }
                    ?.addOnFailureListener {
                        Toast.makeText(this, "Error deleting Profile", Toast.LENGTH_SHORT).show()
                    }

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)


            }
            .setNegativeButton("No") { dialog, which ->
                // Do nothing

                dialog.cancel()
            }

        val dialog = builder.create()
        dialog.show()
    }
}