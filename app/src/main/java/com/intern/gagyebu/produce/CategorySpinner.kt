package com.intern.gagyebu.produce

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CategorySpinner(
    modifier: Modifier = Modifier,
    viewModel: ProduceActivityViewModel = viewModel(),
    list: Array<Category> = enumValues()
    /*
    preselected: String,
    //파라미터 람다식 공부하기
    onSelectionChanged: (category: String) -> Unit,
     */
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