package com.example.operationclient.ui.main.admin.ui.operation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.operationclient.databinding.FragmentAddOperationBinding
import com.example.operationclient.ui.main.Operation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AddOperationFragment : BottomSheetDialogFragment() {

    lateinit var database: DatabaseReference
    private var _binding: FragmentAddOperationBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddOperationBinding.inflate(inflater, container, false)

        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = view.context

        addOperation()

    }

    private fun addOperation() {

        database = Firebase.database.reference

        binding.addProduct.setOnClickListener {

            if (binding.nameEditText.text == null) {
                Toast.makeText(context, "Введите название", Toast.LENGTH_SHORT).show()
            } else if (binding.priceEditText.text == null) {
                Toast.makeText(context, "Введите цену", Toast.LENGTH_SHORT).show()
            } else {

                val operation = Operation(
                    binding.nameEditText.text.toString(),
                    binding.priceEditText.text.toString().toInt()
                )

                binding.nameEditText.text.clear()
                binding.priceEditText.text.clear()

                database.child("operations").child(operation.name).setValue(operation)

                Toast.makeText(context,"Операция успешно добавлена",Toast.LENGTH_SHORT).show()

                parentFragmentManager.beginTransaction().remove(this@AddOperationFragment)
                    .commit()
            }

        }
    }
}