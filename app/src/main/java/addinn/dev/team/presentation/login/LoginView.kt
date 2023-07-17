package addinn.dev.team.presentation.login

import addinn.dev.domain.entity.auth.LoginRequest
import addinn.dev.domain.entity.response.Response
import addinn.dev.team.R
import addinn.dev.team.utils.navigation.NavigationProvider
import addinn.dev.team.utils.widgets.PasswordToggleIcon
import addinn.dev.team.utils.widgets.loadingProgress.DialogBoxLoading
import addinn.dev.team.viewModel.AuthViewModel
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Destination(start = true)
@Composable
fun LoginView(navigator: NavigationProvider, viewModel: AuthViewModel = hiltViewModel()) {

    // UI
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisibility = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    // VIEW MODEL
    val requestState = viewModel.loginState.collectAsState()
    val loadingState = viewModel.loadingState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val error = remember { mutableStateOf("") }

    LaunchedEffect(error.value){
        if (error.value.isNotEmpty()){
            snackbarHostState.showSnackbar(error.value)
        }
    }

    LaunchedEffect(requestState.value) {
        when (requestState.value) {
            is Response.Error -> {
                error.value = (requestState.value as Response.Error).error
                snackbarHostState.showSnackbar(error.value)
            }

            is Response.Success -> {
                navigator.navigateToHome()
            }

            else -> {}
        }
    }

    if (loadingState.value) {
        DialogBoxLoading()
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {
        ConstraintLayout(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 12.dp)
                .fillMaxSize()
        ) {
            val (logo, welcomeText, secondText, emailField, passwordField, recoverPassword, loginBtn, registerBtn) = createRefs()

            // Logo
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "logo",
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Transparent, shape = RoundedCornerShape(0.dp))
                    .padding(8.dp)
                    .constrainAs(logo) {
                        top.linkTo(parent.top, margin = 30.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        bottom.linkTo(welcomeText.top, margin = 18.dp)
                        width = Dimension.fillToConstraints
                    }
            )
            // Welcome Text
            Text(
                text = "Welcome back!",
                modifier = Modifier.constrainAs(welcomeText) {
                    top.linkTo(logo.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                },
                style = TextStyle(
                    color = Color(0xFF0D5881),
                    fontSize = 28.sp,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.W600
                )
            )

            // Second Text
            Text(
                text = "Connect with your team faster!",
                modifier = Modifier.constrainAs(secondText) {
                    top.linkTo(welcomeText.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                },
                style = TextStyle(
                    color = Color(0xFF0D5881),
                    fontSize = 20.sp,
                    letterSpacing = 0.1.sp,
                    fontWeight = FontWeight.Light
                )
            )

            // Email Field
            Box(
                modifier = Modifier
                    .constrainAs(emailField) {
                        top.linkTo(secondText.bottom, margin = 65.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
                    .shadow(0.dp, shape = RoundedCornerShape(8.dp), clip = true)
            ) {
                OutlinedTextField(
                    value = username.value,
                    onValueChange = { username.value = it },
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.LightGray,
                        unfocusedBorderColor = Color.LightGray,
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 16.sp),
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Person,
                            contentDescription = "person",
                            tint = Color.Gray
                        )
                    },
                    placeholder = {
                        Text(
                            text = "Email/Username...",
                            style = TextStyle(color = Color.LightGray)
                        )
                    }
                )
            }

            // Password
            Box(
                modifier = Modifier
                    .constrainAs(passwordField) {
                        top.linkTo(emailField.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
                    .shadow(0.dp, shape = RoundedCornerShape(8.dp), clip = true)
            ) {
                OutlinedTextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.LightGray,
                        unfocusedBorderColor = Color.LightGray,
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    visualTransformation = if (passwordVisibility.value) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 16.sp),
                    leadingIcon = {
                        Icon(Icons.Outlined.Lock, contentDescription = "lock", tint = Color.Gray)
                    },
                    trailingIcon = {
                        PasswordToggleIcon(
                            passwordVisibility.value,
                            onToggle = { passwordVisibility.value = !passwordVisibility.value }
                        )
                    },
                    placeholder = {
                        Text(
                            text = "Password...",
                            style = TextStyle(color = Color.LightGray)
                        )
                    }
                )
            }

            // Recover password
            TextButton(
                onClick = { navigator.navigateToRecoverPass() },
                modifier = Modifier.constrainAs(recoverPassword) {
                    top.linkTo(passwordField.bottom)
                    end.linkTo(passwordField.end)
                }) {
                Text(
                    text = "Recover password",
                    color = Color.Gray,
                    style = TextStyle(fontSize = 14.sp)
                )
            }

            // Login button
            TextButton(
                onClick = {
                    if (username.value.isEmpty()) {
                        error.value = "Please enter your email or username"
                    } else if (password.value.isEmpty()) {
                        error.value = "Please enter your password"
                    } else {
                        val loginRequest = LoginRequest(
                            email = username.value,
                            password = password.value
                        )

                        viewModel.login(loginRequest)
                    }
                },
                modifier = Modifier
                    .constrainAs(loginBtn) {
                        top.linkTo(recoverPassword.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .background(
                        color = Color(0xFF0D5881),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Login",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding()
                )
            }

            // Register
            Box(
                modifier = Modifier
                    .constrainAs(registerBtn) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(loginBtn.bottom, margin = 16.dp)
                    }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Don't have an account ?",
                        style = TextStyle(fontWeight = FontWeight.W400)
                    )
                    TextButton(
                        onClick = { navigator.navigateToRegister() },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF0D5881))
                    ) {
                        Text(text = "Join us", style = TextStyle(fontWeight = FontWeight.W700))
                    }
                }
            }

        }
    }
}
