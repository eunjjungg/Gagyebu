package com.intern.gagyebu.produce

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun BasicTextField(
    modifier: Modifier = Modifier,
    value: State<String>,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    interaction: MutableInteractionSource = remember { MutableInteractionSource() },
    keyboardType: KeyboardType = KeyboardType.Text,
    readOnlyValue: Boolean = false,
    label : String
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        modifier = modifier.padding(10.dp),
        label = { Text(label) },
        value = value.value,
        readOnly = readOnlyValue,
        onValueChange = onValueChange,
        interactionSource = interaction,
        leadingIcon = { Icon(icon, null, modifier = Modifier) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        maxLines = 1,
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
        })
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
fun BasicTextFieldPreview() {
    OutlinedTextField(
        modifier = Modifier.padding(10.dp),
        value = "2022-12-31",
        onValueChange = {},
        leadingIcon = { Icon(Icons.Outlined.Add, null, modifier = Modifier) },

        /*
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White
        )

         */
    )
}