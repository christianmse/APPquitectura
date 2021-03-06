package com.etsisi.appquitectura.data.workers

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.etsisi.appquitectura.domain.usecase.FetchAllQuestionsUseCase
import com.etsisi.appquitectura.presentation.utils.TAG
import com.etsisi.appquitectura.utils.GlideApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class QuestionsWorker (appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params), KoinComponent {

    private val fetchAllQuestionsUseCase: FetchAllQuestionsUseCase by inject()

    companion object {

        private const val WORKER_QUESTIONS_ID = "questionsId"

        fun fetchAllQuestions(context: Context) {
            val constraints: Constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<QuestionsWorker>()
                .setConstraints(constraints)
                .build()


            WorkManager
                .getInstance(context)
                .enqueueUniqueWork(WORKER_QUESTIONS_ID, ExistingWorkPolicy.KEEP, workRequest)
        }
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            launchFetchQuestionsUseCase()
        } catch (e: Exception) {
            Log.e(TAG, e.message.orEmpty())
            Result.failure()
        }
    }

    private suspend fun launchFetchQuestionsUseCase(): Result = suspendCancellableCoroutine { cont ->
        fetchAllQuestionsUseCase.invoke(params = Unit) {
            if (it.isNullOrEmpty()) {
                it.forEach {
                    GlideApp
                        .with(applicationContext)
                        .load(it.getImageFirestorageReference())
                        .preload()
                }
                cont.resume(Result.failure(), null)
            } else {
                cont.resume(Result.success(), null)
            }
        }
    }
}