package com.intern.gagyebu.produce

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.intern.gagyebu.ui.theme.GagyebuTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ProduceActivity2 : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: ProduceActivityViewModel by viewModels()
            InitUpdate(viewModel = viewModel, intent = intent)

            lifecycleScope.launch {
                viewModel.eventFlow.collect { event -> handleEvent(event) }
            }

            GagyebuTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column() {
                        Title()
                        DateField()
                        TitleField()
                        AmountField()
                        CategorySpinner()

                        Row {
                            CancelButtons()
                            SaveButtons()
                        }
                    }
                }
            }
        }
    }

    private fun handleEvent(event: ProduceActivityViewModel.Event) = when (event) {
        is ProduceActivityViewModel.Event.Done -> {
            Log.d("saveLog", event.value)
            finish()
        }
        is ProduceActivityViewModel.Event.Error -> {
            Toast.makeText(this, event.value, Toast.LENGTH_SHORT).show()
        }
    }

}
@Composable
fun InitUpdate(viewModel: ProduceActivityViewModel = viewModel(), intent: Intent){
    var firstInit by rememberSaveable { mutableStateOf(true) }

    if (firstInit) {
        if (intent.hasExtra("updateData")) {
            viewModel.initUpdate(intent.extras!!)
        }
        firstInit = false
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateField(modifier: Modifier = Modifier,
              viewModel: ProduceActivityViewModel = viewModel()) {
    val date by viewModel.date.observeAsState("")
    val interactionSource = remember { MutableInteractionSource() }

        OutlinedTextField(
            value = date,
            onValueChange = { },
            readOnly = true,
            label = { Text("날짜") },
            interactionSource = interactionSource,
            leadingIcon = { Icon(Icons.Outlined.DateRange, null, modifier = Modifier) }
        )

    if (interactionSource.collectIsPressedAsState().value) {
        DatePicker()
    }
}

@Composable
fun Title(modifier: Modifier = Modifier,
            viewModel: ProduceActivityViewModel = viewModel()) {
    Column() {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.Add, null, modifier = Modifier)

            Text(
                text = viewModel.activityTitle,
                modifier = modifier,
                style = LocalTextStyle.current.merge(
                    TextStyle(
                        fontSize = 50.sp
                    )
                )
            )
        }
        Canvas(modifier = Modifier.fillMaxWidth()) {
            drawLine(
                start = Offset(x = 0f, y = 0f),
                end = Offset(x = size.width, y = 0f),
                color = Color.Black,
                strokeWidth = 1f
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TitleField(modifier: Modifier = Modifier,
               viewModel: ProduceActivityViewModel = viewModel()) {
    val title by viewModel.title.observeAsState("")

    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = title,
        //ViewModel StateFlow -> 신뢰할 수 있는 단일 소스. 따라서 {mutable = ..} 문법 사용 안해도 가능
        onValueChange = viewModel::updateTitle,
        label = { Text("제목") },
        leadingIcon = { Icon(Icons.Outlined.Add, null, modifier = modifier) },
        singleLine = true,
        maxLines = 1,
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
        })
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AmountField(modifier: Modifier = Modifier,
                viewModel: ProduceActivityViewModel = viewModel()) {

    val amount by viewModel.amount.observeAsState("")
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = amount,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        onValueChange = viewModel::updateAmount,
        label = { Text("금액") },
        leadingIcon = { Text(text = "$") },
        singleLine = true,
        maxLines = 1,
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
        })
    )
}





