package com.bingo.gobin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.navigation.NavController
import com.bingo.gobin.databinding.ActivityMainBinding
import com.bingo.gobin.ui.camera.CaptureFragment
import com.bingo.gobin.ui.home.ContentFragment
import com.bingo.gobin.ui.pickup.ScheduleFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.commit {
            replace(R.id.main_fragment_container,ContentFragment())
            addToBackStack(null)
        }
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menu->
            when(menu.itemId){
                R.id.btn_camera_nav ->{
                    Toast.makeText(this,"hallo", Toast.LENGTH_SHORT).show()
                    supportFragmentManager.commit {
                        replace(R.id.main_fragment_container, CaptureFragment())
                        addToBackStack(null)
                    }
                    true
                }
                R.id.btn_home_nav ->{
                    supportFragmentManager.commit {
                        replace(R.id.main_fragment_container,ContentFragment())
                        addToBackStack(null)
                    }
                    true
                }
                R.id.btn_schedule_nav->{
                    supportFragmentManager.commit {
                        replace(R.id.main_fragment_container, ScheduleFragment())
                        addToBackStack(null)
                    }
                    true
                }
                else -> false
            }
        }

    }

}