package com.intern.gagyebu.add

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.intern.gagyebu.R
import com.intern.gagyebu.add.ui.theme.GagyebuTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AddItemActivity2 : ComponentActivity() {
    private lateinit var viewModel: AddActivityViewModel2

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContent {
            viewModel = ViewModelProvider(this)[AddActivityViewModel2::class.java]

            val areInputsValid by viewModel.areInputsValid.observeAsState(false)

            lifecycleScope.launch {
                viewModel.eventFlow.collect { event -> handleEvent(event) }
            }

            GagyebuTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    /**
                     * text = mutableStateOf.. 변경 안됨.
                     * 값 변경되면 setContent 재실행 -> 초기화됨
                     * 따라서 remember 이용하여 값 복구할 수 있도록
                     */

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

                    Column {
                        DateField(source)
                        TitleField()
                        AmountField()
                        CategorySpinner(enumValues())

                        Button(onClick = { lifecycleScope.launch {  viewModel.save() } }, enabled = areInputsValid) {
                            Text(text = "저장")
                        }
                    }
                }
            }
        }
    }

    //@ReadOnlyComposable
    @Composable
    fun DatePicker() {
        val resource = stringResource(id = R.string.show_date_full)
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            LocalContext.current,
            { _, year, month, day ->
                viewModel.updateDate(resource.format(year, month, day))
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
            label = { Text("제목") }
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
            label = { Text("금액") }
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

        // 선택한 값(초기값), expanded 여부
        //var selected by remember { mutableStateOf(preselected) } // data 저장
        var expanded by remember { mutableStateOf(false) } // initial value


        OutlinedCard(
            modifier = modifier.clickable {
                expanded = !expanded
            }
        ) {

            //spinner 의 열 layout (제목 - 아이콘) 지정
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {

                //내부 보여지는 값(text) 지정
                Text(
                    text = selected,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
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
                                        //.wrapContentWidth()  //optional instad of fillMaxWidth
                                        .fillMaxWidth()
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    private fun handleEvent(event: AddActivityViewModel2.Event) = when (event) {
        is AddActivityViewModel2.Event.Done -> {
            Log.d("saveLog",event.value)
        }
        is AddActivityViewModel2.Event.Error -> {
            Toast.makeText(this, event.value, Toast.LENGTH_SHORT).show()
        }
    }
}