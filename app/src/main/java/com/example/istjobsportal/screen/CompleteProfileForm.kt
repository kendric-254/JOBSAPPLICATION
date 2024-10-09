package com.example.istjobsportal.screen

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
//    showLogo: Boolean,
    profilePhoto: Uri?,
    onSelectPhotoClick: () -> Unit,
    onSubmit: () -> Unit
) {
    // Get context from LocalContext within the composable
    val context = LocalContext.current
//before validation check the values to ensure they are as expected
    Log.d("CompleteProfileForm", "Full Name: $fullName, Email: $email, Phone: $phone, LinkedIn: $linkedIn")


    // Define the form validity check
    val isFormValid = fullName.isNotBlank() && email.isNotBlank() && phone.isNotBlank() && linkedIn.isNotBlank()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
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
////to display logo conditionally based on the showLogo parameter
//        if (showLogo) {
//            Image(
//                painter = painterResource(id = schoolLogo),
//                contentDescription = "School Logo",
//                modifier = Modifier
//                    .size(200.dp)
//                    .shadow(4.dp),
//                contentScale = ContentScale.Crop
//            )
//        }

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

        // Full Name
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            value = fullName,
            onValueChange = onFullNameChange,
            label = { Text(text = "Full Name") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            isError = fullName.isBlank()
        )

        // Email
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            value = email,
            onValueChange = onEmailChange,
            label = { Text(text = "Email") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            readOnly = true,
            isError = email.isBlank()
        )

        // Phone
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            value = phone,
            onValueChange = onPhoneChange,
            label = { Text(text = "Phone") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            isError = phone.isBlank()
        )

        // LinkedIn URL
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            value = linkedIn,
            onValueChange = onLinkedInChange,
            label = { Text(text = "LinkedIn Profile URL") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Done
            ),
            isError = linkedIn.isBlank()
        )

        Spacer(modifier = Modifier.height(24.dp))

        var errorMessage by remember { mutableStateOf("") }

        Button(
            onClick = {
                if (isFormValid) {
                    onSubmit()  // Submit the form data
                } else {
                    errorMessage = "Please fill in all the fields"
                }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text(text = "Submit", style = MaterialTheme.typography.labelLarge)
        }

        if (errorMessage.isNotBlank()) {
            Text(text = errorMessage, color = Color.Red)
        }

    }
}
