package com.study.jettrivia.component

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.study.jettrivia.screens.QuestionViewModel
import com.study.jettrivia.util.AppColors

@Composable
fun Questions(viewModel: QuestionViewModel) {
    val questions = viewModel.data.value.data?.toMutableList()

    if (viewModel.data.value.loading == true) {
        CircularProgressIndicator()
    } else {
        Log.d("DEBUG Q", "Questions: ${questions?.size}")
    }
}

@Preview(showBackground = true)
@Composable
fun QuestionDisplay() {
    val dashedPathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), phase = 0f)
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        color = AppColors.mDarkPurple
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            QuestionTracker()
            DottedLine(pathEffect = dashedPathEffect)

        }
    }
}

@Composable
fun DottedLine(pathEffect: PathEffect) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp),
    ) {
        drawLine(
            color = AppColors.mLightGray,
            start = Offset(0f, 0f),
            end = Offset(size.width, y = 0f),
            pathEffect = pathEffect
        )
    }
}

//@Preview
@Composable
fun QuestionTracker(current: Int = 1, total: Int = 4056) {
    Text(text = buildAnnotatedString {
        withStyle(style = ParagraphStyle(textIndent = TextIndent.None)) {
            withStyle(
                style = SpanStyle(
                    color = AppColors.mLightGray,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append("Question $current/")
                withStyle(style = SpanStyle(
                    color = AppColors.mLightGray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )) {
                    append("$total")
                }
            }
        }
    }, modifier = Modifier.padding(20.dp))
}