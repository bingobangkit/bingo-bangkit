package com.bingo.gobin.data.repository

import android.util.Log
import com.bingo.gobin.data.model.Driver
import com.bingo.gobin.data.model.Order
import com.bingo.gobin.data.model.Type
import com.bingo.gobin.data.model.User
import com.bingo.gobin.vo.Resources
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

@ExperimentalCoroutinesApi
class MainRepositoryImpl {
    private val firestore = FirebaseFirestore.getInstance()

    //    fun getDriver() {
//        val db = FirebaseFirestore.getInstance().collection("drivers").document()
//        CoroutineScope(Dispatchers.IO).launch {
//            val x = db.get().await().data
//            Log.d("TAG", x.toString())
//        }
//    }
    fun setUser(user: User?) {
        val userCollection = firestore.collection("users")
        user?.let {
            CoroutineScope(Dispatchers.IO).launch {
                userCollection.document().set(user)
            }
        }
    }

    fun setOrder(order: Order?) {
        val orderCollection = firestore.collection("order")
        order?.let {
            CoroutineScope(Dispatchers.IO).launch {
                orderCollection.document().set(order)
            }
        }
    }

    suspend fun getOrder(): Flow<Resources<List<Order>>> = callbackFlow {
        val order = firestore.collection("order").document()
        val data = order.addSnapshotListener { value, _ ->
            if (value!!.exists()) {
                val data = value.toObject<List<Order>>()
                offer(Resources.Success(data))
            }
        }
        awaitClose { data.remove() }
    }

}

