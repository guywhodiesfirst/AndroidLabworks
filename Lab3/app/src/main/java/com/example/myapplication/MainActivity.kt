package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val fragmentGroups = GroupsFragment()
            val fragmentInfo = InfoFragment()

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_groups, fragmentGroups)
                .replace(R.id.fragment_info, fragmentInfo)
                .commit()
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_add_student -> {
                    true
                }
                R.id.nav_view_students -> {
                    val intent = Intent(this, ViewStudentsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        bottomNav.selectedItemId = R.id.nav_add_student
    }
}