package com.intern.gagyebu.produce

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AttachMoney
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

            val source = remember {
                MutableInteractionSource()
            }

            if (source.collectIsPressedAsState().value) {
                DatePicker()
            }

            GagyebuTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    Title(modifier = Modifier)

                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        BasicTextField(
                            value = viewModel.date.observeAsState(""),
                            onValueChange = viewModel::updateDate,
                            interaction = source,
                            icon = Icons.Outlined.DateRange,
                            readOnlyValue = true
                        )

                        BasicTextField(
                            value = viewModel.title.observeAsState(""),
                            onValueChange = viewModel::updateTitle,
                            icon = Icons.Outlined.Add
                        )

                        BasicTextField(
                            value = viewModel.amount.observeAsState(""),
                            onValueChange = viewModel::updateAmount,
                            icon = Icons.Outlined.AttachMoney,
                            keyboardType = KeyboardType.Number
                        )

                        CategorySpinner(list = Category.values())

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
fun InitUpdate(viewModel: ProduceActivityViewModel = viewModel(), intent: Intent) {
    var firstInit by rememberSaveable { mutableStateOf(true) }

    if (firstInit) {
        if (intent.hasExtra("updateData")) {
            viewModel.initUpdate(intent.extras!!)
        }
        firstInit = false
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun BasicTextField(
    modifier: Modifier = Modifier,
    value: State<String>,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    interaction: MutableInteractionSource = remember { MutableInteractionSource() },
    keyboardType: KeyboardType = KeyboardType.Text,
    readOnlyValue: Boolean = false
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        modifier = modifier.padding(10.dp),
        value = value.value,
        readOnly = readOnlyValue,
        onValueChange = onValueChange,
        interactionSource = interaction,
        leadingIcon = { Icon(icon, null, modifier = Modifier) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
        })
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Preview
@Composable
fun BasicTextFieldPreview() {
    OutlinedTextField(
        modifier = Modifier.padding(10.dp),
        value = "2022-12-31",
        onValueChange = {},
        leadingIcon = { Icon(Icons.Outlined.Add, null, modifier = Modifier) },

        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White
        )
    )
}