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
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.intern.gagyebu.ui.theme.GagyebuTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ProduceActivity : ComponentActivity() {

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: ProduceActivityViewModel by viewModels()

        lifecycleScope.launch {
            viewModel.eventFlow.collect { event -> handleEvent(event) }
        }

        setContent {

            InitUpdate(viewModel = viewModel, intent = intent)

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
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Title(modifier = Modifier)

                        BasicTextField(
                            value = viewModel.date.observeAsState(""),
                            onValueChange = viewModel::updateDate,
                            interaction = source,
                            icon = Icons.Outlined.DateRange,
                            readOnlyValue = true,
                            label = "날짜"
                        )

                        BasicTextField(
                            value = viewModel.title.observeAsState(""),
                            onValueChange = viewModel::updateTitle,
                            icon = Icons.Outlined.Add,
                            label = "제목"
                        )

                        BasicTextField(
                            value = viewModel.amount.observeAsState(""),
                            onValueChange = viewModel::updateAmount,
                            icon = Icons.Outlined.AttachMoney,
                            keyboardType = KeyboardType.Number,
                            label = "금액"
                        )

                        CategorySpinner(list = Category.values())

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.Top
                        ) {
                            CancelButtons(onExitClick = {finish()})
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
fun Title(
    modifier: Modifier = Modifier,
    viewModel: ProduceActivityViewModel = viewModel()
) {
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