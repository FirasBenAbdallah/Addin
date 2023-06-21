package addinn.dev.team.presentation.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditProfile(onSaveProfile: (names: String, emails: String) -> Unit) {

    // Variables Local
    val nameIn = remember { mutableStateOf("") }
    val emailIn = remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold {
        ConstraintLayout(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 12.dp)
                .fillMaxSize()
        ) {
            val (columnEdit /*email, save*/) = createRefs()
            Column(
                modifier = Modifier.constrainAs(columnEdit) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
            ) {
                // Name
                OutlinedTextField(
                    value = nameIn.value,
                    onValueChange = { nameIn.value = it },
                    label = { Text(text = "Name") },
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                // Email
                OutlinedTextField(
                    value = emailIn.value,
                    onValueChange = { emailIn.value = it },
                    modifier = Modifier.padding(vertical = 8.dp),
                    label = { Text(text = "Email") },
                )
                // Update Profile
                Button(
                    onClick = {
                        onSaveProfile(nameIn.value, emailIn.value)
                        showToast(context, "Profile Updated")
                    },
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Update Profile")
                }
            }
        }
    }
}