package com.intern.gagyebu.produce

import android.app.DatePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.intern.gagyebu.R
import java.util.*
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DatePicker(viewModel: ProduceActivityViewModel = viewModel()) {
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