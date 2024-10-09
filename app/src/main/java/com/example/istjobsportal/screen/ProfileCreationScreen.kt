//package com.example.alumni_system.screen
//
//import android.net.Uri
//import android.widget.Toast
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.runtime.Composable
//import androidx.navigation.NavController
//import androidx.compose.animation.*
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.text.style.TextAlign
//import androidx.lifecycle.viewmodel.compose.viewModel
//import coil.compose.rememberAsyncImagePainter
//import com.example.alumni_system.R
//import com.example.alumni_system.utils.ProfileData
//import com.example.alumni_system.utils.ProfileViewModel
//import com.example.alumni_system.utils.SkillData
//import com.example.istjobsportal.utils.ProfileViewModel
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.coroutines.delay
//
//@Composable
//fun ProfileCreationScreen(
//    navController: NavController,
//    profileViewModel: ProfileViewModel,
//    viewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory),
//) {
//    val auth = FirebaseAuth.getInstance()
//    val user = auth.currentUser
//    val isEmailVerified = user?.isEmailVerified ?: false
//    val context = LocalContext.current
//    val skillsCollection = remember { mutableStateListOf<SkillData>() }
//    val firestore = FirebaseFirestore.getInstance()
//    var isLoading by remember { mutableStateOf(false) }
//
//    var profileData by remember { mutableStateOf(ProfileData(userID = user?.uid ?: "")) }
//    var currentStep by remember { mutableIntStateOf(1) }
//
//    LaunchedEffect(Unit) {
//        firestore.collection("skills").get().addOnSuccessListener { querySnapshot ->
//            for (document in querySnapshot.documents) {
//                val skill = document.toObject(SkillData::class.java)
//                if (skill != null) {
//                    skillsCollection.add(skill)
//                }
//            }
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth(),
//            contentAlignment = Alignment.Center
//        ) {
//            Image(
//                modifier = Modifier
//                    .size(250.dp)
//                    .padding(top = 32.dp)
//                    .clip(RoundedCornerShape(56.dp)),
//                painter = painterResource(id = R.drawable.ist_logo),
//                contentDescription = "Profile_Creation"
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Display the progress bar with step indication
//        StepProgressBar(currentStep)
//
//        Spacer(modifier = Modifier.height(2.dp))
//
//        // Display the multi-step form with animated transitions
//        MultiStepProfileForm(
//            currentStep = currentStep,
//            onNextClick = { currentStep++ },
//            onBackClick = { currentStep-- },
//            onFinishClick = {
//                isLoading = true
//                viewModel.saveProfile(profileData) { success ->
//                    isLoading = false // Reset loading state after save attempt
//                    if (success) {
//                        navController.navigate("dashboard_screen")
//                    } else {
//                        Toast.makeText(
//                            context,
//                            "Failed to save profile. Please try again.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//            },
//            isEmailVerified = isEmailVerified,
//            profileData = profileData,
//            onProfileDataChange = { newProfileData -> profileData = newProfileData },
//            skillsCollection = skillsCollection,
//            user = user,
//            isLoading = isLoading
//        )
//    }
//}
//
//@Composable
//fun MultiStepProfileForm(
//    currentStep: Int,
//    onNextClick: () -> Unit,
//    onBackClick: () -> Unit,
//    onFinishClick: () -> Unit,
//    isEmailVerified: Boolean,
//    profileData: ProfileData,
//    onProfileDataChange: (ProfileData) -> Unit,
//    skillsCollection: List<SkillData>,
//    user: FirebaseUser?,
//    isLoading: Boolean
//) {
//    // Use AnimatedVisibility for smooth transitions between steps
//    Box(Modifier.fillMaxSize()) {
//        AnimatedVisibility(
//            visible = currentStep == 1,
//            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
//            exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
//        ) {
//            ProfileFormStep1(
//                onNextClick = onNextClick,
//                profileData = profileData,
//                onProfileDataChange = onProfileDataChange)
//        }
//
//        AnimatedVisibility(
//            visible = currentStep == 2,
//            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
//            exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
//        ) {
//            ProfileFormStep2(
//                onNextClick = onNextClick,
//                onBackClick = onBackClick,
//                profileData = profileData,
//                onProfileDataChange = onProfileDataChange)
//        }
//
//        AnimatedVisibility(
//            visible = currentStep == 3,
//            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
//            exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
//        ) {
//            ProfileFormStep3(
//                onNextClick = onNextClick,
//                onBackClick = onBackClick,
//                profileData = profileData,
//                onProfileDataChange = onProfileDataChange)
//        }
//
//        AnimatedVisibility(
//            visible = currentStep == 4,
//            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
//            exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
//        ) {
//            ProfileFormStep4(
//                onNextClick = onNextClick,
//                onBackClick = onBackClick,
//                profileData = profileData,
//                onProfileDataChange = onProfileDataChange)
//        }
//
//        AnimatedVisibility(
//            visible = currentStep == 5,
//            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
//            exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
//        ) {
//            ProfileFormStep5(
//                onNextClick = onNextClick,
//                onBackClick = onBackClick,
//                user = user
//            )
//        }
//
//        AnimatedVisibility(
//            visible = currentStep == 6,
//            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
//            exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
//        ) {
//            ProfileFormStep6(
//                onNextClick = onNextClick,
//                onBackClick = onBackClick,
//                profileData = profileData,
//                onProfileDataChange = onProfileDataChange,
//                allSkills = skillsCollection,
//            )
//        }
//
//        AnimatedVisibility(
//            visible = currentStep == 7,
//            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
//            exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
//        ) {
//            ProfileFormStep7(onFinishClick = onFinishClick, isLoading = isLoading)
//        }
//    }
//}
//
//// Step 1: Personal Info
//@Composable
//fun ProfileFormStep1(
//    onNextClick: () -> Unit,
//    profileData: ProfileData,
//    onProfileDataChange: (ProfileData) -> Unit
//) {
//    // State for managing validation errors
//    var errorMessage by remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(text = "Personal Info", style = MaterialTheme.typography.headlineMedium)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        OutlinedTextField(
//            value = profileData.firstName,
//            onValueChange = {
//                onProfileDataChange(profileData.copy(firstName = it))
//                if (errorMessage.isNotEmpty()) errorMessage = "" // Clear error if user is typing
//            },
//            label = { Text("First Name") },
//            modifier = Modifier.fillMaxWidth(),
//            keyboardOptions = KeyboardOptions.Default.copy(
//                imeAction = ImeAction.Next
//            ),
//            isError = profileData.firstName.isBlank() && errorMessage.isNotEmpty()
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        OutlinedTextField(
//            value = profileData.lastName,
//            onValueChange = {
//                onProfileDataChange(profileData.copy(lastName = it))
//                if (errorMessage.isNotEmpty()) errorMessage = "" // Clear error if user is typing
//            },
//            label = { Text("Last Name") },
//            modifier = Modifier.fillMaxWidth(),
//            keyboardOptions = KeyboardOptions.Default.copy(
//                imeAction = ImeAction.Next
//            ),
//            isError = profileData.lastName.isBlank() && errorMessage.isNotEmpty()
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        OutlinedTextField(
//            value = profileData.phoneNumber,
//            onValueChange = {
//                onProfileDataChange(profileData.copy(phoneNumber = it))
//                if (errorMessage.isNotEmpty()) errorMessage = "" // Clear error if user is typing
//            },
//            label = { Text("Phone Number") },
//            modifier = Modifier.fillMaxWidth(),
//            keyboardOptions = KeyboardOptions.Default.copy(
//                imeAction = ImeAction.Done,
//                keyboardType = KeyboardType.Phone
//            ),
//            isError = profileData.phoneNumber.isBlank() && errorMessage.isNotEmpty()
//        )
//
//        // Show error message
//        if (errorMessage.isNotEmpty()) {
//            Text(
//                text = errorMessage,
//                color = MaterialTheme.colorScheme.error,
//                style = MaterialTheme.typography.bodySmall,
//                modifier = Modifier.padding(top = 8.dp)
//            )
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        Button(
//            onClick = {
//                // Validate fields before proceeding
//                if (profileData.firstName.isBlank() || profileData.lastName.isBlank() || profileData.phoneNumber.isBlank()) {
//                    errorMessage = "Please fill all fields."
//                } else {
//                    onNextClick()
//                }
//            },
//            modifier = Modifier.fillMaxWidth(),
//            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
//        ) {
//            Text("Next")
//        }
//    }
//}
//
//// Step 2: Location
//@Composable
//fun ProfileFormStep2(
//    onNextClick: () -> Unit,
//    onBackClick: () -> Unit,
//    profileData: ProfileData,
//    onProfileDataChange: (ProfileData) -> Unit
//) {
//    // State for managing validation errors
//    var errorMessage by remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(text = "Location", style = MaterialTheme.typography.headlineMedium)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        OutlinedTextField(
//            value = profileData.location,
//            onValueChange = {
//                onProfileDataChange(profileData.copy(location = it))
//                if (errorMessage.isNotEmpty()) errorMessage = "" // Clear error if user is typing
//            },
//            label = { Text("Location") },
//            modifier = Modifier.fillMaxWidth(),
//            keyboardOptions = KeyboardOptions.Default.copy(
//                imeAction = ImeAction.Done
//            ),
//            isError = profileData.location.isBlank() && errorMessage.isNotEmpty()
//        )
//
//        // Show error message
//        if (errorMessage.isNotEmpty()) {
//            Text(
//                text = errorMessage,
//                color = MaterialTheme.colorScheme.error,
//                style = MaterialTheme.typography.bodySmall,
//                modifier = Modifier.padding(top = 8.dp)
//            )
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            // Back Button
//            OutlinedButton(
//                onClick = { onBackClick() },
//                modifier = Modifier.weight(1f)
//            ) {
//                Text("Back")
//            }
//
//            Spacer(modifier = Modifier.width(16.dp))
//
//            // Next Button
//            Button(
//                onClick = {
//                    // Validate fields before proceeding
//                    if (profileData.location.isBlank()) {
//                        errorMessage = "Please enter your location."
//                    } else {
//                        onNextClick()
//                    }
//                },
//                modifier = Modifier.weight(1f),
//                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
//            ) {
//                Text("Next")
//            }
//        }
//    }
//}
//
//// Step 3: Education
//@Composable
//fun ProfileFormStep3(
//    onNextClick: () -> Unit,
//    onBackClick: () -> Unit,
//    profileData: ProfileData,
//    onProfileDataChange: (ProfileData) -> Unit
//) {
//    var school by remember { mutableStateOf(profileData.school) }
//    var startYear by remember { mutableStateOf(profileData.startYear) }
//    var endYear by remember { mutableStateOf(profileData.endYear) }
//    var errorMessage by remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(text = "Education", style = MaterialTheme.typography.headlineMedium)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        OutlinedTextField(
//            value = school,
//            onValueChange = {
//                school = it
//                onProfileDataChange(profileData.copy(school = school))
//                if (errorMessage.isNotEmpty()) errorMessage = "" // Clear error if user is typing
//            },
//            label = { Text("School") },
//            modifier = Modifier.fillMaxWidth(),
//            keyboardOptions = KeyboardOptions.Default.copy(
//                imeAction = ImeAction.Next
//            ),
//            isError = school.isBlank() && errorMessage.isNotEmpty()
//        )
//
//        // Show error message for the school field
//        if (errorMessage.isNotEmpty() && school.isBlank()) {
//            Text(
//                text = errorMessage,
//                color = MaterialTheme.colorScheme.error,
//                style = MaterialTheme.typography.bodySmall,
//                modifier = Modifier.padding(top = 8.dp)
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Start Year and End Year Input Fields
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//        ) {
//            OutlinedTextField(
//                value = startYear,
//                onValueChange = {
//                    startYear = it
//                    onProfileDataChange(profileData.copy(startYear = startYear))
//                },
//                label = { Text("Start Year") },
//                modifier = Modifier.weight(1f),
//                keyboardOptions = KeyboardOptions.Default.copy(
//                    keyboardType = KeyboardType.Number,
//                    imeAction = ImeAction.Next
//                ),
//                isError = startYear.toIntOrNull() == null && errorMessage.isNotEmpty()
//            )
//
//            Spacer(modifier = Modifier.width(16.dp))
//
//            OutlinedTextField(
//                value = endYear,
//                onValueChange = {
//                    endYear = it
//                    onProfileDataChange(profileData.copy(endYear = endYear))
//                },
//                label = { Text("End Year") },
//                modifier = Modifier.weight(1f),
//                keyboardOptions = KeyboardOptions.Default.copy(
//                    keyboardType = KeyboardType.Number,
//                    imeAction = ImeAction.Done
//                ),
//                isError = endYear.toIntOrNull() == null && errorMessage.isNotEmpty()
//            )
//        }
//
//        // Show error messages for the year fields
//        if (errorMessage.isNotEmpty()) {
//            if (startYear.toIntOrNull() == null) {
//                Text(
//                    text = "Please enter a valid start year.",
//                    color = MaterialTheme.colorScheme.error,
//                    style = MaterialTheme.typography.bodySmall,
//                    modifier = Modifier.padding(top = 8.dp)
//                )
//            }
//            if (endYear.toIntOrNull() == null) {
//                Text(
//                    text = "Please enter a valid end year.",
//                    color = MaterialTheme.colorScheme.error,
//                    style = MaterialTheme.typography.bodySmall,
//                    modifier = Modifier.padding(top = 8.dp)
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // Back and Next Buttons
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            OutlinedButton(
//                onClick = { onBackClick() },
//                modifier = Modifier.weight(1f)
//            ) {
//                Text("Back")
//            }
//
//            Spacer(modifier = Modifier.width(16.dp))
//
//            Button(
//                onClick = {
//                    // Validate fields before proceeding
//                    when {
//                        school.isBlank() -> {
//                            errorMessage = "Please enter your school."
//                        }
//                        startYear.toIntOrNull() == null -> {
//                            errorMessage = "Please enter a valid start year."
//                        }
//                        endYear.toIntOrNull() == null -> {
//                            errorMessage = "Please enter a valid end year."
//                        }
//                        else -> {
//                            onNextClick()
//                        }
//                    }
//                },
//                modifier = Modifier.weight(1f),
//                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
//            ) {
//                Text("Next")
//            }
//        }
//    }
//}
//
//// Step 4: Profile Picture
//@Composable
//fun ProfileFormStep4(
//    onNextClick: () -> Unit,
//    onBackClick: () -> Unit,
//    profileData: ProfileData,
//    onProfileDataChange: (ProfileData) -> Unit
//) {
//    var selectedImageUri by remember { mutableStateOf<Uri?>(profileData.profilePictureUri?.let { Uri.parse(it) }) }
//
//    // Create an ActivityResultLauncher for picking an image
//    val imagePickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        if (uri != null) {
//            selectedImageUri = uri
//            // Update profileData with the new image URI as a String
//            onProfileDataChange(profileData.copy(profilePictureUri = uri.toString()))
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(text = "Profile Picture", style = MaterialTheme.typography.headlineMedium)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        if (selectedImageUri != null) {
//            Image(
//                painter = rememberAsyncImagePainter(selectedImageUri),
//                contentDescription = "Selected Profile Picture",
//                modifier = Modifier
//                    .size(150.dp)
//                    .clip(RoundedCornerShape(75.dp))
//            )
//        } else {
//            Box(
//                modifier = Modifier
//                    .size(150.dp)
//                    .clip(RoundedCornerShape(75.dp))
//                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(text = "No Image", style = MaterialTheme.typography.bodyMedium)
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(
//            onClick = {
//                imagePickerLauncher.launch("image/*")
//            }
//        ) {
//            Text("Select Profile Picture")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text(
//            text = "Skip for now",
//            modifier = Modifier.clickable {
//                onNextClick()
//            },
//            style = MaterialTheme.typography.bodyMedium,
//            color = MaterialTheme.colorScheme.onSurface
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            OutlinedButton(
//                onClick = { onBackClick() },
//                modifier = Modifier.weight(1f)
//            ) {
//                Text("Back")
//            }
//
//            Spacer(modifier = Modifier.width(16.dp))
//
//            Button(
//                onClick = { onNextClick() },
//                modifier = Modifier.weight(1f),
//                enabled = selectedImageUri != null,
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = if (selectedImageUri != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
//                )
//            ) {
//                Text("Next")
//            }
//        }
//    }
//}
//
//// Step 5: Verify Email
//@Composable
//fun ProfileFormStep5(
//    onNextClick: () -> Unit,
//    onBackClick: () -> Unit,
//    user: FirebaseUser?
//) {
//    val isEmailVerified = remember { mutableStateOf(user?.isEmailVerified == true) }
//    val context = LocalContext.current
//
//    // Periodically check if the email is verified
//    LaunchedEffect(Unit) {
//        while (true) {
//            delay(1000)
//            user?.reload()
//            isEmailVerified.value = user?.isEmailVerified == true
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = "Verify Your Email",
//            style = MaterialTheme.typography.headlineLarge
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        if (isEmailVerified.value) {
//            Text(
//                text = "Your email has been verified! You may proceed.",
//                color = MaterialTheme.colorScheme.primary,
//                style = MaterialTheme.typography.bodyMedium
//            )
//        } else {
//            Text(
//                text = "Your email is not verified. Please verify your email before continuing.",
//                color = MaterialTheme.colorScheme.error,
//                style = MaterialTheme.typography.bodyMedium
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            OutlinedButton(
//                onClick = { onBackClick() },
//                modifier = Modifier.weight(1f)
//            ) {
//                Text("Back")
//            }
//
//            Spacer(modifier = Modifier.width(16.dp))
//
//            // Disable the "Next" button if the email is not verified
//            Button(
//                onClick = { onNextClick() },
//                modifier = Modifier.weight(1f),
//                enabled = isEmailVerified.value,
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = if (isEmailVerified.value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
//                )
//            ) {
//                Text("Next")
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Show "Resend Verification Email" only if the email is not verified
//        if (!isEmailVerified.value) {
//            Text(
//                text = "Resend Verification Email",
//                modifier = Modifier.clickable {
//                    user?.let {
//                        resendVerificationEmail(it) { _, message ->
//                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                },
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.onSurface
//            )
//        }
//    }
//}
//
//// Function to resend the verification email
//private fun resendVerificationEmail(user: FirebaseUser, callback: (Boolean, String) -> Unit) {
//    user.sendEmailVerification()
//        .addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                callback(true, "Verification email sent successfully!")
//            } else {
//                callback(false, "Failed to send verification email: ${task.exception?.message}")
//            }
//        }
//}
//
//// Step 6: Work experience and Skills
//@Composable
//fun ProfileFormStep6(
//    onNextClick: () -> Unit,
//    onBackClick: () -> Unit,
//    profileData: ProfileData,
//    allSkills: List<SkillData>,
//    onProfileDataChange: (ProfileData) -> Unit
//) {
//    var workExperience by remember { mutableStateOf(profileData.workExperience) }
//    val selectedSkills = remember { profileData.skills.toMutableList() }
//    var errorMessage by remember { mutableStateOf("") }
//    var showSkillsDialog by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(text = "Experience", style = MaterialTheme.typography.headlineMedium)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        OutlinedTextField(
//            value = workExperience,
//            onValueChange = {
//                workExperience = it
//                onProfileDataChange(profileData.copy(workExperience = workExperience))
//                if (errorMessage.isNotEmpty()) errorMessage = "" // Clear error if user is typing
//            },
//            label = { Text("Work Experience") },
//            modifier = Modifier.fillMaxWidth(),
//            keyboardOptions = KeyboardOptions.Default.copy(
//                imeAction = ImeAction.Done
//            ),
//            isError = workExperience.isBlank() && errorMessage.isNotEmpty()
//        )
//
//        // Show error message for the work experience field
//        if (errorMessage.isNotEmpty() && workExperience.isBlank()) {
//            Text(
//                text = errorMessage,
//                color = MaterialTheme.colorScheme.error,
//                style = MaterialTheme.typography.bodySmall,
//                modifier = Modifier.padding(top = 8.dp)
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text(text = "Select Skills", style = MaterialTheme.typography.headlineMedium)
//
//        // List of Skills with Checkboxes
//        LazyColumn(
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            items(allSkills) { skill ->
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 4.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Checkbox(
//                        checked = selectedSkills.contains(skill),
//                        onCheckedChange = { isChecked ->
//                            if (isChecked) {
//                                selectedSkills.add(skill) // Add skill
//                            } else {
//                                selectedSkills.remove(skill) // Remove skill
//                            }
//                            onProfileDataChange(profileData.copy(skills = selectedSkills.toList())) // Update profile data
//                        }
//                    )
//                    Text(text = skill.skillName, modifier = Modifier.padding(start = 8.dp))
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // Back and Next Buttons
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            OutlinedButton(
//                onClick = { onBackClick() },
//                modifier = Modifier.weight(1f)
//            ) {
//                Text("Back")
//            }
//
//            Spacer(modifier = Modifier.width(16.dp))
//
//            Button(
//                onClick = {
//                    // Validate fields before proceeding
//                    when {
//                        workExperience.isBlank() -> {
//                            errorMessage = "Please enter your work experience."
//                        }
//                        selectedSkills.isEmpty() -> {
//                            errorMessage = "Please select at least one skill."
//                        }
//                        else -> {
//                            onNextClick()
//                        }
//                    }
//                },
//                modifier = Modifier.weight(1f),
//                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
//            ) {
//                Text("Next")
//            }
//        }
//    }
//}
//
////@Composable
////fun SkillsDialog(
////    allSkills: List<SkillData>,
////    selectedSkills: List<SkillData>,
////    onDismiss: () -> Unit,
////    onSelectSkills: (List<SkillData>) -> Unit
////) {
////    var currentSelection by remember { mutableStateOf(selectedSkills.toMutableList()) }
////
////    AlertDialog(
////        onDismissRequest = onDismiss,
////        title = { Text("Select Skills") },
////        confirmButton = {
////            TextButton(
////                onClick = {
////                    onSelectSkills(currentSelection)
////                    onDismiss()
////                }
////            ) {
////                Text("Done")
////            }
////        },
////        dismissButton = {
////            TextButton(onClick = onDismiss) {
////                Text("Cancel")
////            }
////        },
////        text = {
////            LazyColumn {
////                items(allSkills) { skill ->
////                    val isSelected = currentSelection.contains(skill)
////                    Row(
////                        modifier = Modifier
////                            .fillMaxWidth()
////                            .padding(8.dp)
////                            .clickable {
////                                if (isSelected) {
////                                    currentSelection.remove(skill) // Remove skill if already selected
////                                } else {
////                                    currentSelection.add(skill) // Add skill if not selected
////                                }
////                            },
////                        verticalAlignment = Alignment.CenterVertically
////                    ) {
////                        Checkbox(
////                            checked = isSelected,
////                            onCheckedChange = {
////                                if (isSelected) {
////                                    currentSelection.remove(skill) // Remove skill if already selected
////                                } else {
////                                    currentSelection.add(skill) // Add skill if not selected
////                                }
////                            }
////                        )
////                        Text(skill.skillName, modifier = Modifier.padding(start = 8.dp))
////                    }
////                }
////            }
////        }
////    )
////}
//
//// Step 7: Your profile has been set up
//@Composable
//fun ProfileFormStep7(
//    onFinishClick: () -> Unit,
//    isLoading: Boolean // Add a parameter to check if the profile creation is in progress
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = "Click the Finish button to complete Profile Creation.",
//            style = MaterialTheme.typography.headlineLarge
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text(
//            text = "Your profile will be processed.",
//            style = MaterialTheme.typography.bodyLarge,
//            textAlign = TextAlign.Center
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // Loader and Button
//        Box(
//            modifier = Modifier.fillMaxWidth(),
//            contentAlignment = Alignment.Center
//        ) {
//            if (isLoading) {
//                CircularProgressIndicator() // Show a loader
//            } else {
//                Button(
//                    onClick = { onFinishClick() },
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
//                    enabled = !isLoading // Disable the button while loading
//                ) {
//                    Text("Finish")
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun StepProgressBar(currentStep: Int) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        Text(
//            text = "Step $currentStep of 7",
//            style = MaterialTheme.typography.bodyMedium,
//            textAlign = TextAlign.Center
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        LinearProgressIndicator(
//            progress = { currentStep / 7f },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//                .height(8.dp),
//            color = MaterialTheme.colorScheme.primary,
//        )
//    }
//}