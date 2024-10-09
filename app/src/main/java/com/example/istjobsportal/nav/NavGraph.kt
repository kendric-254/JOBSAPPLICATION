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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
        startDestination = Screens.ISTLoginScreen.route // The first screen when the app opens
    ) {
        // Notifications Screen
        composable(
            route = Screens.NotificationsScreen.route
        ) {
            NotificationScreen(
                notificationViewModel = notificationViewModel,
                navController = navController,
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
            val jobID = backStackEntry.arguments?.getString("jobID") ?: return@composable
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
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
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
            ViewApplicationScreen(
                navController = navController,
                applicationModel = jobApplicationModel,
                notificationViewModel
            )
        }

        // EditProfile screen
        composable(
            route = Screens.EditProfileScreen.route
        ) {
            EditProfileScreen(
                navController = navController,
                profileViewModel = profileViewModel,
                saveSkill = { skill, context ->
                    profileViewModel.saveSkill(skill, context)
                }
            )
        }

        // View Alumni Profiles Screen
        composable(
            route = Screens.ViewAlumniProfilesScreen.route
        ) {
            ViewAlumniProfilesScreen(
                navController = navController,
                profileViewModel = profileViewModel,
                notificationViewModel
            )
        }

        // View Profile Screen (No arguments)
        composable(
            route = Screens.ViewProfileScreen.route
        ) {
            ViewProfileScreen(navController = navController, profileViewModel = profileViewModel)
        }

        // Complete Profile Form Screen
        composable(
            route = Screens.CompleteProfileForm.route
        ) {
            val profileViewModel: ProfileViewModel = viewModel()
            var fullName by remember { mutableStateOf("") }
            var email by remember { mutableStateOf("") }
            var phone by remember { mutableStateOf("") }
            var linkedIn by remember { mutableStateOf("") }
            val profilePhoto: Uri? = null
            val context = LocalContext.current

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
                onSelectPhotoClick = { /* Handle photo selection */ },
                onSubmit = {
                    profileViewModel.saveAlumniProfile(
                        AlumniProfileData(
                            fullName = fullName,
                            email = email,
                            phone = phone,
                            linkedIn = linkedIn,
                            profileID = "", // Adjust this according to your model
                            profilePhotoUri = profilePhoto?.toString()
                        ),
                        profilePhotoUri = profilePhoto,
                        navController = navController,
                        context = context,
                        onComplete = {
                            navController.navigate(Screens.DashboardScreen.route) {
                                popUpTo(Screens.CompleteProfileForm.route) {
                                    inclusive = true
                                }
                            }
                        },
                        onError = { errorMessage ->
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                currentStep = 3,
                isLoading = false,
                onBackClick = { navController.popBackStack() }
            )
        }

        // Display Alumni Jobs Screen (No arguments)
        composable(
            route = Screens.DisplayAlumniJobsScreen.route
        ) {
            DisplayAlumniJobsScreen(
                navController = navController,
                sharedViewModel = sharedViewModel,
                profileViewModel = profileViewModel,
                notificationViewModel = notificationViewModel
            )
        }

        // Edit Job Screen with jobID argument
        composable(
            route = "edit_job/{jobID}",
            arguments = listOf(navArgument("jobID") { type = NavType.StringType })
        ) { backStackEntry ->
            val jobID = backStackEntry.arguments?.getString("jobID") ?: ""
            if (jobID.isNotEmpty()) {
                EditJobScreen(
                    navController = navController,
                    sharedViewModel = sharedViewModel,
                    jobID = jobID
                )
            } else {
                Text("Invalid Job ID", color = MaterialTheme.colorScheme.error)
            }
        }

        // Add Job Screen
        composable(
            route = Screens.AddJobScreen.route
        ) {
            AddJobScreen(navController = navController, sharedViewModel = sharedViewModel)
        }

        // Display Job Screen
        composable(
            route = Screens.DisplayJobScreen.route
        ) {
            DisplayJobScreen(
                navController = navController,
                sharedViewModel = sharedViewModel,
                profileViewModel = profileViewModel,
                notificationViewModel = notificationViewModel
            )
        }

        // Skill Screen (for adding skills)
        composable(
            route = Screens.AddSkillScreen.route
        ) {
            SkillScreen(navController = navController, sharedViewModel = sharedViewModel)
        }

        // Forgot Password Screen
        composable(
            route = Screens.ForgotPasswordScreen.route
        ) {
            ForgotPasswordScreen(navController = navController)
        }

        // Register Screen
        composable(
            route = Screens.ISTRegisterScreen.route
        ) {
            ISTRegisterScreen(navController = navController)
        }

        // Login Screen
        composable(
            route = Screens.ISTLoginScreen.route
        ) {
            ISTLoginScreen(navController = navController)
        }

        // Dashboard Screen
        composable(
            route = Screens.DashboardScreen.route
        ) {
            DashboardScreen(
                navController = navController,
                profileViewModel = profileViewModel,
                notificationViewModel = notificationViewModel
            )
        }
    }
}
