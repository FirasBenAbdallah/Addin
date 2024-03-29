package addinn.dev.team.presentation.profile

import addinn.dev.domain.entity.response.Response
import addinn.dev.team.R
import addinn.dev.team.utils.navigation.NavigationProvider
import addinn.dev.team.utils.widgets.loadingProgress.DialogBoxLoading
import addinn.dev.team.viewModel.AuthViewModel
import addinn.dev.team.viewModel.SharedViewModel
import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileView(
    navigator: NavigationProvider,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    val (names, setNames) = remember { mutableStateOf("Firas") }
    val (_, setEmails) = remember { mutableStateOf("") }
    val show = remember { mutableStateOf(false) }
    val help = remember { mutableStateOf(false) }

    // VIEW MODEL
    val requestState = authViewModel.logoutState.collectAsState()
    val loadingState = authViewModel.loadingState.collectAsState()

    // CURRENT USER
    val currentUser = sharedViewModel.getUser()

    LaunchedEffect(requestState.value) {
        when (requestState.value) {
            is Response.Error -> {}

            is Response.Success -> {
                navigator.navigateToLogin()
            }

            else -> {}
        }
    }

    if (loadingState.value) {
        DialogBoxLoading()
    }

    Scaffold(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {

            val context = LocalContext.current
            // Profile Row
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Profile Picture
                Image(
                    painter = painterResource(id = R.drawable.avatar),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )

                // Profile Name and Email
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(text = names, style = TextStyle(fontSize = 20.sp))
                    Text(text = currentUser.email!!, style = TextStyle(fontSize = 16.sp))
                }

                Spacer(modifier = Modifier.weight(1f))
                // Button on the right
                IconButton(onClick = {
                    showToast(context, "Edit Profile")
                    show.value = true
                })
                {
                    Icon(Icons.Outlined.Edit, contentDescription = "Edit Profile")
                }
            }
            if (!show.value) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {

                    // Account
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            Icons.Outlined.Person,
                            contentDescription = "Account Icon",
                            modifier = Modifier.size(24.dp)
                        )
                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text(
                                text = "Account",
                                style = TextStyle(fontSize = 16.sp),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                            Text(
                                text = "Manage your account",
                                style = TextStyle(fontSize = 14.sp, color = Color.Gray),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = { /*showToast(context, "Manage Your Account")*/ },
                        ) {
                            Icon(
                                Icons.Outlined.ArrowForward,
                                contentDescription = "Account Button",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Orders
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            Icons.Outlined.ShoppingCart,
                            contentDescription = "Oreders Icon",
                            modifier = Modifier.size(24.dp)
                        )
                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text(
                                text = "Orders",
                                style = TextStyle(fontSize = 16.sp),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                            Text(
                                text = "Orders history",
                                style = TextStyle(fontSize = 14.sp, color = Color.Gray),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = { /*showToast(context, "Manage Your Account")*/ },
                        ) {
                            Icon(
                                Icons.Outlined.ArrowForward,
                                contentDescription = "Account Button",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Addresses
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.outline_navigation_24),
                            contentDescription = "Addresses Icon",
                        )
                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text(
                                text = "Addresses",
                                style = TextStyle(fontSize = 16.sp),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                            Text(
                                text = "Your saved addresses",
                                style = TextStyle(fontSize = 14.sp, color = Color.Gray),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = { /*showToast(context, "Manage Your Account")*/ },
                        ) {
                            Icon(
                                Icons.Outlined.ArrowForward,
                                contentDescription = "Account Button",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Saved Cards
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.outline_payments_24),
                            contentDescription = "Saved Cards Icon",
                        )
                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text(
                                text = "Saved Cards",
                                style = TextStyle(fontSize = 16.sp),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                            Text(
                                text = "Your saved debit/credit cards",
                                style = TextStyle(fontSize = 14.sp, color = Color.Gray),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = { /*showToast(context, "Manage Your Account")*/ },
                        ) {
                            Icon(
                                Icons.Outlined.ArrowForward,
                                contentDescription = "Account Button",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Settings
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.outline_settings_24),
                            contentDescription = "Settings Icon",
//                modifier = Modifier.size(24.dp)
                        )
                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text(
                                text = "Settings",
                                style = TextStyle(fontSize = 16.sp),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                            Text(
                                text = "App notification settings",
                                style = TextStyle(fontSize = 14.sp, color = Color.Gray),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = { /*showToast(context, "Manage Your Account")*/ },
                        ) {
                            Icon(
                                Icons.Outlined.ArrowForward,
                                contentDescription = "Account Button",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Offers and Coupons
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.outline_local_offer_24),
                            contentDescription = "Offers and Coupons Icon",
//                modifier = Modifier.size(24.dp)
                        )
                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text(
                                text = "Offers and Coupons",
                                style = TextStyle(fontSize = 16.sp),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                            Text(
                                text = "Offers and coupon codes for you",
                                style = TextStyle(fontSize = 14.sp, color = Color.Gray),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = { /*showToast(context, "Manage Your Account")*/ },
                        ) {
                            Icon(
                                Icons.Outlined.ArrowForward,
                                contentDescription = "Account Button",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Help Center
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.outline_help_24),
                            contentDescription = "Help Icon",
                        )
                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text(
                                text = "Help Center",
                                style = TextStyle(fontSize = 16.sp),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                            Text(
                                text = "FAQs and customer support",
                                style = TextStyle(fontSize = 14.sp, color = Color.Gray),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = {
                                showToast(context, "Help Center")
                                help.value = true
                            },
                        ) {
                            Icon(
                                Icons.Outlined.ArrowForward,
                                contentDescription = "Account Button",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Logout
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_logout_24),
                            contentDescription = "Logout Icon",
//                modifier = Modifier.size(24.dp)
                        )
                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text(
                                text = "Logout",
                                style = TextStyle(fontSize = 16.sp),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                            Text(
                                text = "Logout from your account",
                                style = TextStyle(fontSize = 14.sp, color = Color.Gray),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = {
                                // TODO: DO NOT REMOVE THIS WIDGET
                                authViewModel.logout()
                            },
                        ) {
                            Icon(
                                Icons.Outlined.ArrowForward,
                                contentDescription = "Account Button",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    /*
                    // Wishlist Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        //modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.outline_favorite_border_24),
                            contentDescription = "Wishlist Icon",
//                modifier = Modifier.size(24.dp)
                        )
                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text(
                                text = "Wishlist",
                                style = TextStyle(fontSize = 16.sp),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                            Text(
                                text = "Items you saved",
                                style = TextStyle(fontSize = 14.sp, color = Color.Gray),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = { showToast(context, "Manage Your Account") },
                        ) {
                            Icon(
                                Icons.Outlined.ArrowForward,
                                contentDescription = "Account Button",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    */
                }
            }
        }

        // Edit Profile
        if (show.value) {
            EditProfile(onSaveProfile = { updatedNames, updatedEmails ->
                setNames(updatedNames)
                setEmails(updatedEmails)
                show.value = false
            })
        }
        if (help.value) {
            navigator.navigateToHelpCenter()
        }
    }
}

fun showToast(context: Context, message: String) {
    CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

/*@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfilePreview() {
    EditProfile()
}*/
