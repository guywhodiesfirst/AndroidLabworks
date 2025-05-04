package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Button
import androidx.fragment.app.Fragment

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

            val infoFragment = parentFragmentManager.findFragmentById(R.id.fragment_info) as? InfoFragment
            infoFragment?.updateData(selectedYear, selectedFaculty)
        }

        return view
    }

    fun clear() {
        yearGroup.clearCheck()
        facultyGroup.clearCheck()
    }
}
