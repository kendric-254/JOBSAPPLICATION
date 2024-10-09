package com.example.istjobsportal.screen

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.istjobsportal.R
import com.example.istjobsportal.nav.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

lateinit var auth: FirebaseAuth
@Composable
fun ISTRegisterScreen(navController: NavController) {
    // Initialize Firebase Auth
    auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showPasswordToggle by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val visualTransformation = if (passwordVisible) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            // Show Circular Progress Indicator with a message
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Registering your account...",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            // Main registration form
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Create an Account",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.headlineMedium,
                            fontSize = 24.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Image(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(56.dp)),
                        painter = painterResource(R.drawable.project_logo),
                        contentDescription = "Login"
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    //the email field asking for a valid email if invalid
                    TextField(
                        value = email,
                        onValueChange = { newEmail ->
                            email = newEmail
                            errorMessage = if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                                "Please enter a valid email address"
                            } else {
                                null
                            }
                        },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = errorMessage != null
                    )


                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    painter = painterResource(if (passwordVisible) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24),
                                    contentDescription = "Toggle password visibility"
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = errorMessage != null
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm Password") },
                        visualTransformation = visualTransformation,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                 //the checkbox for showing the password
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = showPasswordToggle,
                            onCheckedChange = { isChecked ->
                                showPasswordToggle = isChecked
                                passwordVisible = isChecked
                            }
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = if (showPasswordToggle) "Hide Password" else "Show Password", color = Color.Black)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    //Clear and Register button
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        Button(
//                            onClick = {
//                                email = ""
//                                password = ""
//                                confirmPassword = ""
//                                errorMessage = null
//                                successMessage = null
//                            },
//                            modifier = Modifier.weight(1f)
//                        ) {
//                            Text("Clear")
//                        }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            isLoading = true  // Start loading
                            // Validate email and passwords
                            if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                                val error = "Please enter all fields."
                                Toast.makeText(navController.context, error, Toast.LENGTH_LONG).show()
                                isLoading = false
                                return@Button
                            }

                            if (password.length < 6) { // Ensure password is at least 6 characters long
                                val error = "Password must be at least 6 characters long."
                                Toast.makeText(navController.context, error, Toast.LENGTH_LONG).show()
                                isLoading = false
                                return@Button
                            }

                            if (password != confirmPassword) {
                                val error = "Passwords do not match."
                                Toast.makeText(navController.context, error, Toast.LENGTH_LONG).show()
                                isLoading = false
                                return@Button
                            }

                            // Proceed with sign-up
                            signUp(
                                email,
                                password,
                                navController,
                                { user ->
                                    successMessage = "Verification email sent to ${user?.email}"
                                    isLoading = false
                                    Toast.makeText(navController.context, successMessage, Toast.LENGTH_LONG).show()
                                    navController.navigate(Screens.ISTLoginScreen.route) // Navigate to Login screen
                                },
                                { error ->
                                    errorMessage = error
                                    isLoading = false
                                    Toast.makeText(navController.context, errorMessage, Toast.LENGTH_LONG).show()
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Register")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Display error or success message
                    errorMessage?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    successMessage?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

private fun signUp(
    email: String,
    password: String,
    navController: NavController,
    onSuccess: (FirebaseUser?) -> Unit,
    onFailure: (String) -> Unit
) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.let {
                    // Default role set to "alumni"
                    val role = "alumni"
                    val db = FirebaseFirestore.getInstance()

                    // Create a user document with email and role in Firestore
                    val userDoc = mapOf(
                        "email" to email,
                        "role" to role
                    )

                    db.collection("users").document(user.uid)
                        .set(userDoc)
                        .addOnSuccessListener {
                            // Send verification email
                            user.sendEmailVerification()
                                .addOnCompleteListener { verificationTask ->
                                    if (verificationTask.isSuccessful) {
                                        navController.navigate(Screens.ISTLoginScreen.route)
                                        onSuccess(user)
                                    } else {
                                        onFailure(verificationTask.exception?.message ?: "Failed to send verification email")
                                    }
                                }
                        }
                        .addOnFailureListener { e ->
                            onFailure(e.message ?: "Failed to save user data")
                        }
                }
            } else {
                onFailure(task.exception?.message ?: "Sign up failed")
            }
        }
}
