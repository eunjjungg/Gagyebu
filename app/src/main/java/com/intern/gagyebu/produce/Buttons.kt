package com.intern.gagyebu.produce

import android.app.Activity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CancelButtons(
    modifier: Modifier = Modifier,
    viewModel: ProduceActivityViewModel = viewModel(),
    onExitClick: () -> Unit
) {
    Button(
        onClick = {onExitClick()},
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
}

@Composable
fun SaveButtons(
    modifier: Modifier = Modifier,
    viewModel: ProduceActivityViewModel = viewModel()
) {
    val state = viewModel.areInputsValid.observeAsState(false)

    Button(
        onClick = { viewModel.setData() },
        enabled = state.value
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