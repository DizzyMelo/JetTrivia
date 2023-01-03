package com.study.jettrivia.repository

import com.study.jettrivia.data.DataOrException
import com.study.jettrivia.model.QuestionItem
import com.study.jettrivia.network.QuestionApi
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val api: QuestionApi) {
    private val listOfQuestions = DataOrException<ArrayList<QuestionItem>, Boolean, Exception>()
}