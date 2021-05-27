package com.bingo.gobin.ui.pickup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bingo.gobin.data.model.Order
import com.bingo.gobin.data.repository.MainRepositoryImpl
import com.bingo.gobin.util.lazyDeferred
import kotlinx.coroutines.Deferred

class ScheduleViewModel : ViewModel() {

    private val repositoryImpl by lazy { MainRepositoryImpl() }

    suspend fun getUserOrder(id_user: String, status: String): LiveData<out List<Order>> {
        val order by lazyDeferred {
            repositoryImpl.getOrder(id_user, status)
        }
        return order.await()
    }


}