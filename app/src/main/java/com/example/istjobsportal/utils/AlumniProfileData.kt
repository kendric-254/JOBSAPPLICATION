package com.example.istjobsportal.utils

enum class DegreeChoice {
    Degree_In_Software_Engineering,
    Degree_In_Computer_Science,
    Degree_In_Information_Technology,
}

//@Entity(tableName = "alumniProfiles")
data class AlumniProfileData(
    var profileID: String = "",
    var fullName: String = "",
    var email: String = "",
    var degree: DegreeChoice = DegreeChoice.Degree_In_Software_Engineering,
    var graduationYear: String = "",
    var extraCourse: String = "",
    var profilePhotoUri: String? = null,
    var currentJob: String = "",
    var currentEmployee: String = "",
    var location: String = "",
    var phone: String = "",
    var linkedIn: String = "",
    var customDegree: String? = null,// New field for custom degree input
    var skills: List<String> = emptyList(),
)
