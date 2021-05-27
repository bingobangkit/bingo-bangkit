package com.bingo.gobin.ui.pickup

import androidx.lifecycle.*
import com.bingo.gobin.data.model.Order
import com.bingo.gobin.data.model.Type
import com.bingo.gobin.data.repository.MainRepositoryImpl
import com.bingo.gobin.util.lazyDeferred
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PickupViewModel : ViewModel() {
    private var repo: MainRepositoryImpl = MainRepositoryImpl()
    private val _amountPlastic = MutableLiveData<Int>().apply { value = 1 }
    private var _datePickup = MutableLiveData<String>()
    val datePickup = _datePickup
    val amountPlastic: LiveData<Int> = _amountPlastic


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



    suspend fun getType(): List<Type> {
        val fs = Firebase.firestore.collection("type").get().await()
        val data = fs.toObjects(Type::class.java) as List<Type>
        return data
    }

//



}