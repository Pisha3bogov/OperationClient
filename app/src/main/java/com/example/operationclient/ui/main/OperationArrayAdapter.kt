package com.example.operationclient.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.operationclient.R

class OperationArrayAdapter(private val context: Context, private val operation: List<Operation>)
    : ArrayAdapter<Operation>(context, R.layout.fragment_operation_list, operation) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater : LayoutInflater = LayoutInflater.from(context)

        val view: View = inflater.inflate(R.layout.fragment_operation_list, null)

        val prodName: TextView = view.findViewById(R.id.productName)

        val prodPrice: TextView = view.findViewById(R.id.productPrice)

        prodName.text = operation[position].name

        prodPrice.text = operation[position].price.toString()

        return view
    }

}