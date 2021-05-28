package com.bingo.gobin.ui.pickup

import androidx.lifecycle.*
import com.bingo.gobin.data.model.Order
import com.bingo.gobin.data.model.Type
import com.bingo.gobin.data.model.User
import com.bingo.gobin.data.repository.MainRepositoryImpl
import com.bingo.gobin.util.INITIAL_FILL_ADDRESS
import com.bingo.gobin.util.lazyDeferred
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ScheduleViewModel : ViewModel() {

    private val repository by lazy { MainRepositoryImpl() }
    private val _amountPlastic = MutableLiveData<Int>().apply { value = 1 }
    private var _datePickup = MutableLiveData<String>()
    private var _address = MutableLiveData(INITIAL_FILL_ADDRESS)
    val datePickup = _datePickup
    val amountPlastic: LiveData<Int> = _amountPlastic
    val address: LiveData<String> = _address
    val latitude = MutableLiveData("")
    val longitude = MutableLiveData("")

    suspend fun getUserOrder(id_user: String): LiveData<out List<Order>> {
        val order by lazyDeferred {
            repository.getOrder(id_user)
        }
        return order.await()
    }

    suspend fun getUserById(id_user: String):LiveData<out User> {
        val user by lazyDeferred {
            repository.getUserById(id_user)
        }
        return user.await()
    }

    fun addPlasticAmount() {
        viewModelScope.launch(Dispatchers.IO) {
            val amount = _amountPlastic.value
            _amountPlastic.postValue(amount!! + 1)
        }

    }

    fun minPlasticAmount() {
        viewModelScope.launch(Dispatchers.IO) {
            val amount = _amountPlastic.value
            if (_amountPlastic.value != 1) _amountPlastic.postValue(amount!! - 1)
        }

    }

    fun getTotal() = liveData {
        val price = getType()[0].price
        _amountPlastic.asFlow().collect {
            emit(price * it)
        }
    }

    fun setDatePickup(date: String) {
        _datePickup.postValue(date)
    }

    fun setAddress(address: String) {
        _address.postValue(address)
    }


    fun setOrder(order: Order): Boolean {
        return try {
            repository.setOrder(order)
            true
        } catch (e: FirebaseFirestoreException) {
            e.printStackTrace()
            false
        }

    }


    suspend fun getType(): List<Type> {
        val fs = Firebase.firestore.collection("type").get().await()
        val data = fs.toObjects(Type::class.java) as List<Type>
        return data
    }
}


