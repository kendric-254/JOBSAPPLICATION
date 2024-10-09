package com.example.istjobsportal.screen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import coil.compose.rememberAsyncImagePainter
import com.example.istjobsportal.R

@Composable
fun CompleteProfileForm(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    linkedIn: String,
    onLinkedInChange: (String) -> Unit,
    profilePhoto: Uri?,
    onSelectPhotoClick: () -> Unit,
    onSubmit: () -> Unit,
    currentStep: Int,
    isLoading: Boolean,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // School logo
        val schoolLogo = R.drawable.project_logo
        Image(
            painter = painterResource(id = schoolLogo),
            contentDescription = "School Logo",
            modifier = Modifier
                .size(200.dp)
                .shadow(4.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Photo
        if (profilePhoto != null) {
            Image(
                painter = rememberAsyncImagePainter(profilePhoto),
                contentDescription = "Profile Photo",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .shadow(4.dp)
                    .padding(16.dp)
            )
        } else {
            Text("No profile photo selected", modifier = Modifier.padding(vertical = 8.dp))
        }

        Button(onClick = onSelectPhotoClick, modifier = Modifier.shadow(4.dp)) {
            Text("Select Profile Photo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Full Name Field
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            value = fullName,
            onValueChange = onFullNameChange,
            label = { Text("Full Name") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )

        // Email Field (removed read-only)
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        // Phone Field
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            value = phone,
            onValueChange = onPhoneChange,
            label = { Text("Phone") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            )
        )

        // LinkedIn URL Field
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            value = linkedIn,
            onValueChange = onLinkedInChange,
            label = { Text("LinkedIn Profile URL") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Progress Bar
        StepProgressBar(currentStep)

        Spacer(modifier = Modifier.height(24.dp))

        // Loader and Buttons (Submit, Back)
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            if (isLoading) {
                CircularProgressIndicator()  // Loader
            } else {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(onClick = onBackClick, modifier = Modifier.weight(1f)) {
                            Text("Back")
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(
                            onClick = {
                                onSubmit() // Directly call submit without validation
                            },
                            modifier = Modifier.weight(1f),
                            enabled = !isLoading
                        ) {
                            Text("Submit")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StepProgressBar(currentStep: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Step $currentStep of 4",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = currentStep / 4f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(8.dp),
            color = MaterialTheme.colorScheme.primary,
        )
    }
}
