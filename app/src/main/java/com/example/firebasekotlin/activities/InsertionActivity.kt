package com.example.firebasekotlin.activities
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasekotlin.models.BudgetModel
import com.example.firebasekotlin.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {

    private lateinit var etBgName: EditText
    private lateinit var etBgCategory: EditText
    private lateinit var etBgAmount: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        etBgName = findViewById(R.id.etBgName)
        etBgCategory = findViewById(R.id.etBgCategory)
        etBgAmount = findViewById(R.id.etBgAmount)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Budgets")

        btnSaveData.setOnClickListener {
            saveBudgetData()
        }
    }

    private fun saveBudgetData() {

        //getting values
        val bgName = etBgName.text.toString()
        val bgCategory = etBgCategory.text.toString()
        val bgAmount = etBgAmount.text.toString()

        if(bgName.isEmpty()||bgCategory.isEmpty()||bgAmount.isEmpty()) {

            if (bgName.isEmpty()) {
                etBgName.error = "Please enter name"
            }
            if (bgCategory.isEmpty()) {
                etBgCategory.error = "Please enter age"
            }
            if (bgAmount.isEmpty()) {
                etBgAmount.error = "Please enter salary"
            }
            Toast.makeText(this, "Empty Data Detected", Toast.LENGTH_LONG).show()
        }

        else {


            val budgetID = dbRef.push().key!!

            val employee = BudgetModel(budgetID, bgName, bgCategory, bgAmount)

            dbRef.child(budgetID).setValue(employee)
                .addOnCompleteListener {
                    Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                    etBgName.text.clear()
                    etBgCategory.text.clear()
                    etBgAmount.text.clear()


                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }
        }

    }

}