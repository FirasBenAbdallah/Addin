package addinn.dev.team.presentation.login

import addinn.dev.team.R
import addinn.dev.team.utils.navigation.NavigationProvider
import addinn.dev.team.utils.widgets.PasswordToggleIcon
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.ramcosta.composedestinations.annotation.Destination

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Destination(start = true)
@Composable
fun LoginView(navigator: NavigationProvider) {

    // UI
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisibility = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Scaffold(
        containerColor = Color.White.copy(alpha = 0.5f)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 12.dp)
                .fillMaxSize()
        ) {
            val (logo, welcomeText,secondText, emailField, passwordField, recoverPassword, loginBtn, divider,fingerprintAuth, registerBtn) = createRefs()

            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "logo",
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Transparent,shape = RoundedCornerShape(0.dp))
                    .padding(8.dp)
                    .constrainAs(logo) {
                        top.linkTo(parent.top,margin = 30.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        bottom.linkTo(welcomeText.top, margin = 18.dp)
                        width = Dimension.fillToConstraints
                    }
            )

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
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.LightGray,
                        unfocusedBorderColor = Color.LightGray
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
                    placeholder = {
                        Text(
                            text = "Email/Username...",
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
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.LightGray,
                        unfocusedBorderColor = Color.LightGray
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

            TextButton(
                onClick = { /*TODO*/ },
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

            TextButton(
                onClick = {
                    /* TODO : implement navigation */
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

            Row(modifier = Modifier
                .constrainAs(divider) {
                    top.linkTo(loginBtn.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Divider(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                )
                Text(text = "OR",modifier = Modifier.padding(horizontal = 8.dp))
                Divider(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                )
            }

            IconButton(
                onClick = {
                    /* TODO : BOTTOM SHEET */
                },
                modifier = Modifier
                    .constrainAs(fingerprintAuth){
                        top.linkTo(divider.bottom,margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(2.dp, Color.LightGray),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fingerprint),
                    contentDescription = "fingerprint",
                    modifier = Modifier.size(60.dp)
                )
            }

            Box(
                modifier = Modifier
                    .constrainAs(registerBtn) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                    }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Don't have an account ?", style = TextStyle(fontWeight = FontWeight.W400))
                    TextButton(
                        onClick = { /*TODO : Navigation */ },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF0D5881))
                    ) {
                        Text(text = "Join us", style = TextStyle(fontWeight = FontWeight.W700))
                    }
                }
            }

        }
    }
}