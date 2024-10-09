package com.example.istjobsportal

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.istjobsportal.nav.NavGraph
import com.example.istjobsportal.ui.theme.ISTJOBAPP
import com.example.istjobsportal.utils.JobApplicationModel
import com.example.istjobsportal.utils.NotificationViewModel
import com.example.istjobsportal.utils.ProfileViewModel
import com.example.istjobsportal.utils.SharedViewModel
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

// MainActivity: The entry point of the app.
class MainActivity : ComponentActivity() {

    // Firebase Authentication instance, used for managing user authentication.
    private lateinit var auth: FirebaseAuth
    // Navigation controller, responsible for managing navigation between screens.
    private lateinit var navController: NavHostController

    // ViewModels used to share data across different screens.
    private val sharedViewModel: SharedViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val jobApplicationModel: JobApplicationModel by viewModels()
    private val notificationViewModel: NotificationViewModel by viewModels()

    // onCreate: This method runs when the activity is created.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase App for the app to access Firebase services.
        FirebaseApp.initializeApp(this)

        // Initialize Firebase App Check for app verification in debug mode (for testing).
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance()
        // Note: This is for testing. In production, use SafetyNetAppCheckProviderFactory.
        )

        // Enable edge-to-edge display mode (makes the app content extend into system UI).
        enableEdgeToEdge()

        // Set the content of the activity using Jetpack Compose

        setContent {
            // Apply a custom theme to the app's UI.
            ISTJOBAPP {
                // Create a Surface container with the entire screen's dimensions.
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.onPrimary
                ) {
                    // Initialize the NavController for navigating between screens.
                    navController = rememberNavController()

                    // Set up the navigation graph to manage composable screens.
                    NavGraph(
                        navController = navController,
                        sharedViewModel = sharedViewModel,
                        profileViewModel = profileViewModel,
                        jobApplicationModel = jobApplicationModel,
                        notificationViewModel = notificationViewModel
                    )
//                    NotificationScreen()
                }
            }
        }

        // Retrieve Firebase Cloud Messaging (FCM) token for push notifications.
        retrieveFCMToken()

    }

    // This function retrieves the FCM Token from Firebase
    private fun retrieveFCMToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // If successful, log and use the FCM token (e.g., send it to the server).
                    val token = task.result
                    Log.d("FCM Token", "FCM Token: $token")
                    // Handle token (e.g., send it to server)
                } else {
                    // If the token retrieval fails, log the error.
                    Log.e("FCM Token", "Fetching FCM token failed", task.exception)
                }
            }
    }
}
