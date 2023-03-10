package com.study.jettrivia.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.study.jettrivia.model.QuestionItem
import com.study.jettrivia.screens.QuestionViewModel
import com.study.jettrivia.util.AppColors

@Composable
fun Questions(viewModel: QuestionViewModel) {
    val questions = viewModel.data.value.data?.toMutableList()

    val questionIndex = remember {
        mutableStateOf(0)
    }
    if (viewModel.data.value.loading == true) {
        CircularProgressIndicator()
    } else {
        val question = try {
            questions?.get(questionIndex.value)
        } catch (ex: Exception) {
            null
        }

        if (questions != null && question != null) {
            QuestionDisplay(question, questionIndex, questions.size) {
                questionIndex.value += 1
            }
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun QuestionDisplay(
    question: QuestionItem,
    questionIndex: MutableState<Int>,
    total: Int,
    onNextClick: (Int) -> Unit = {}
) {
    val choicesState = remember(question) {
        question.choices.toMutableList()
    }

    val answerState = remember(question) {
        mutableStateOf<Int?>(null)
    }

    val correctAnswerState = remember(question) {
        mutableStateOf<Boolean?>(null)
    }

    val updateAnswer: (Int) -> Unit = remember(question) {
        {
            answerState.value = it
            correctAnswerState.value = choicesState[it] == question.answer
        }
    }

    val dashedPathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), phase = 0f)
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = AppColors.mDarkPurple
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            if (questionIndex.value >= 3) ScoreMeter(score = questionIndex.value)
            QuestionTracker(current = questionIndex.value + 1, total = total)
            DottedLine(pathEffect = dashedPathEffect)
            Text(
                text = question.question,
                modifier = Modifier
                    .padding(6.dp)
                    .align(Alignment.Start)
                    .fillMaxHeight(fraction = .3f),
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 22.sp,
                color = AppColors.mOffWhite
            )

            // Choices
            choicesState.forEachIndexed { index, answerText -> 
                Row(
                    modifier = Modifier
                        .padding(3.dp)
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(
                            width = 4.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(AppColors.mOffDarkPurple, AppColors.mOffDarkPurple)
                            ),
                            shape = RoundedCornerShape(15.dp)
                        )
                        .clip(
                            RoundedCornerShape(
                                topStartPercent = 50,
                                topEndPercent = 50,
                                bottomEndPercent = 50,
                                bottomStartPercent = 50
                            )
                        )
                        .background(Color.Transparent),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = answerState.value == index,
                        onClick = { updateAnswer(index) },
                        modifier = Modifier.padding(start = 16.dp),
                        colors = RadioButtonDefaults
                            .colors(
                                selectedColor =
                                if (correctAnswerState.value == true && index == answerState.value)
                                    Color.Green.copy(alpha = .2f)
                                else
                                    Color.Red.copy(alpha = .6f)
                            )
                    )
                    val annotatedString = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Light,
                                color =
                                if (correctAnswerState.value == true && index == answerState.value)
                                    Color.Green
                                else if (correctAnswerState.value == false && index == answerState.value)
                                    Color.Red
                                else
                                    AppColors.mOffWhite,
                                fontSize = 17.sp
                        )) {
                            append(answerText)
                        }
                    }

                    Text(text = annotatedString)

                }
            }

            Button(
                onClick = { onNextClick.invoke(questionIndex.value) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                shape = CircleShape.copy(all = CornerSize(10.dp)),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = AppColors.mLightBlue,
                    contentColor = AppColors.mOffWhite
                )
            ) {
                Text(text = "Next", modifier = Modifier.padding(4.dp), fontSize = 17.sp)
            }

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

@Preview
@Composable
fun ScoreMeter(score: Int = 12) {
    val gradient = Brush.linearGradient(colors = listOf(Color(0xFFF95075),Color(0xFFBE6BE5)))
    val progressFactor = remember(score) {
        mutableStateOf(score * .005f)
    }
    Row(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .height(45.dp)
            .border(
                width = 4.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        AppColors.mLightPurple,
                        AppColors.mLightPurple
                    )
                ),
                shape = CircleShape
            )
            .clip(
                shape = RoundedCornerShape(
                    topStartPercent = 50,
                    topEndPercent = 50,
                    bottomStartPercent = 50,
                    bottomEndPercent = 50
                )
            )
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            contentPadding = PaddingValues(1.dp),
            onClick = { },
            modifier = Modifier
                .fillMaxWidth(fraction = progressFactor.value)
                .background(brush = gradient),
            enabled = false,
            elevation = null,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                disabledBackgroundColor = Color.Transparent
            )
        ) {
            Text(
                text = (score * 10).toString(),
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(23.dp))
                    .fillMaxHeight(.87f)
                    .fillMaxWidth()
                    .padding(6.dp),
                color = AppColors.mOffWhite,
                textAlign = TextAlign.Center
            )
        }
    }
}