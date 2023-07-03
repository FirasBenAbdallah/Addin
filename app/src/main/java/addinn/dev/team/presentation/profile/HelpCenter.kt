package addinn.dev.team.presentation.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpCenter() {
    val helpItems = listOf(
        HelpItem("How to create an account", "Learn how to create a new account."),
        HelpItem("Forgot password", "Recover your password."),
        HelpItem("Contact support", "Get in touch with our support team."),
        HelpItem("How to create an account", "Learn how to create a new account."),
        HelpItem("Forgot password", "Recover your password."),
        HelpItem("Contact support", "Get in touch with our support team."),
        HelpItem("How to create an account", "Learn how to create a new account."),
        HelpItem("Forgot password", "Recover your password."),
        HelpItem("Contact support", "Get in touch with our support team.")

        // Add more help items as needed
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Help Center") })
        },
    ) {
        Box(modifier = Modifier.padding(top = 50.dp)) {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                items(helpItems) { helpItem ->
                    HelpItemCard(helpItem)
                    Divider()
                }
            }
        }
    }
}

@Composable
fun HelpItemCard(helpItem: HelpItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle help item click */ }
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 15.dp
        )
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(text = helpItem.title, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = helpItem.description, style = MaterialTheme.typography.bodySmall)
        }
    }
}

data class HelpItem(val title: String, val description: String)
