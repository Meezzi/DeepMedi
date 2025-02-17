package com.meezzi.deepmedi.presentation.ui.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meezzi.deepmedi.R
import com.meezzi.deepmedi.presentation.ui.camera.LoadingIndicator
import com.meezzi.deepmedi.presentation.ui.camera.UploadViewModel

@Composable
fun ResultScreen(uploadViewModel: UploadViewModel = hiltViewModel()) {

    val userInfo by uploadViewModel.userInfo.collectAsState()
    val isLoading by uploadViewModel.isLoading.collectAsState()

    if (isLoading) {
        LoadingIndicator()
        return
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp)
        ) {

            Text(
                text = userInfo.healthStatus.mainMessage,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row {
                Text(
                    "${userInfo.age}세 ${userInfo.gender} - ",
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    stringResource(R.string.result_title),
                    color = Color.Gray,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            HeartRateItem(
                label = stringResource(R.string.heart_rate),
                value = userInfo.healthStatus.heartRate.toString(),
                status = userInfo.healthStatus.heartRateStatus
            )
        }
    }
}

@Composable
fun HeartRateItem(label: String, value: String, status: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    label,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.titleMedium
                )

                StatusTag(
                    status = status,
                    backgroundColor = getStatusColor(status)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "${value}회 ",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "/ 분 ",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    stringResource(R.string.result_range_heart_rate),
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

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