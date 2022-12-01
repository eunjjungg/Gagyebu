package com.intern.gagyebu.produce

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.intern.gagyebu.R
import com.intern.gagyebu.ui.theme.GagyebuTheme
import kotlinx.coroutines.launch
import java.util.*

/** mainActivity ViewModel**/

class ProduceActivity : ComponentActivity() {
    private lateinit var viewModel: ProduceActivityViewModel

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[ProduceActivityViewModel::class.java]

        if(intent.hasExtra("updateData")){

            viewModel.initUpdate(intent.extras!!)
        }

        lifecycleScope.launch {
            viewModel.eventFlow.collect { event -> handleEvent(event) }
        }

        setContent {
            val areInputsValid by viewModel.areInputsValid.observeAsState(false)

            GagyebuTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    /**
                     * text = mutableStateOf.. 변경 안됨.
                     * 값 변경되면 setContent 재실행 -> 초기화됨
                     * 따라서 remember 이용하여 값 복구할 수 있도록
                     */

                    val source = remember {
                        MutableInteractionSource()
                    }

                    if (source.collectIsPressedAsState().value) {
                        DatePicker()
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Title()

                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(30.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            DateField(source)
                            TitleField()
                            AmountField()
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Text("카테고리")
                                CategorySpinner(enumValues())
                            }

                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Button(
                                onClick = { finish() },
                                Modifier.padding()
                            ) {
                                Text(
                                    text = "취소",
                                    modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                                    style = LocalTextStyle.current.merge(
                                        TextStyle(
                                            fontSize = 20.sp
                                        )
                                    )
                                )
                            }

                            Button(
                                onClick = {viewModel.setData()},
                                enabled = areInputsValid
                            ) {
                                Text(
                                    text = "저장",
                                    modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                                    style = LocalTextStyle.current.merge(
                                        TextStyle(
                                            fontSize = 20.sp
                                        )
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun Title() {
        Column() {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Add, null, modifier = Modifier)

                Text(
                    text = viewModel.activityTitle,
                    modifier = Modifier,
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

    @Composable
    fun DatePicker() {
        val resource = stringResource(id = R.string.show_date_full)
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            LocalContext.current,
            { _, year, month, day ->
                viewModel.updateDate(resource.format(year, month + 1, day))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DATE)
        )
        datePickerDialog.show()
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DateField(source: MutableInteractionSource) {
        val date by viewModel.date.observeAsState("")
        OutlinedTextField(
            value = date,
            onValueChange = { },
            readOnly = true,
            label = { Text("날짜") },
            interactionSource = source,
            leadingIcon = { Icon(Icons.Outlined.DateRange, null, modifier = Modifier) }
        )
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TitleField() {
        val title by viewModel.title.observeAsState("")
        OutlinedTextField(
            value = title,
            //ViewModel StateFlow -> 신뢰할 수 있는 단일 소스. 따라서 {mutable = ..} 문법 사용 안해도 가능
            onValueChange = viewModel::updateTitle,
            label = { Text("제목") },
            leadingIcon = { Icon(Icons.Outlined.Add, null, modifier = Modifier) },
            singleLine = true,
            maxLines = 1
        )

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AmountField() {
        val amount by viewModel.amount.observeAsState("")
        OutlinedTextField(
            value = amount,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = viewModel::updateAmount,
            label = { Text("금액") },
            leadingIcon = { Text(text = "$") },
            singleLine = true,
            maxLines = 1
        )
    }


    @Composable
    fun CategorySpinner(

        list: Array<Category>,
        /*
        preselected: String,
        //파라미터 람다식 공부하기
        onSelectionChanged: (category: String) -> Unit,

         */
        modifier: Modifier = Modifier
    ) {
        val selected by viewModel.category.observeAsState("수입")
        var expanded by remember { mutableStateOf(false) } // initial value

        OutlinedCard(
            modifier = modifier.clickable {
                expanded = !expanded
            }
        ) {

            //spinner 의 열 layout (제목 - 아이콘) 지정
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Top,
            ) {

                //내부 보여지는 값(text) 지정
                Text(
                    text = selected,
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 8.dp)
                )
                //오른쪽 icon
                Icon(Icons.Outlined.ArrowDropDown, null, modifier = Modifier.padding(8.dp))

                //Dropdown 정의
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()   // delete this modifier and use .wrapContentWidth() if you would like to wrap the dropdown menu around the content
                ) {
                    list.forEach { listEntry ->
                        DropdownMenuItem(
                            onClick = {
                                viewModel.updateCategory(listEntry.script)
                                expanded = false
                            },
                            text = {
                                Text(
                                    text = listEntry.script,
                                    modifier = modifier
                                        .wrapContentWidth()  //optional instad of fillMaxWidth

                                )
                            }
                        )
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