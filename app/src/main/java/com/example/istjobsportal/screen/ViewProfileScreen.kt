package com.example.istjobsportal.screen

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.istjobsportal.R
import com.example.istjobsportal.nav.Screens
import com.example.istjobsportal.utils.AlumniProfileData
import com.example.istjobsportal.utils.ProfileViewModel


@Composable
fun ViewProfileScreen(navController: NavController, profileViewModel: ProfileViewModel) {
    val context = LocalContext.current
    val profileData = remember { mutableStateOf<AlumniProfileData?>(null) }
    val profilePhotoUrl = remember { mutableStateOf<String?>(null) }
    val loading = remember { mutableStateOf(true) }
    val isProfileLoaded = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf<String?>(null) }

    // Fetch the profile photo separately
    LaunchedEffect(Unit) {
        profileViewModel.retrieveProfilePhoto(
            onLoading = { loading.value = it },
            onSuccess = { url ->
                profilePhotoUrl.value = url
                Log.d("ViewProfileScreen", "Retrieved profile photo URL: $url")
            },
            onFailure = { error ->
                errorMessage.value = error
                Log.e("ViewProfileScreen", "Error retrieving profile photo: $error")
            }
        )
    }

    // Fetch the profile data
    LaunchedEffect(Unit) {
        profileViewModel.retrieveCurrentUserProfile(
            context = context,
            onLoading = { loading.value = it },
            onSuccess = { profile ->
                profileData.value = profile
                isProfileLoaded.value = true
                if (profile != null) {
                    Log.d("ViewProfileScreen", "Retrieved profile data")
                }
            },
            onFailure = { error ->
                errorMessage.value = error
                isProfileLoaded.value = true
                Log.e("ViewProfileScreen", "Error retrieving profile: $error")
            }
        )
    }

    // Main Layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        // Add back button
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        // Main content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(1.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                loading.value -> {
                    // Show loading indicator
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 4.dp,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Please wait...",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                errorMessage.value != null -> {
                    // Show error message
                    Text(
                        text = "Error: ${errorMessage.value}",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                isProfileLoaded.value && profileData.value == null -> {
                    // Show "No profile data available" only after loading is complete
                    Text(
                        text = "No profile data available.",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {





                    // Show the profile details
                    profileData.value?.let { profile ->
                        ProfileDetails(profile, profilePhotoUrl.value,navController)
                    }
                }
            }
        }
    }
}


@Composable
fun ProfileDetails(profile: AlumniProfileData, profilePhotoUrl: String?, navController: NavController) {
    var isLoading by remember { mutableStateOf(false) }  // Loading state for the button click
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // If the loading state is true, display the CircularProgressIndicator
        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading profile...",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            Button(onClick = {
                isLoading = false // Set loading to true when user clicks the button
                // Simulate delay or actual profile load before navigation
                // You can add a delay here or make sure any required data is fetched

                navController.navigate(Screens.EditProfileScreen.route)

                isLoading = false // Set to false after navigation (optional, depending on flow)
            }) {
                Text(text = "Edit Profile")
            }

            // Other Profile Details UI below the button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                ProfileImage(profile.profilePhotoUri ?: "")
            }

            // Display profile information
            Text(
                text = profile.fullName,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = profile.currentJob,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = profile.location, style = MaterialTheme.typography.bodyMedium)
            }

            // Rest of the profile sections (Contact, Education, Skills)
            HorizontalDivider()
            SectionHeader("Contact Information")
            ContactInfoSection(profile)
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            SectionHeader("Education")
            EducationSection(profile)
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            SectionHeader("Skills")
            SkillsSection(profile.skills)
        }
    }
}


@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun ContactInfoSection(profile: AlumniProfileData) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (profile.email.isNotBlank()) {
            InfoRow(Icons.Default.Email, "Email", profile.email)
        }
        if (profile.phone.isNotBlank()) {
            InfoRow(Icons.Default.Phone, "Phone", profile.phone)
        }
        if (profile.linkedIn.isNotBlank()) {
            LinkedInRow(profile.linkedIn)
        }
    }
}

@Composable
fun EducationSection(profile: AlumniProfileData) {
    Column(modifier = Modifier.fillMaxWidth()) {
        InfoRow(
            Icons.Default.Star,
            "Degree",
            profile.degree.name.replace("_", " ") + (profile.customDegree?.let { " ($it)" } ?: "")
        )
        InfoRow(Icons.Default.DateRange, "Graduation Year", profile.graduationYear)
        if (profile.extraCourse.isNotBlank()) {
            InfoRow(Icons.Default.Add, "Extra Course", profile.extraCourse)
        }
    }
}


@Composable
fun LinkedInRow(linkedInUrl: String) {
    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable {
                // Open LinkedIn URL in browser
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkedInUrl))
                context.startActivity(intent)
            }
    ) {
        // Replace with your LinkedIn icon drawable
        Icon(
            painter = painterResource(id = R.drawable.linkedin_), // Your LinkedIn icon
            contentDescription = "LinkedIn",
            tint = Color.Unspecified, // Make sure the icon shows original colors
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = "LinkedIn",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = Color.Gray
            )
//            Text(
//                text = linkedInUrl,
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.primary // You can also make the text appear clickable
//            )
        }
    }
}

@Composable
fun SkillsSection(skills: List<String>) {
    if (skills.isNotEmpty()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            skills.forEach { skill ->
                Text(
                    text = skill,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    } else {
        Text(
            text = "No skills listed.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Icon(icon, contentDescription = label, tint = Color.Gray, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = Color.Gray
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ProfileImage(profileUrl: String) {
    // Load profile image with Coil, and handle empty URL case efficiently
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current)
            .data(profileUrl.ifBlank { R.drawable.placeholder }) // Handle empty URL properly
            .crossfade(true)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error)
            .build()
    )

    Image(
        painter = painter,
        contentDescription = "Profile Photo",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
    )
}
