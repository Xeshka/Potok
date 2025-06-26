package ru.kolesnik.potok.core.ui.assignees

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kolesnik.potok.core.designsystem.theme.AppTheme
import ru.kolesnik.potok.core.ui.getInputFieldBackgroundColor

@Composable
fun TaskTitleField(
    value: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    focusManager: FocusManager
) {
    val inputFieldColor = getInputFieldBackgroundColor()
    
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .focusRequester(focusRequester),
            placeholder = { 
                Text(
                    "Заголовок задачи",
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
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            ),
            singleLine = false,
            maxLines = 2,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            )
        )
        Text(
            text = "${value.length}/200",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
            fontSize = 12.sp,
            color = if (value.length > 200) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}

@Preview(showBackground = true, name = "Light Theme - Empty Title")
@Composable
fun TaskTitleFieldPreview_LightEmpty() {
    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()

    AppTheme(darkTheme = false) {
        TaskTitleField(
            value = "",
            onValueChange = {},
            focusRequester = focusRequester,
            focusManager = focusManager
        )
    }
}

@Preview(showBackground = true, name = "Dark Theme - Empty Title")
@Composable
fun TaskTitleFieldPreview_DarkEmpty() {
    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()

    AppTheme(darkTheme = false) {
        TaskTitleField(
            value = "",
            onValueChange = {},
            focusRequester = focusRequester,
            focusManager = focusManager
        )
    }
}

@Preview(showBackground = true, name = "Light Theme - Filled Title")
@Composable
fun TaskTitleFieldPreview_LightFilled() {
    val text = "Важная задача с нормальным заголовком"
    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()

    AppTheme(darkTheme = false) {
        TaskTitleField(
            value = text,
            onValueChange = {},
            focusRequester = focusRequester,
            focusManager = focusManager
        )
    }
}

@Preview(showBackground = true, name = "Dark Theme - Filled Title")
@Composable
fun TaskTitleFieldPreview_DarkFilled() {
    val text = "Важная задача с нормальным заголовком"
    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()

    AppTheme(darkTheme = true) {
        TaskTitleField(
            value = text,
            onValueChange = {},
            focusRequester = focusRequester,
            focusManager = focusManager
        )
    }
}