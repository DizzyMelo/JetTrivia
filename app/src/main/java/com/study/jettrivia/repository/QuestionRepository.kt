package com.study.jettrivia.repository

import android.util.Log
import com.study.jettrivia.data.DataOrException
import com.study.jettrivia.model.QuestionItem
import com.study.jettrivia.network.QuestionApi
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val api: QuestionApi) {
    private val dataOrException = DataOrException<ArrayList<QuestionItem>, Boolean, Exception>()

    suspend fun getAllQuestions(): DataOrException<ArrayList<QuestionItem>, Boolean, Exception> {
        try {
            dataOrException.loading = true
            dataOrException.data = api.getAllQuestions()
        } catch (exception: Exception) {
            dataOrException.e = exception
            Log.d(QuestionRepository::class.java.name, "getAllQuestions: ${exception.localizedMessage}")
        } finally {
            dataOrException.loading = false
        }

        return dataOrException
    }

}