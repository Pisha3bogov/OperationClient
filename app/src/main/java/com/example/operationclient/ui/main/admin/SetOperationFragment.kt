package com.example.operationclient.ui.main.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.operationclient.databinding.FragmentSetOperationBinding
import com.example.operationclient.ui.main.Operation
import com.example.operationclient.ui.main.OperationArrayAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SetOperationFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSetOperationBinding? = null

    lateinit var database: DatabaseReference
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetOperationBinding.inflate(inflater, container, false)

        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = Firebase.database.reference

        init()

        updateOperation()

        delOperation()
    }

    private fun init() {


        val readOperationListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val t: GenericTypeIndicator<Operation> =
                    object : GenericTypeIndicator<Operation>() {}

                val operation: Operation? =
                    snapshot.child("operations").child(tag.toString()).getValue(t)

                if (operation != null) {
                    binding.nameEditText.text = operation.name
                    binding.priceEditText.hint = operation.price.toString()
                }


            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    context, "Ошибка вывода Возможно данная информация скрыта или отсутствует",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        database.addValueEventListener(readOperationListener)

    }

    private fun updateOperation() {

        binding.setProductPrice.setOnClickListener {

            val readOperationListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val price = binding.priceEditText.text.toString()

                    val t: GenericTypeIndicator<Operation> =
                        object : GenericTypeIndicator<Operation>() {}

                    val operation: Operation? =
                        snapshot.child("operations").child(tag.toString()).getValue(t)

                    if (operation == null) {
                        Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_SHORT).show()
                    } else if (price == "") {
                        Toast.makeText(context, "Введите новую цену", Toast.LENGTH_SHORT).show()
                    } else if (price == binding.priceEditText.hint.toString()) {
                        Toast.makeText(context, "Цена совпадает с текущей", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        val updateMap =
                            mapOf<String, Any>("name" to operation.name,"price" to price.toInt())

                        database.child("operations").child(tag.toString())
                            .updateChildren(updateMap).addOnSuccessListener {
                                Toast.makeText(context, "Цена изменена", Toast.LENGTH_SHORT).show()
                                parentFragmentManager.beginTransaction().remove(this@SetOperationFragment)
                                    .commit()
                                binding.priceEditText.text.clear()
                                binding.nameEditText.text = ""
                                binding.nameEditText.hint = ""
                            }.addOnFailureListener {
                                Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_SHORT)
                            }
                    }
                }


                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        context, "Ошибка вывода. Возможно данная информация скрыта или отсутствует",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }

            database.addListenerForSingleValueEvent(readOperationListener)
        }
    }

    private fun delOperation() {

        binding.delProduct.setOnClickListener{

            database.child("operations").child(tag.toString()).removeValue().addOnSuccessListener {
                Toast.makeText(context,"Удалено успешно", Toast.LENGTH_SHORT).show()
                parentFragmentManager.beginTransaction().remove(this@SetOperationFragment)
                    .commit()
            }.addOnFailureListener {
                Toast.makeText(context,"Произошла ошибка", Toast.LENGTH_SHORT).show()
            }

        }

    }

}