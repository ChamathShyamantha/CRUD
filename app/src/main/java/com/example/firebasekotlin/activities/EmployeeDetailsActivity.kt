package com.example.firebasekotlin.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasekotlin.R
import com.example.firebasekotlin.models.BudgetModel
import com.google.firebase.database.FirebaseDatabase

class EmployeeDetailsActivity : AppCompatActivity() {
    private lateinit var tvBudgetId: TextView
    private lateinit var tvBgName: TextView
    private lateinit var tvBgCategory: TextView
    private lateinit var tvBgAmount: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("budgetID").toString(),
                intent.getStringExtra("bgName").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("budgetID").toString()
            )
        }

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Budgets").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Budget bgCategory deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun initView() {
        tvBudgetId = findViewById(R.id.tvBudgetId)
        tvBgName = findViewById(R.id.tvBgName)
        tvBgCategory = findViewById(R.id.tvCatergory)
        tvBgAmount = findViewById(R.id.tvBgAmount)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvBudgetId.text = intent.getStringExtra("budgetID")
        tvBgName.text = intent.getStringExtra("bgName")
        tvBgCategory.text = intent.getStringExtra("bgCategory")
        tvBgAmount.text = intent.getStringExtra("bgAmount")

    }

    private fun openUpdateDialog(
        budgetID: String,
        bgName: String
    ){
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog,null)

        mDialog.setView(mDialogView)
        val etBgName =  mDialogView.findViewById<EditText>(R.id.etBgName)
        val etBgCategory =  mDialogView.findViewById<EditText>(R.id.etBgCategory)
        val etBgAmount =  mDialogView.findViewById<EditText>(R.id.etBgAmount)
        val btnUpdateData =  mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etBgName.setText(intent.getStringExtra("bgName").toString())
        etBgCategory.setText(intent.getStringExtra("bgCategory").toString())
        etBgAmount.setText(intent.getStringExtra("bgAmount").toString())

        mDialog.setTitle("Updating $bgName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener{
            updateBgData(
                budgetID,
                etBgName.text.toString(),
                etBgCategory.text.toString(),
                etBgAmount.text.toString()
            )

            Toast.makeText(applicationContext, "Budget Updated", Toast.LENGTH_LONG).show()

            tvBgName.text = etBgName.text.toString()
            tvBgCategory.text = etBgCategory.text.toString()
            tvBgAmount.text = etBgAmount.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateBgData(
        id:String,
        name:String,
        category:String,
        amount:String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Budgets").child(id)
        val bgInfo = BudgetModel(id, name, category, amount)
        dbRef.setValue(bgInfo)
    }

}