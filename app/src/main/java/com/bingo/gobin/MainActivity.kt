package com.bingo.gobin

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.bingo.gobin.data.model.Order
import com.bingo.gobin.data.repository.MainRepositoryImpl
import com.bingo.gobin.databinding.ActivityMainBinding
import com.bingo.gobin.ui.camera.CaptureFragment
import com.bingo.gobin.ui.home.ContentFragment
import com.bingo.gobin.ui.pickup.ScheduleFragment
import com.bingo.gobin.vo.Resources
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
//import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val viewModel : MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.commit {
            replace(R.id.main_fragment_container, ContentFragment())
            addToBackStack(null)
        }
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menu ->
            when (menu.itemId) {
                R.id.btn_camera_nav -> {
                    Toast.makeText(this, "hallo", Toast.LENGTH_SHORT).show()
                    supportFragmentManager.commit {
                        replace(R.id.main_fragment_container, CaptureFragment())
                    }
                    true
                }
                R.id.btn_home_nav -> {
                    supportFragmentManager.commit {
                        replace(R.id.main_fragment_container, ContentFragment())
                    }
                    true
                }
                R.id.btn_schedule_nav -> {
                    supportFragmentManager.commit {
                        replace(R.id.main_fragment_container, ScheduleFragment())
                    }
                    true
                }
                else -> false
            }

        }
//        lifecycleScope.launch {
//            viewModel.setOrder(
//                Order(
//                    price_mixed = 1000,
//                    price_plastic = 1000,
//                    id_user = "test",
//                )
//            )
//
//        }



    }

}