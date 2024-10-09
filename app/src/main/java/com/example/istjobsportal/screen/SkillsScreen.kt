package com.example.istjobsportal.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.istjobsportal.utils.SharedViewModel
import com.example.istjobsportal.utils.SkillData
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    var skillName by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    // Gradient background color
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Skill", style = MaterialTheme.typography.headlineMedium) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back_button")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(brush = backgroundGradient),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .shadow(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Add a New Skill",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    TextField(
                        value = skillName,
                        onValueChange = {
                            skillName = it
                            showError = false
                        },
                        label = { Text("Skill Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(4.dp, RoundedCornerShape(8.dp)),
                        shape = RoundedCornerShape(12.dp),
                        isError = showError
                    )

                    if (showError) {
                        Text(
                            text = "Please enter a valid skill name",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (skillName.isNotEmpty()) {
                                isSaving = true
                                val skill = SkillData(
                                    skillID = UUID.randomUUID().toString(),
                                    skillName = skillName
                                )

                                // Save skill without callback, then update isSaving manually
                                sharedViewModel.saveSkill(skill, context = navController.context)
                                isSaving = false  // Set to false after save is initiated

                                // Navigate back
                                navController.popBackStack()
                            } else {
                                showError = true
                            }
                        },
                        enabled = !isSaving,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        } else {
                            Text("Save Skill", style = MaterialTheme.typography.bodyLarge)
                        }
                    }

                }
            }
        }
    }
}
