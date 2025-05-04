package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

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
    }
}
