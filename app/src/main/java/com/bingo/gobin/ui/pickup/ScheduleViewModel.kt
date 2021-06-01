package com.bingo.gobin.ui.pickup

import android.util.Log
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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ScheduleViewModel : ViewModel() {

    private val repository by lazy { MainRepositoryImpl() }
    private val _amountPlastic = MutableLiveData<Int>().apply { value = 0 }
    private val _amountCardboard = MutableLiveData<Int>().apply { value = 0 }
    private val _amountSteel = MutableLiveData<Int>().apply { value = 0 }
    private var _datePickup = MutableLiveData<String>()
    private var _address = MutableLiveData(INITIAL_FILL_ADDRESS)
    private val _totalPlastic = MutableLiveData<Int>().apply { value = 0 }
    private val _totalCard = MutableLiveData<Int>().apply { value = 0 }
    private val _totalSteel = MutableLiveData<Int>().apply { value = 0 }
    val totalPlastic = _totalPlastic
    val totalCard = _totalCard
    val totalSteel = _totalSteel


    val datePickup = _datePickup
    val amountPlastic: LiveData<Int> = _amountPlastic
    val amountCardboard: LiveData<Int> = _amountCardboard
    val amountSteel: LiveData<Int> = _amountSteel
    val address: LiveData<String> = _address
    val latitude = MutableLiveData("")
    val longitude = MutableLiveData("")


    suspend fun getUserOrder(id_user: String): LiveData<out List<Order>> {
        val order by lazyDeferred {
            repository.getOrder(id_user)
        }
        return order.await()
    }

    suspend fun getUserById(id_user: String): LiveData<out User> {
        val user by lazyDeferred {
            repository.getUserById(id_user)
        }
        return user.await()
    }

    fun addPlasticAmount() {
        viewModelScope.launch(Dispatchers.IO) {
            val amount = _amountPlastic.value
            _amountPlastic.postValue(amount!! + 1)
            amountPlastic.asFlow().collect {
                val price = getType()[0].price
                _totalPlastic.postValue(price * it)
            }
        }
    }

    fun minPlasticAmount() {
        viewModelScope.launch(Dispatchers.IO) {
            val amount = _amountPlastic.value
            if (_amountPlastic.value != 0){
                _amountPlastic.postValue(amount!! - 1)
                amountPlastic.asFlow().collect {
                    val price = getType()[0].price
                    _totalPlastic.postValue(price * it)
                }
            }
        }

    }

    fun addCardboardAmount() {
        viewModelScope.launch(Dispatchers.IO) {
            val amount = _amountCardboard.value
            _amountCardboard.postValue(amount!! + 1)
            amountCardboard.asFlow().collect {
                val price = getType()[1].price
                _totalCard.postValue(price * it)
            }
        }

    }

    fun minCardboardAmount() {
        viewModelScope.launch(Dispatchers.IO) {
            val amount = _amountCardboard.value
            if (_amountCardboard.value != 0){
                _amountCardboard.postValue(amount!! - 1)
                amountCardboard.asFlow().collect {
                    val price = getType()[1].price
                    _totalCard.postValue(price * it)
                }
            }
        }

    }


    fun addSteelAmount() {
        viewModelScope.launch(Dispatchers.IO) {
            val amount = _amountSteel.value
            _amountSteel.postValue(amount!! + 1)
            amountSteel.asFlow().collect {
                val price = getType()[2].price
                _totalSteel.postValue(price * it)
            }
        }

    }

    fun minSteelAmount() {
        viewModelScope.launch(Dispatchers.IO) {
            val amount = _amountSteel.value
            if (_amountSteel.value != 0) {
                _amountSteel.postValue(amount!! - 1)
                amountSteel.asFlow().collect {
                    val price = getType()[2].price
                    _totalSteel.postValue(price * it)
                }
            }
        }
    }


    fun getTotal() = liveData {
        val total = MutableLiveData(0)
        val pricePlastic = getType()[0].price
        val priceCard = getType()[1].price
        val priceSteel = getType()[2].price
        Log.d("TAG", "$priceCard")
        total.asFlow().collect { t ->
            _amountPlastic.asFlow().combine(_amountCardboard.asFlow()) { aPlastic, aCard ->
                total.postValue(t + (aPlastic * pricePlastic) + (aCard * priceCard))
                t + (aPlastic * pricePlastic) + (aCard * priceCard)
            }.combine(_amountSteel.asFlow()) { aPnC, aSteel ->
                total.postValue(t + aPnC + (aSteel * priceSteel))
                t + aPnC + (aSteel * priceSteel)
            }.collect {
                emit(it)
            }
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


