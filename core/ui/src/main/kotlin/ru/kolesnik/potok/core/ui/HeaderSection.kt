package ru.kolesnik.potok.core.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kolesnik.potok.core.designsystem.AppTheme
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Composable
fun AppHeader(
    title: String? = null,
    onBackClick: () -> Unit,
    rightContent: @Composable RowScope.() -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(start = 0.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.size(100.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.core_ui_potok_main),
                contentDescription = "Иконка Потока",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        if (title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .weight(1f)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            content = rightContent
        )
    }
}

@Composable
fun TaskHeaderSection(
    taskOwner: String,
    creationDate: OffsetDateTime?
) {

    Column(
        modifier = Modifier
            .padding(start = 16.dp),
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = "Создатель: $taskOwner",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            text = "Дата создания: ${creationDate?.formatDefault() ?: "Нет даты"}",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

private fun OffsetDateTime.formatDefault() =
    format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))

@Preview(showBackground = true, name = "AppHeader With Title")
@Composable
fun AppHeaderPreview_WithTitle() {
    AppTheme {
        AppHeader(
            title = "Мой заголовок",
            onBackClick = {},
            rightContent = {
                Text(
                    text = "Доска 1 из 3",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        )
    }
}

@Preview(showBackground = true, name = "AppHeader Without Title")
@Composable
fun AppHeaderPreview_WithoutTitle() {
    AppTheme {
        AppHeader(
            title = null,
            onBackClick = {},
            rightContent = {
                TaskHeaderSection("Иван Иванов", OffsetDateTime.now())
            }
        )
    }
}