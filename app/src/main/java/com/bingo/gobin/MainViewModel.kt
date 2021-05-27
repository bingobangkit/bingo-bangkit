package com.bingo.gobin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.bingo.gobin.data.model.Order
import com.bingo.gobin.data.repository.MainRepositoryImpl
import com.bingo.gobin.vo.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
class MainViewModel : ViewModel() {

    private var repo : MainRepositoryImpl = MainRepositoryImpl()

    fun setOrder(order: Order) = repo.setOrder(order)


}