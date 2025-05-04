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
    private lateinit var courseGroup: RadioGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_groups, container, false)

        facultyGroup = view.findViewById(R.id.facultyGroup)
        courseGroup = view.findViewById(R.id.courseGroup)

        val okBtn: Button = view.findViewById(R.id.okBtn)

        okBtn.setOnClickListener {
            val selectedCourseId = courseGroup.checkedRadioButtonId
            val selectedCourseButton: RadioButton? = view.findViewById(selectedCourseId)
            val selectedCourse = selectedCourseButton?.text?.toString() ?: ""

            val selectedFacultyId = facultyGroup.checkedRadioButtonId
            val selectedFacultyButton: RadioButton? = view.findViewById(selectedFacultyId)
            val selectedFaculty = selectedFacultyButton?.text?.toString() ?: ""

            val infoFragment = parentFragmentManager.findFragmentById(R.id.fragment_info) as? InfoFragment
            infoFragment?.updateData(selectedCourse, selectedFaculty)
        }

        return view
    }

    fun clear() {
        courseGroup.clearCheck()
        facultyGroup.clearCheck()
    }
}
