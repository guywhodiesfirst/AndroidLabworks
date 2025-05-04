package com.example.myapplication

class Student {
    var id: Int = 0
    var faculty: String = ""
    var year: Int = 1

    constructor(faculty: String, year: Int) {
        this.faculty = faculty
        this.year = year
    }

    constructor(id: Int, faculty: String, year: Int) {
        this.id = id
        this.faculty = faculty
        this.year = year
    }
}