package com.bingo.gobin.data.repository

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bingo.gobin.data.model.Order
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.model.ServerTimestamps
import com.google.firebase.firestore.model.mutation.ServerTimestampOperation
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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

    fun getServerTime(){
        ServerTimestampOperation.getInstance().toString()

    }

    fun getOrder(id_user: String, status: String): LiveData<out List<Order>> {
        val list = MutableLiveData<List<Order>>()
        val order = ArrayList<Order>()
        Firebase.firestore.collection("order")
            .whereEqualTo("id_user", id_user)
            .whereEqualTo("status", status)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    querySnapshot: QuerySnapshot?,
                    e: FirebaseFirestoreException?
                ) {
                    if (e != null) {
                        Log.w(ContentValues.TAG, "Listen error", e)
                        return
                    }
                    order.clear()
                    for (document in querySnapshot!!) {
                        order.add(
                            Order(
                                id_invoice = document.data["id_invoice"].toString(),
                                id_user = document.data["id_user"].toString(),
                                id_driver = document.data["id_driver"].toString(),
                                address = document.data["id_address"].toString(),
                                id_type = document.data["id_type"].toString(),
                                amount = document.data["amount"].toString(),
                                latitude = document.data["latitude"].toString(),
                                longitude = document.data["longitude"].toString()
                            )
                        )
                    }
                    list.postValue(order)
                    for (change in querySnapshot.documentChanges) {
                        if (change.type == DocumentChange.Type.ADDED) {
                            Log.d(ContentValues.TAG, "data:" + change.document.data)
                        }
                        val source = if (querySnapshot.metadata.isFromCache)
                            "local cache"
                        else
                            "server"
                        Log.d(ContentValues.TAG, "Data fetched from $source")
                    }
                }

            })
        return list
    }


}

