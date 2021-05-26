package com.bingo.gobin.ui.pickup

import androidx.lifecycle.ViewModel
import com.bingo.gobin.data.repository.MainRepositoryImpl
import com.bingo.gobin.util.lazyDeferred

class ScheduleViewModel : ViewModel() {
    private val repositoryImpl by lazy { MainRepositoryImpl() }

    val order by lazyDeferred {
        repositoryImpl.getOrder()
    }
}