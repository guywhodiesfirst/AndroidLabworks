package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SharedViewModel : ViewModel() {
    private val _selectedCourse = MutableLiveData<String>()
    val selectedCourse: LiveData<String> = _selectedCourse

    private val _selectedFaculty = MutableLiveData<String>()
    val selectedFaculty: LiveData<String> = _selectedFaculty

    fun setSelection(course: String, faculty: String) {
        _selectedCourse.value = course
        _selectedFaculty.value = faculty
    }
}
