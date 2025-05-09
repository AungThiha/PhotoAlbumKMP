package aung.thiha.photo.album.authentication.presentation.signup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import aung.thiha.photo.album.design.AlbumTopAppBar
import aung.thiha.photo.album.koin.getViewModel
import aung.thiha.photo.album.design.LoadingOverlay
import aung.thiha.photo.album.navigation.Route

@Composable
fun SignupScreen(
    navHostController: NavHostController
) {

    val viewModel = getViewModel<SignupViewModel>()
    val signupState by remember { viewModel.signupState }

    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val keyboard = LocalSoftwareKeyboardController.current

    if (messages.isNotEmpty()) {
        val message = messages.first()

        LaunchedEffect(key1 = message) {
            snackbarHostState.showSnackbar(message = message)
            viewModel.setMessageShown(message)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                SignupEvent.NavigateToPhotoList -> {
                    navHostController.navigate(Route.PhotoList.name) {
                        popUpTo(0)
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            AlbumTopAppBar { navHostController.navigateUp() }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome to My Photo",
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.updateEmail(it) },
                label = { Text("email") },
                placeholder = { Text("example@example.com") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.updatePassword(it) },
                label = { Text("password") },
                placeholder = { Text("your password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.confirmPassword,
                onValueChange = { viewModel.updateConfirmPassword(it) },
                label = { Text("confirm password") },
                placeholder = { Text("confirm your password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    keyboard?.hide()
                    viewModel.signup()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Sign up", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

        }
    }
    if (signupState == SignupState.OverlayLoading) {
        LoadingOverlay()
    }
}
