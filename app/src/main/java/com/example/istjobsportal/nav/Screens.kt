package com.example.istjobsportal.nav

sealed class Screens(val route:String)
{

//    NOTIFICATIONS SCREENS
    data object NotificationsScreen:Screens(route = "notifications")

//    profile screens
    data object ViewAlumniProfilesScreen:Screens(route = "view_profiles")
    data object CompleteProfileForm:Screens(route = "complete_profile")
    data object ViewProfileScreen:Screens(route = "view_profile")
    data object EditProfileScreen : Screens(route = "edit_profile")
    data object DisplayAlumniJobsScreen:Screens(route = "view_alumni_jobs")

//    job screens
    data object AddSkillScreen:Screens(route = "add_skill")
    data object DisplayJobScreen:Screens(route = "view_job")
    data object AddJobScreen:Screens(route = "add_job")
    data object EditJobScreen:Screens(route = "edit_job/{jobID}")

//    job application screens
    data object JobApplicationScreen:Screens(route = "job_application")
    data object DisplayApplicationScreen:Screens(route = "display_application{applicationId}")
    data object ViewApplicationsScreen:Screens(route = "display_applications")

//    authentication screens
    data object ForgotPasswordScreen:Screens(route = "forgot_password")
    data object ISTLoginScreen:Screens(route = "login_screen")
    data object ISTRegisterScreen:Screens(route = "register_screen")
    data object DashboardScreen:Screens(route = "dashboard_screen")
}