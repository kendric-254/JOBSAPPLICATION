package com.example.istjobsportal.screen

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.istjobsportal.nav.Screens
import com.example.istjobsportal.utils.JobData
import com.example.istjobsportal.utils.NotificationViewModel
import com.example.istjobsportal.utils.ProfileViewModel
import com.example.istjobsportal.utils.SharedViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun DisplayJobScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    profileViewModel: ProfileViewModel,
    notificationViewModel: NotificationViewModel
) {
    var jobs by remember { mutableStateOf<List<JobData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var userRole by remember { mutableStateOf<String?>(null) }
    var showLogoutConfirmation by remember { mutableStateOf(false) }
    var profilePhotoUrl by remember { mutableStateOf<String?>(null) }
    val loading = remember { mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() } // Snackbar host state
    val coroutineScope = rememberCoroutineScope() // Coroutine scope for launching coroutines

    // Fetch user role and profile photo
    LaunchedEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            val db = FirebaseFirestore.getInstance()
            val documentSnapshot = db.collection("users").document(uid).get().await()
            userRole = documentSnapshot.getString("role") ?: "alumni"
        }

        if (userRole == "alumni"){
        profileViewModel.retrieveProfilePhoto(
            onLoading = { loading.value = it },
            onSuccess = { url -> profilePhotoUrl = url },
            onFailure = { message ->
                Log.e(
                    "DisplayJobScreen",
                    "Error fetching profile photo: $message"
                )

        }
        )}
    }

    // Fetch jobs
    LaunchedEffect(Unit) {
        sharedViewModel.retrieveJobs(
            onLoading = { loading -> isLoading = loading },
            onSuccess = { retrievedJobs -> jobs = retrievedJobs },
            onFailure = { message -> errorMessage = message }
        )
    }

    // Show logout confirmation dialog
    if (showLogoutConfirmation) {
        LogoutConfirm(
            onConfirm = {
                FirebaseAuth.getInstance().signOut() // Log out the user
                navController.navigate(Screens.ISTLoginScreen.route) // Navigate to login screen
                showLogoutConfirmation = false
            },
            onDismiss = { showLogoutConfirmation = false }
        )
    }

    Scaffold(
        topBar = {
            DashboardTopBar(
                navController = navController,
                onLogoutClick = { showLogoutConfirmation = true },
                userRole = userRole,
                profilePhotoUrl = profilePhotoUrl
            )
        },
        bottomBar = {
            DashboardBottomBar(navController = navController, userRole = userRole, notificationViewModel = notificationViewModel)
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }   // Add SnackbarHost
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center // Center content horizontally and vertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 4.dp,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Retrieving Jobs, please wait...",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                }
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Conditionally show the "Add Job" and "Add Skill" buttons if the user is an admin
                    if (userRole == "admin") {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Button(onClick = { navController.navigate(Screens.AddJobScreen.route) }) {
                                Text(text = "Add Job")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = { navController.navigate(Screens.AddSkillScreen.route) }) {
                                Text(text = "Add Skill")
                            }
                        }
                    }

                    // Job List or Error Message
                    when {
                        errorMessage != null -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
                            }
                        }

                        jobs.isEmpty() -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No jobs available at the moment.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }

                        else -> {
                            // Display the list of jobs once they are loaded
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(jobs) { job ->
                                    JobItem(
                                        job = job,
                                        userRole = userRole ?: "",
                                        navController = navController,
                                        sharedViewModel = sharedViewModel,
                                        onJobDeleted = { deletedJob ->
                                            jobs =
                                                jobs.filterNot { it.jobID == deletedJob.jobID } // Update the list
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Job deleted successfully")
                                            }
                                        }
                                    )
                                }
                            }
                        }


                    }
                }
            }
        }
    }
}


@Composable
fun LogoutConfirm(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text("Logout")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Logout") },
        text = { Text("Are you sure you want to logout?") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("NewApi", "SuspiciousIndentation")
@Composable
fun JobItem(
    job: JobData,
    userRole: String,
    navController: NavController,
    sharedViewModel: SharedViewModel,
    onJobDeleted: (JobData) -> Unit // Callback when job is deleted
) {
    var isExpanded by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) } // State for delete confirmation dialog

    val currentDate = LocalDate.now()
    val deadlineDate = job.deadlineDate?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()

    val color by animateColorAsState(
        targetValue = if (isExpanded) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.onPrimary,
        label = "",
    )


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                .background(color = color)
        ) {
            if (userRole == "admin") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { navController.navigate("edit_job/${job.jobID}") }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Delete Job")
                    }
                    IconButton(onClick = { showDeleteConfirmation = true }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Job")
                    }
                }
            }

            if (job.companyLogo.isNotEmpty()) {
                val painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(job.companyLogo)
                        .apply { crossfade(true) }.build()
                )
                Image(
                    painter = painter,
                    contentDescription = "Company Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                modifier = Modifier.padding(10.dp),
                text = job.title,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (!isExpanded) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = job.description.take(100) + if (job.description.length > 100) "..." else "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (isExpanded) {
                JobDetails(job, deadlineDate, currentDate, navController, userRole)
                TextButton(onClick = { isExpanded = false }) {
                    Text(text = "Show Less")
                }
            } else {
                TextButton(onClick = { isExpanded = true }) {
                    Text(text = "View More")
                }
            }
        }

    if (showDeleteConfirmation) {
        DeleteJobConfirmationDialog(
            onConfirm = {
                sharedViewModel.deleteJob(
                    jobID = job.jobID,
                    onSuccess = {
                        onJobDeleted(job) // Update the UI after deletion
                        showDeleteConfirmation = false
                    },
                    onFailure = { error ->
                        Log.e("JobItem", "Failed to delete job: $error")
                        showDeleteConfirmation = false
                    }
                )
            },
            onDismiss = { showDeleteConfirmation = false }
        )
    }
}

@Composable
fun DeleteJobConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Delete Job") },
        text = { Text("Are you sure you want to delete this job? This action cannot be undone.") }
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JobDetails(
    job: JobData,
    deadlineDate: LocalDate?,
    currentDate: LocalDate,
    navController: NavController,
    userRole: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)  // Add padding to give the content breathing room
            .animateContentSize(  // Animate the size changes for smooth expansion and collapse
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
    ) {
        // Deadline Information
        Text(
            text = "Deadline: ${deadlineDate?.toString() ?: "N/A"}",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Description
        Text(
            text = job.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 4.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))

        JobInfoSection(title = "Location", info = job.location)

        JobInfoSection(title = "Salary", info = job.salary)

        JobInfoSection(title = "Job Type", info = job.jobType.toString())

        JobInfoSection(title = "Experience Level", info = job.experienceLevel)

        JobInfoSection(title = "Education Level", info = job.educationLevel)

        // Skills Required
        if (job.skills.isNotEmpty()) {
            Text(
                text = "Skills Required:",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                job.skills.forEach { skill ->
                    Text(
                        text = skill,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Apply Button (only for alumni)
        if (userRole == "alumni") {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navController.navigate("job_application/${job.jobID}")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (deadlineDate != null && !currentDate.isAfter(deadlineDate))
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    contentColor = if (deadlineDate != null && !currentDate.isAfter(deadlineDate))
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                ),
                enabled = deadlineDate != null && !currentDate.isAfter(deadlineDate) // Disable button if deadline passed
            ) {
                Text(text = if (deadlineDate != null && !currentDate.isAfter(deadlineDate)) "Apply" else "Deadline Passed")
            }
        }
    }
}

@Composable
fun JobInfoSection(title: String, info: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = info,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

