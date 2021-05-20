package com.bingo.gobin.data.repository

import android.util.Log
import com.bingo.gobin.data.model.Order
import com.bingo.gobin.data.model.Type
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class MainRepositoryImpl {
    fun setOrder(order: Order?) {
        val firestore = FirebaseFirestore.getInstance()
        val orderCollection = firestore.collection("order")
        order?.let {
            CoroutineScope(Dispatchers.IO).launch {
                orderCollection.document().set(order)
            }
        }
    }




}

