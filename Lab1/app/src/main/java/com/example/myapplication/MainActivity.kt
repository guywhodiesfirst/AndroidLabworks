package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val facultyGroup = findViewById<RadioGroup>(R.id.facultyGroup)
        val courseGroup = findViewById<RadioGroup>(R.id.courseGroup)
        val btnOk = findViewById<Button>(R.id.btnOk)
        val btnCancel = findViewById<Button>(R.id.btnCancel)
        val resultText = findViewById<TextView>(R.id.resultText)

        btnOk.setOnClickListener {
            val selectedFacultyId = facultyGroup.checkedRadioButtonId
            val selectedCourseId = courseGroup.checkedRadioButtonId

            if (selectedCourseId == -1 || selectedFacultyId == -1) {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Оберіть факультет та рік навчання!")
                    .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss()}
                    .show()
            }
            else {
                val faculty = findViewById<RadioButton>(selectedFacultyId).text
                val course = findViewById<RadioButton>(selectedCourseId).text
                resultText.text = getString(R.string.selected_data, faculty, course)
            }

            btnCancel.setOnClickListener {
                facultyGroup.clearCheck()
                courseGroup.clearCheck()
                resultText.text = ""
            }
        }
    }
}