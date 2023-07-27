package addinn.dev.team.presentation.events

import addinn.dev.domain.entity.event.EventRequest
import addinn.dev.domain.entity.response.Response
import addinn.dev.team.utils.navigation.NavigationProvider
import addinn.dev.team.utils.widgets.loadingProgress.DialogBoxLoading
import addinn.dev.team.viewModel.event.AddEventViewModel
import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.marosseleng.compose.material3.datetimepickers.date.ui.dialog.DatePickerDialog
import com.marosseleng.compose.material3.datetimepickers.time.ui.dialog.TimePickerDialog
import com.ramcosta.composedestinations.annotation.Destination
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Destination
fun AddEventView(
    navigator: NavigationProvider,
    addEventViewModel: AddEventViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val titleField = remember { mutableStateOf("") }
    val descriptionField = remember { mutableStateOf("") }
    // DROPDOWN
    val eventTypes = listOf("Meeting", "Outing", "Birthday", "Other")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(eventTypes[0]) }

    var isTimeDialogShown: Boolean by rememberSaveable {
        mutableStateOf(false)
    }
    val (selectedTime, setSelectedTime) = rememberSaveable {
        mutableStateOf(LocalTime.now())
    }

    var isDateDialogShown: Boolean by rememberSaveable {
        mutableStateOf(false)
    }
    val (selectedDate, setSelectedDate) = rememberSaveable {
        mutableStateOf(LocalDate.now())
    }

    val convertTimeToLong = { time: LocalTime ->
        time.atDate(LocalDate.ofEpochDay(0))
            .atZone(ZoneId.systemDefault())
            .toInstant().toEpochMilli()
    }

    val convertDateToLong = { date: LocalDate ->
        date.atTime(LocalTime.of(0, 0))
            .atZone(ZoneId.systemDefault())
            .toInstant().toEpochMilli()
    }

    val isToday = { date: LocalDate ->
        date.isEqual(LocalDate.now())
    }

    val loadingState = addEventViewModel.loadingState.collectAsState()
    val addState = addEventViewModel.create.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    if (loadingState.value) {
        DialogBoxLoading()
    }

    LaunchedEffect(addState.value) {
        when (addState.value) {
            is Response.Error -> {
                val error = (addState.value as Response.Error).error
                snackbarHostState.showSnackbar(error)
            }

            Response.Loading -> {

            }

            is Response.Success -> {
                navigator.navigateBack()
            }
        }
    }

    if (isTimeDialogShown) {
        TimePickerDialog(
            onDismissRequest = { isTimeDialogShown = false },
            onTimeChange = {
                setSelectedTime(it)
                isTimeDialogShown = false
            },
            title = { Text(text = "Select time") }
        )
    }

    if (isDateDialogShown) {
        DatePickerDialog(
            onDismissRequest = { isDateDialogShown = false },
            onDateChange = {
                Timber.e("selected date: $it")
                if(it.isBefore(LocalDate.now())||it.isEqual(LocalDate.now())){
                    println("selected date is before today")
                    Toast.makeText(context, "Please select a valid date", Toast.LENGTH_SHORT).show()
                }else{
                    setSelectedDate(it)
                    isDateDialogShown = false
                }
            },
            title = { Text(text = "Select date") },
        )
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Add Event") }, navigationIcon = {
            IconButton(onClick = { navigator.navigateBack() }) {
                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "back")
            }
        })
    }, snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = titleField.value,
                onValueChange = {
                    titleField.value = it
                },
                label = { Text("Title") },
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                textStyle = TextStyle(fontSize = 16.sp),
            )

            OutlinedTextField(
                value = descriptionField.value,
                onValueChange = {
                    descriptionField.value = it
                },
                label = { Text("Description") },
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                textStyle = TextStyle(fontSize = 16.sp),
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
            ) {
                OutlinedTextField(
                    value = selectedText,
                    onValueChange = {},
                    label = { Text("Event Type") },
                    shape = RoundedCornerShape(10.dp),
                    readOnly = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.LightGray,
                        unfocusedBorderColor = Color.LightGray,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                        .menuAnchor(),
                    textStyle = TextStyle(fontSize = 16.sp),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    }
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    eventTypes.forEach { item ->
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TextButton(onClick = { isDateDialogShown = true }) {
                    Text("Set Date")
                }

                TextButton(onClick = { isTimeDialogShown = true }) {
                    Text("Set Time")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            OutlinedButton(
                onClick = {
                    if(isToday(selectedDate)){
                        Toast.makeText(context, "Please set a date", Toast.LENGTH_SHORT).show()
                        return@OutlinedButton
                    }
                    if(titleField.value.isEmpty() || descriptionField.value.isEmpty()) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }else{
                        val eventRequest = EventRequest(
                            title = titleField.value,
                            date = convertDateToLong(selectedDate),
                            time = convertTimeToLong(selectedTime),
                            description = descriptionField.value,
                            type = selectedText
                        )
                        addEventViewModel.addEvent(eventRequest)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {
                Text(text = "Add Event")
            }

        }
    }
}
