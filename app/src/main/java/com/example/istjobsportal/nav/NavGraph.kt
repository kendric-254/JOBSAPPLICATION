package com.example.istjobsportal.nav

import android.net.Uri
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.compose.ui.platform.LocalContext
import com.example.istjobsportal.screen.AddJobScreen
import com.example.istjobsportal.screen.SkillScreen
import com.example.istjobsportal.screen.CompleteProfileForm
import com.example.istjobsportal.screen.DashboardScreen
import com.example.istjobsportal.screen.DisplayAlumniJobsScreen
import com.example.istjobsportal.screen.DisplayApplicationScreen
import com.example.istjobsportal.screen.DisplayJobScreen
import com.example.istjobsportal.screen.EditJobScreen
import com.example.istjobsportal.screen.EditProfileScreen
import com.example.istjobsportal.screen.ForgotPasswordScreen
import com.example.istjobsportal.screen.ISTLoginScreen
import com.example.istjobsportal.screen.ISTRegisterScreen
import com.example.istjobsportal.screen.JobApplicationScreen
import com.example.istjobsportal.screen.NotificationScreen
import com.example.istjobsportal.screen.ViewAlumniProfilesScreen
import com.example.istjobsportal.screen.ViewApplicationScreen
import com.example.istjobsportal.screen.ViewProfileScreen
import com.example.istjobsportal.utils.AlumniProfileData
import com.example.istjobsportal.utils.JobApplicationModel
import com.example.istjobsportal.utils.NotificationViewModel
import com.example.istjobsportal.utils.ProfileViewModel
import com.example.istjobsportal.utils.SharedViewModel

