package ru.kolesnik.potok.core.ui.task
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kolesnik.potok.core.designsystem.AppTheme


@Composable
fun ImportantSwitch(
    initialChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    var checked by remember { mutableStateOf(initialChecked) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Важная задача",
            modifier = Modifier.weight(1f),
            fontSize = 16.sp
        )
        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
                onCheckedChange(it)
            }
        )
    }
}

@Preview(showBackground = true, name = "Switch - Checked")
@Composable
fun ImportantSwitchPreview_Checked() {
    AppTheme {
        ImportantSwitch(
            initialChecked = true,
            onCheckedChange = {}
        )
    }
}

@Preview(showBackground = true, name = "Switch - Unchecked")
@Composable
fun ImportantSwitchPreview_Unchecked() {
    AppTheme {
        ImportantSwitch(
            initialChecked = false,
            onCheckedChange = {}
        )
    }
}
