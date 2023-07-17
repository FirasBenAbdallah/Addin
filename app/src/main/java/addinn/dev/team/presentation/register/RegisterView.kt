package addinn.dev.team.presentation.register

import addinn.dev.domain.entity.auth.RegisterRequest
import addinn.dev.domain.entity.response.Response
import addinn.dev.team.R
import addinn.dev.team.utils.navigation.NavigationProvider
import addinn.dev.team.utils.widgets.PasswordToggleIcon
import addinn.dev.team.utils.widgets.loadingProgress.DialogBoxLoading
import addinn.dev.team.viewModel.AuthViewModel
import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun RegisterView(navigator: NavigationProvider, viewModel: AuthViewModel = hiltViewModel()) {
    // UI
    val username = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisibility = remember { mutableStateOf(false) }
    var selectedImageURI by remember {
        mutableStateOf<Uri?>(null)
    }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageURI = uri
        }
    )
    val focusManager = LocalFocusManager.current
    val isEnabled = remember { mutableStateOf(false) }

    // DROPDOWN
    val listOfDepartments = listOf("Administration", "Web", "Mobile", "Data", "DevOps")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(listOfDepartments[0]) }

    // VIEW MODEL
    val requestState = viewModel.registerState.collectAsState()
    val loadingState = viewModel.loadingState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    /**USERNAME CHECK**/
    var iconId by remember { mutableStateOf(0) }
    var color by remember { mutableStateOf(Color.Gray) }
    val loadingCheck = viewModel.loadingCheckState.collectAsState()
    val checkState = viewModel.checkState.collectAsState()
    if (username.value.isEmpty()) {
        iconId = 0
    }

    val error = remember { mutableStateOf("") }

    LaunchedEffect(error.value) {
        if (error.value.isNotEmpty()) {
            snackbarHostState.showSnackbar(error.value)
        }
    }

    LaunchedEffect(checkState.value) {
        when (checkState.value) {
            is Response.Error -> {
                error.value = (checkState.value as Response.Error).error
            }

            is Response.Success -> {
                val success = (checkState.value as Response.Success).data
                if (success) {
                    isEnabled.value = false
                    iconId = R.drawable.outline_error_24
                    color = Color.Red
                } else {
                    isEnabled.value = true
                    iconId = R.drawable.baseline_check_circle_24
                    color = Color.Green
                }
            }

            else -> {}
        }
    }

    /****/

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
            val (logo, welcomeText, secondText, usernameField, emailField, passwordField, departmentDropdown, registerBtn, /*divider,fingerprintAuth,*/ loginBtn) = createRefs()

            Box(modifier = Modifier
                .constrainAs(logo) {
                    top.linkTo(parent.top, margin = 30.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                    bottom.linkTo(welcomeText.top, margin = 18.dp)
                    width = Dimension.wrapContent
                }
                .background(color = Color.LightGray, shape = CircleShape)
                .size(100.dp)
                .clickable {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            ) {
                if (selectedImageURI == null) {
                    Image(
                        painter = painterResource(R.drawable.avatar),
                        contentDescription = "avatar",
                        contentScale = ContentScale.Inside,
                        modifier = Modifier.size(100.dp)
                    )
                } else {
                    AsyncImage(
                        model = selectedImageURI,
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.size(100.dp)
                    )
                }

            }

            Text(
                text = "Welcome !",
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

            Text(
                text = "Join our app!",
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

            Box(
                modifier = Modifier
                    .constrainAs(usernameField) {
                        top.linkTo(secondText.bottom, margin = 65.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
                    .shadow(0.dp, shape = RoundedCornerShape(8.dp), clip = true)
            ) {
                OutlinedTextField(
                    value = username.value,
                    onValueChange = {
                        username.value = it
                        viewModel.checkUsername(it)
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.LightGray,
                        unfocusedBorderColor = Color.LightGray,
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
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
                    trailingIcon = {
                        if (loadingCheck.value) {
                            CircularProgressIndicator(
                                color = Color.Gray,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            if (iconId != 0) {
                                Icon(
                                    painter = painterResource(id = iconId),
                                    contentDescription = "check",
                                    tint = color
                                )
                            }
                        }
                    },
                    placeholder = {
                        Text(
                            text = "Username...",
                            style = TextStyle(color = Color.LightGray)
                        )
                    }
                )
            }

            Box(
                modifier = Modifier
                    .constrainAs(emailField) {
                        top.linkTo(usernameField.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
                    .shadow(0.dp, shape = RoundedCornerShape(8.dp), clip = true)
            ) {
                OutlinedTextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.LightGray,
                        unfocusedBorderColor = Color.LightGray,
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 16.sp),
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Email,
                            contentDescription = "mail",
                            tint = Color.Gray
                        )
                    },
                    placeholder = {
                        Text(
                            text = "Email...",
                            style = TextStyle(color = Color.LightGray)
                        )
                    }
                )
            }

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
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    visualTransformation = if (passwordVisibility.value) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 16.sp),
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Lock,
                            contentDescription = "lock",
                            tint = Color.Gray
                        )
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

            Box(
                modifier = Modifier
                    .constrainAs(departmentDropdown) {
                        top.linkTo(passwordField.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
                    .shadow(0.dp, shape = RoundedCornerShape(8.dp), clip = true)
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                ) {
                    OutlinedTextField(
                        value = selectedText,
                        onValueChange = {},
                        shape = RoundedCornerShape(10.dp),
                        readOnly = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.LightGray,
                            unfocusedBorderColor = Color.LightGray,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        textStyle = TextStyle(fontSize = 16.sp),
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Home,
                                contentDescription = "Home",
                                tint = Color.Gray
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listOfDepartments.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    selectedText = item
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            TextButton(
                enabled = isEnabled.value,
                onClick = {
                    if (username.value.isEmpty()) {
                        error.value = "Please enter a username"
                    } else if (email.value.isEmpty()) {
                        error.value = "Please enter an email"
                    } else if (password.value.isEmpty()) {
                        error.value = "Please enter a password"
                    } else if (selectedImageURI == null) {
                        error.value = "Please select an image"
                    }else {
                        val registerRequest = RegisterRequest(
                            email = email.value,
                            password = password.value,
                            department = selectedText,
                            username = username.value
                        )

                        viewModel.register(selectedImageURI!!, registerRequest)
                    }
                },
                modifier = Modifier
                    .constrainAs(registerBtn) {
                        top.linkTo(departmentDropdown.bottom, margin = 16.dp)
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
                    text = "Create Account",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding()
                )
            }

            Box(
                modifier = Modifier
                    .constrainAs(loginBtn) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(registerBtn.bottom, margin = 16.dp)
                    }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already have an account ?",
                        style = TextStyle(fontWeight = FontWeight.W400)
                    )
                    TextButton(
                        onClick = { navigator.navigateBack() },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF0D5881))
                    ) {
                        Text(text = "Login", style = TextStyle(fontWeight = FontWeight.W700))
                    }
                }
            }
        }

    }


}