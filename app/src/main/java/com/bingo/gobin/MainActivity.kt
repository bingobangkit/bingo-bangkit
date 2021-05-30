@file:Suppress("DEPRECATION")

package com.bingo.gobin
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.bingo.gobin.databinding.ActivityMainBinding
import com.bingo.gobin.ui.camera.ImageDetectionFragment
import com.bingo.gobin.ui.home.ContentFragment
import com.bingo.gobin.ui.pickup.ScheduleFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
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
                    supportFragmentManager.commit {
                        replace(R.id.main_fragment_container, ImageDetectionFragment())
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




    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PLACE_PICKER_REQUEST){
//            if (resultCode == RESULT_OK){
//                val place = PlacePicker.getPlace(data,this)
//                print(place.name)
//            }
//        }
//    }
}