package com.example.myapplication

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Button
import android.widget.Toast

class GroupsFragment : Fragment() {
    private lateinit var facultyGroup: RadioGroup
    private lateinit var yearGroup: RadioGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_groups, container, false)

        facultyGroup = view.findViewById(R.id.facultyGroup)
        yearGroup = view.findViewById(R.id.yearGroup)

        val okBtn: Button = view.findViewById(R.id.okBtn)

        okBtn.setOnClickListener {
            val selectedYearId = yearGroup.checkedRadioButtonId
            val selectedYearButton: RadioButton? = view.findViewById(selectedYearId)
            val selectedYear = selectedYearButton?.text?.toString() ?: ""

            val selectedFacultyId = facultyGroup.checkedRadioButtonId
            val selectedFacultyButton: RadioButton? = view.findViewById(selectedFacultyId)
            val selectedFaculty = selectedFacultyButton?.text?.toString() ?: ""

            if (selectedFaculty.isEmpty() || selectedYear.isEmpty()) {
                Toast.makeText(requireContext(), "Будь ласка, оберіть курс та факультет", Toast.LENGTH_SHORT).show()
            }
            else {
                val infoFragment = parentFragmentManager.findFragmentById(R.id.fragment_info) as? InfoFragment
                infoFragment?.updateData(Student(selectedFaculty, selectedYear.toInt()))
                try {
                    insertDataToDb(selectedYear.toInt(), selectedFaculty)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Помилка: ${e.message}", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }

            }
        }

        return view
    }

    fun clear() {
        yearGroup.clearCheck()
        facultyGroup.clearCheck()
    }

    fun insertDataToDb(year: Int, faculty: String) {
        val dbHandler = DatabaseHandler(requireContext())
        val isInserted = dbHandler.insertStudent(Student(faculty, year))

        if (isInserted) {
            Toast.makeText(requireContext(), "Запис успішно збережено", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Помилка при збереженні", Toast.LENGTH_SHORT).show()
        }
    }
}