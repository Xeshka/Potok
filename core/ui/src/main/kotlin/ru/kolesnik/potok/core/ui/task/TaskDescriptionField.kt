package ru.kolesnik.potok.core.ui.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kolesnik.potok.core.designsystem.AppTheme

private val inputFieldColor = Color(0xFFF8F9FA)

@Composable
fun TaskDescriptionField(
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .heightIn(min = 150.dp, max = 400.dp),
            placeholder = { Text("Описание задачи") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = inputFieldColor,
                unfocusedContainerColor = inputFieldColor,
                disabledContainerColor = inputFieldColor,
                errorContainerColor = inputFieldColor,
            ),
            textStyle = TextStyle(fontSize = 16.sp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Default,
                keyboardType = KeyboardType.Text
            ),
            maxLines = Int.MAX_VALUE
        )
        Text(
            text = "${value.length}/5000",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
            fontSize = 12.sp,
            color = if (value.length > 5000) Color.Red else Color.Gray
        )
    }
}

@Preview(showBackground = true, name = "Empty Description")
@Composable
fun TaskDescriptionFieldPreview_Empty() {
    val textState = remember { mutableStateOf("") }

    AppTheme {
        TaskDescriptionField(
            value = textState.value,
            onValueChange = { textState.value = it }
        )
    }
}

@Preview(showBackground = true, name = "Filled Description")
@Composable
fun TaskDescriptionFieldPreview_Filled() {
    val loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit...".repeat(20)
    val textState = remember { mutableStateOf(loremIpsum) }

    AppTheme {
        TaskDescriptionField(
            value = textState.value,
            onValueChange = { textState.value = it }
        )
    }
}

@Preview(showBackground = true, name = "Overlimit Description")
@Composable
fun TaskDescriptionFieldPreview_Overlimit() {
    val longText = "a".repeat(5001)
    val textState = remember { mutableStateOf(longText) }

    AppTheme {
        TaskDescriptionField(
            value = textState.value,
            onValueChange = { textState.value = it }
        )
    }
}