package com.bingo.gobin.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bingo.gobin.R
import com.bingo.gobin.databinding.ActivitySettingBinding
import com.bingo.gobin.databinding.RowSettingProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val name = intent.getStringExtra("name")
        val mAdapter = SettingAdapter(name)
        with(binding) {
            rvSetting.apply {
                layoutManager = LinearLayoutManager(this@SettingActivity)
                adapter = mAdapter
            }
            btnBackSetting.setOnClickListener {
                finish()
            }
        }
        mAdapter.onItemClick = {
            when (it.id) {
                1 -> {
                    Toast.makeText(this, "Under Maintenance", Toast.LENGTH_SHORT).show()
                }
                2 -> {
                    Toast.makeText(this, "Under Maintenance", Toast.LENGTH_SHORT).show()
                }
                3 -> {
                    Firebase.auth.signOut()
                    if (Firebase.auth.currentUser == null){
                        finish()
                    }else{
                        Toast.makeText(this, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }

    inner class SettingAdapter(name :String?) : RecyclerView.Adapter<SettingAdapter.SettingViewHolder>() {
        val settings = arrayListOf<Setting>(
            Setting(
                id = 1,
                title = name?:"Profile",
                subtitle = "Edit Profile",
                img = R.drawable.ic_user,
            ),
            Setting(
                id = 2,
                title = "History",
                subtitle = "Show Last Schedule Completed",
                img = R.drawable.ic_baseline_history_24
            ),
            Setting(
                id = 3,
                title = "Sign Out",
                subtitle = "",
                img = R.drawable.ic_logout,
            ),
        )

        inner class SettingViewHolder(val binding: RowSettingProfileBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun binds(setting: Setting) {
                with(binding) {
                    titleSetting.text = setting.title
                    subtitleSetting.text = setting.subtitle
                    imgLogoSetting.load(setting.img)
                }
            }
        }

        var onItemClick: ((Setting) -> Unit)? = null
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingViewHolder =
            SettingViewHolder(
                RowSettingProfileBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        override fun onBindViewHolder(holder: SettingViewHolder, position: Int) {
            val data = settings[position]
            holder.binding.root.setOnClickListener { onItemClick?.invoke(data) }
            holder.binds(data)
        }

        override fun getItemCount(): Int = settings.size
    }

    data class Setting(
        val id: Int,
        var title: String?,
        var subtitle: String,
        var img: Int,
    )
}