package com.bingo.gobin.data.repository

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bingo.gobin.data.model.Order
import com.bingo.gobin.data.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class MainRepositoryImpl {
    private val users = Firebase.firestore.collection("users")
    fun setOrder(order: Order?) {
        val firestore = FirebaseFirestore.getInstance()
        val orderCollection = firestore.collection("order")
        order?.let {
            CoroutineScope(Dispatchers.IO).launch {
                orderCollection.document().set(order)
            }
        }
    }


    fun getOrder(id_user: String): LiveData<out List<Order>> {
        val list = MutableLiveData<List<Order>>()
        val order = ArrayList<Order>()
        Firebase.firestore.collection("order")
            .whereEqualTo("id_user", id_user)
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
                                id = document.id,
                                id_invoice = document.data["id_invoice"].toString(),
                                id_user = document.data["id_user"].toString(),
                                id_driver = document.data["id_driver"].toString(),
                                date = document.data["date"].toString(),
                                address = document.data["id_address"].toString(),
                                id_type = document.data["id_type"].toString(),
                                amount = document.data["amount"].toString(),
                                latitude = document.data["latitude"].toString(),
                                longitude = document.data["longitude"].toString(),
                                status = document.data["status"].toString(),
                                total_price = document.data["total_price"].toString()
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

    fun getOrderById(id: String): LiveData<out Order> {
        val order = MutableLiveData<Order>()

        Firebase.firestore.collection("order").document(id)
            .addSnapshotListener(object : EventListener<DocumentSnapshot?> {
                override fun onEvent(value: DocumentSnapshot?, e: FirebaseFirestoreException?) {
                    if (e != null) {
                        Log.w(ContentValues.TAG, "Listen error", e)
                        return
                    }
                    order.postValue(
                        Order(
                            id = value?.id.toString(),
                            id_invoice = value?.data?.get("id_invoice").toString(),
                            id_user = value?.data?.get("id_user").toString(),
                            id_driver = value?.data?.get("id_driver").toString(),
                            address = value?.data?.get("address").toString(),
                            id_type = value?.data?.get("id_type").toString(),
                            amount = value?.data?.get("amount").toString(),
                            latitude = value?.data?.get("latitude").toString(),
                            longitude = value?.data?.get("longitude").toString(),
                            total_price = value?.data?.get("total_price").toString(),
                            date = value?.data?.get("date").toString(),
                            status = value?.data?.get("status").toString()
                        )
                    )

                }
            })
        return order
    }

    fun updateBalance(id: String, balance: String) {

        Firebase.firestore.collection("users").document(id).update("saldo", balance)

    }

    fun getUserById(id: String): LiveData<out User> {
        val user = MutableLiveData<User>()

        Firebase.firestore.collection("users").document(id)
            .addSnapshotListener(object : EventListener<DocumentSnapshot?> {
                override fun onEvent(value: DocumentSnapshot?, e: FirebaseFirestoreException?) {
                    if (e != null) {
                        Log.w(ContentValues.TAG, "Listen error", e)
                        return
                    }
                    user.postValue(
                        User(
                            id = value?.id.toString(),
                            address = value?.data?.get("address").toString(),
                            latitude = value?.data?.get("latitude").toString(),
                            longitude = value?.data?.get("longitude").toString(),
                            phone = value?.data?.get("phone").toString(),
                            saldo = value?.data?.get("saldo").toString(),
                            poin = value?.data?.get("poin").toString(),
                            name = value?.data?.get("name").toString()
                        )
                    )

                }
            })
        return user
    }


    fun register(email: String, password: String, user: User): LiveData<out Boolean> {
        val task = MutableLiveData<Boolean>()
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    user.id = it.result.user?.uid
                    CoroutineScope(Dispatchers.IO).launch {
                        users.document(user.id.toString()).set(user).await()
                    }
                    task.postValue(true)
                } else {
                    task.postValue(false)
                }
            }
        return task
    }

    fun login(email : String, password: String): LiveData<out Boolean> {
        val task = MutableLiveData<Boolean>()
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    task.postValue(true)
                } else {
                    task.postValue(false)
                }
            }
        return task
    }


    //    fun register(email: String, password: String): LiveData<out Boolean> {

    //        repositoryImpl.register(email, password).asTask().addOnCompleteListener { p0 ->
    //            if (p0.isSuccessful) {
    //                task.postValue(true)
    //            } else {
    //                task.postValue(false)
    //            }
    //        }
    //        return task
    //    }


}

