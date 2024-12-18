package com.example.operationclient.ui.main.user

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.operationclient.databinding.ActivityOperationUserBinding
import com.example.operationclient.ui.main.Operation
import com.example.operationclient.ui.main.OperationArrayAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class OperationActivityUser : AppCompatActivity() {

    lateinit var binding: ActivityOperationUserBinding
    lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOperationUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val context = binding.root.context

        initTab(context)
    }

    @JvmSuppressWildcards
    private fun initTab(context: Context) {

        database = Firebase.database.reference

        val readOperationListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val t : GenericTypeIndicator<Operation> =
                    object : GenericTypeIndicator<Operation>() {}

                val operations = ArrayList<Operation>()

                for (item in snapshot.children){
                    operations.add(item.getValue(t)!!)
                }

                operations.forEach {
                    Log.d("","$it")
                }

                if (operations != null) {
                    val operationArrayAdapter: ArrayAdapter<Operation> =
                        OperationArrayAdapter(context, operations)

                    binding.listProd.adapter = operationArrayAdapter
                }else {
                    Toast.makeText(context,"Данные отсутсвуют", Toast.LENGTH_SHORT).show()
                }
            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Ошибка вывода. Возможно данная информация скрыта или отсутствует",
                    Toast.LENGTH_SHORT).show()
            }

        }

        database.child("operations").addValueEventListener(readOperationListener)

    }
}