// NavGraph function defines the navigation routes and screens in the app.
@Composable
fun NavGraph(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    profileViewModel: ProfileViewModel,
    jobApplicationModel: JobApplicationModel,
    notificationViewModel: NotificationViewModel
) {
    // NavHost defines the navigation graph with the starting screen.
    NavHost(
        navController = navController,
        startDestination = Screens.ISTLoginScreen.route// The first screen when the app opens
    ) {


        // Notifications Screen

        composable(
            route = Screens.NotificationsScreen.route
        ) {
            // Navigate to NotificationScreen
            NotificationScreen(
                notificationViewModel = notificationViewModel,
                navController = navController ,
                sharedViewModel = sharedViewModel,
                profileViewModel = profileViewModel
            )
        }

// Job Application Screen with jobID as an argument
        composable(
            route = Screens.JobApplicationScreen.route + "/{jobID}", // URL-like route with jobID as a parameter
            arguments = listOf(
                navArgument("jobID") { type = NavType.StringType } // jobID is passed as a String argument
            )
        ) { backStackEntry ->
            // Extract jobID from the backStackEntry
            val jobID = backStackEntry.arguments?.getString("jobID") ?: return@composable

            // Navigate to JobApplicationScreen and pass the jobID
            JobApplicationScreen(
                navController = navController,
                jobID = jobID, // Pass the jobID to the screen
                jobApplicationModel = jobApplicationModel
            )
        }

// View Applications Screen, accepts a userId argument
        composable(
            route = "${Screens.DisplayApplicationScreen.route}/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            // Retrieve the userId from the backStack arguments
            val userId = backStackEntry.arguments?.getString("userId") ?: ""

            // Navigate to DisplayApplicationScreen and pass the userId
            DisplayApplicationScreen(
                navController = navController,
                jobApplicationModel = viewModel(),
                profileViewModel,
                sharedViewModel,
                notificationViewModel,
                userId = userId // Pass userId to fetch applications
            )
        }

        // View Application Screen (No arguments)
        composable(
            route = Screens.ViewApplicationsScreen.route
        ) {
            // Navigate to ViewApplicationScreen
            ViewApplicationScreen(
                navController = navController,
                applicationModel = jobApplicationModel,
                notificationViewModel
            )
        }

      //EditProfile screen
        composable(
            route = Screens.EditProfileScreen.route
        ) {
            EditProfileScreen(
                navController = navController,
                profileViewModel = profileViewModel,
                saveSkill = { skill, context ->
                    profileViewModel.saveSkill(skill, context)  // Save a skill when the user edits their profile

                }
            )
        }

        // View Alumni Profiles Screen
        composable(
            route = Screens.ViewAlumniProfilesScreen.route
        ) {
            // Navigate to ViewAlumniProfilesScreen
            ViewAlumniProfilesScreen(
                navController = navController,
                profileViewModel = profileViewModel,
                notificationViewModel
            )
        }
        //view profile Screen (No arguments)
        composable(
            route = Screens.ViewProfileScreen.route
        ) {
            // Navigate to ViewProfileScreen

            ViewProfileScreen(navController = navController, profileViewModel = profileViewModel)
        }
        composable(
            route = Screens.CompleteProfileForm.route
        ) {
            // Retrieve the ProfileViewModel using Hilt or regular ViewModel
            val profileViewModel: ProfileViewModel = viewModel()

            // State variables for the form fields
            var fullName by remember { mutableStateOf("") }
            var email by remember { mutableStateOf("") }
            var phone by remember { mutableStateOf("") }
            var linkedIn by remember { mutableStateOf("") }
            val profilePhoto: Uri? = null

            // Get the current context using LocalContext
            val context = LocalContext.current


            // CompleteProfileForm Composable
            CompleteProfileForm(
                fullName = fullName,
                onFullNameChange = { fullName = it },
                email = email,
                onEmailChange = { email = it },
                phone = phone,
                onPhoneChange = { phone = it },
                linkedIn = linkedIn,
                onLinkedInChange = { linkedIn = it },
                profilePhoto = profilePhoto,
                onSelectPhotoClick = {
                    // Implement photo selection logic
                },
                onSubmit = {
                    // Save profile using the ProfileViewModel
                    profileViewModel.saveAlumniProfile(
                        AlumniProfileData(
                            fullName = fullName,
                            email = email,
//                            showLogo:Boolean,
                            phone = phone,
                            linkedIn = linkedIn,
                            profileID = "", // Adjust this according to your model
                            profilePhotoUri = profilePhoto?.toString()
                        ),
                        profilePhotoUri = profilePhoto,
                        navController = navController,
                        context = context,  // Pass the context appropriately here
                        onComplete = {
                            // On successful save, navigate to the next screen
                            navController.navigate(Screens.DashboardScreen.route) {
                                popUpTo(Screens.CompleteProfileForm.route) {
                                    inclusive = true
                                }
                            }
                        },
                        onError = { errorMessage ->
                            // Handle error scenario
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            )
        }

//        JOB SCREENS-Display Alumni Jobs Screen (No arguments)

        composable(
            route = Screens.DisplayAlumniJobsScreen.route
        ) {
            // Navigate to DisplayAlumniJobsScreen
            DisplayAlumniJobsScreen(
                navController = navController,
                sharedViewModel = sharedViewModel,
                profileViewModel = profileViewModel,
                notificationViewModel = notificationViewModel
            )
        }

        // Edit Job Screen with jobID argument
        composable(
            route = "edit_job/{jobID}",// URL-like route with jobID as a parameter
            arguments = listOf(navArgument("jobID") { type = NavType.StringType })
        ) { backStackEntry ->
            // Retrieve the jobID argument from the back stack
            val jobID = backStackEntry.arguments?.getString("jobID") ?: ""
            if (jobID.isNotEmpty()) {
                //Navigate to EditJobScreen and pass jobID
                EditJobScreen(
                    navController = navController,
                    sharedViewModel = sharedViewModel,
                    jobID = jobID
                )
            } else {
                // Display error message for invalid jobID
                Text("Invalid Job ID", color = MaterialTheme.colorScheme.error)
            }
        }

// Add Job Screen
        composable(
            route = Screens.AddJobScreen.route
        ) {
            // Navigate to AddJobScreen
            AddJobScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        // Display Job Screen
        composable(
            route = Screens.DisplayJobScreen.route
        ) {
            // Navigate to DisplayJobScreen
            DisplayJobScreen(
                navController = navController,
                sharedViewModel = sharedViewModel,
                profileViewModel = profileViewModel,
                notificationViewModel
            )
        }
// Skill Screen (for adding skills)
        composable(
            route = Screens.AddSkillScreen.route
        ) {
            //Navigate to SkillScreen
            SkillScreen(navController = navController, sharedViewModel = sharedViewModel)
        }


//        AUTHENTICATION SCREENS

        // Forgot Password Screen
        composable(
            route = Screens.ForgotPasswordScreen.route
        ) {
            // Navigate to ForgotPasswordScreen
            ForgotPasswordScreen(navController = navController)
        }
        // Register Screen
        composable(
            route = Screens.ISTRegisterScreen.route
        ) {
            // Navigate to ISTRegisterScreen
            ISTRegisterScreen(navController = navController)
        }
//        login screen
        composable(
            route = Screens.ISTLoginScreen.route
        ) {
            ISTLoginScreen(navController = navController)
        }
//        dashboard screen
        composable(
            route = Screens.DashboardScreen.route
        ) {
            DashboardScreen(
                navController = navController,
                profileViewModel = profileViewModel,
                notificationViewModel
            )
        }


    }
}

