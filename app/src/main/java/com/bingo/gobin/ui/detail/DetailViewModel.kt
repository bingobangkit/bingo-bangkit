package com.bingo.gobin.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bingo.gobin.data.model.Order
import com.bingo.gobin.data.model.User
import com.bingo.gobin.data.repository.MainRepositoryImpl
import com.bingo.gobin.util.lazyDeferred

class DetailViewModel:ViewModel() {
    private val repository = MainRepositoryImpl()
    suspend fun getDetailOrder(id: String): LiveData<out Order> {
        val detailOrder by lazyDeferred {
            repository.getOrderById(id)
        }
        return detailOrder.await()
    }

    fun updateBalance(id:String,balance:String){
        repository.updateBalance(id,balance)
    }

    suspend fun getUserById(id_user: String):LiveData<out User> {
        val user by lazyDeferred {
            repository.getUserById(id_user)
        }
        return user.await()
    }
}