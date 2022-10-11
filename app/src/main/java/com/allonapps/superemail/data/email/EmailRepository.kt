package com.allonapps.superemail.data.email

import com.allonapps.superemail.model.Email
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class EmailRepository(
    private val networkDataSource: EmailDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getEmails(bookmark: String?): List<Email> {
        return withContext(dispatcher) {
            networkDataSource.getEmails(bookmark)
        }
    }

    fun getEmailFlow(bookmark: String?): Flow<List<Email>> {
        return flow {
            emit(networkDataSource.getEmails(bookmark))
        }
    }
}