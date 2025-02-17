package com.meezzi.deepmedi.presentation.ui.result

import androidx.compose.runtime.Composable

@Composable
fun StatusTag(status: String, backgroundColor: Color, textColor: Color = Color.Black) {
    Box(
        modifier = Modifier
            .background(
                backgroundColor,
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .wrapContentSize()
    ) {
        Text(
            text = status,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

fun getStatusColor(status: String): Color {
    return when (status) {
        "위험" -> Color.Red
        "경고" -> Color.Magenta
        "관심" -> Color.Yellow
        "정상" -> Color.Cyan
        "건강" -> Color.Blue
        else -> Color.Gray
    }
}