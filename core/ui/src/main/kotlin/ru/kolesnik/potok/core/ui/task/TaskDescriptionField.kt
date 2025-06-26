package ru.kolesnik.potok.core.ui.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kolesnik.potok.core.designsystem.AppTheme
import ru.kolesnik.potok.core.ui.getInputFieldBackgroundColor

@Composable
fun TaskDescriptionField(
    value: String,
    onValueChange: (String) -> Unit
) {
    val inputFieldColor = getInputFieldBackgroundColor()
    
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .heightIn(min = 150.dp, max = 400.dp),
            placeholder = { 
                Text(
                    "Описание задачи",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ) 
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = inputFieldColor,
                unfocusedContainerColor = inputFieldColor,
                disabledContainerColor = inputFieldColor,
                errorContainerColor = inputFieldColor,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            ),
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            ),
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
            color = if (value.length > 5000) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}

@Preview(showBackground = true, name = "Light Theme - Empty Description")
@Composable
fun TaskDescriptionFieldPreview_LightEmpty() {
    val textState = remember { mutableStateOf("") }

    AppTheme(darkTheme = false) {
        TaskDescriptionField(
            value = textState.value,
            onValueChange = { textState.value = it }
        )
    }
}

@Preview(showBackground = true, name = "Dark Theme - Empty Description")
@Composable
fun TaskDescriptionFieldPreview_DarkEmpty() {
    val textState = remember { mutableStateOf("") }

    AppTheme(darkTheme = true) {
        TaskDescriptionField(
            value = textState.value,
            onValueChange = { textState.value = it }
        )
    }
}

@Preview(showBackground = true, name = "Light Theme - Filled Description")
@Composable
fun TaskDescriptionFieldPreview_LightFilled() {
    val loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
    val textState = remember { mutableStateOf(loremIpsum) }

    AppTheme(darkTheme = false) {
        TaskDescriptionField(
            value = textState.value,
            onValueChange = { textState.value = it }
        )
    }
}

@Preview(showBackground = true, name = "Dark Theme - Filled Description")
@Composable
fun TaskDescriptionFieldPreview_DarkFilled() {
    val loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
    val textState = remember { mutableStateOf(loremIpsum) }

    AppTheme(darkTheme = true) {
        TaskDescriptionField(
            value = textState.value,
            onValueChange = { textState.value = it }
        )
    }
}