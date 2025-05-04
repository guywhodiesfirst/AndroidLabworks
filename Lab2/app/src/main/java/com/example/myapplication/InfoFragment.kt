package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class InfoFragment : Fragment() {

    private lateinit var resultText: TextView
    private lateinit var cancelBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_info, container, false)
        resultText = view.findViewById(R.id.resultText)

        cancelBtn = view.findViewById(R.id.cancelBtn)

        cancelBtn.setOnClickListener {
            this.resultText.text = ""
            val groupsFragment = parentFragmentManager.findFragmentById(R.id.fragment_groups) as? GroupsFragment
            groupsFragment?.clear()
        }
        return view
    }

    fun updateData(course: String, faculty: String) {
        if (faculty.isEmpty() || course.isEmpty()) {
            Toast.makeText(requireContext(), "Будь ласка, оберіть курс та факультет", Toast.LENGTH_SHORT).show()
            return
        }
        if (this::resultText.isInitialized) {
            resultText.text = buildString {
                append("Курс: ")
                append(course)
                append("\nФакультет: ")
                append(faculty)
            }
        }
    }

}

