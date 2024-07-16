package com.example.operationclient.ui.main

import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor
import com.google.firebase.database.PropertyName

data class Operation(
    @PropertyName("name") val name: String = "",
    @PropertyName("price") val price: Int = 0
)
