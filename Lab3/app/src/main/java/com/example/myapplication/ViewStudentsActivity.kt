package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.myapplication.databinding.ActivityViewStudentsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ViewStudentsActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityViewStudentsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewStudentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listView: ListView = findViewById(R.id.studentsListView)

        val dbHandler = DatabaseHandler(this)
        val students = dbHandler.getAllStudents()

        val studentStrings = students.map { student ->
            "ID: ${student.id}, Курс: ${student.year}, Факультет: ${student.faculty}"
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, studentStrings)
        listView.adapter = adapter

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_view_students -> {
                    true
                }
                R.id.nav_add_student -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        bottomNav.selectedItemId = R.id.nav_view_students
    }
}