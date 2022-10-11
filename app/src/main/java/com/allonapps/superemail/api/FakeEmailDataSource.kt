package com.allonapps.superemail.api

import com.allonapps.superemail.data.email.EmailDataSource
import com.allonapps.superemail.model.Email

class FakeEmailDataSource : EmailDataSource {
    override suspend fun getEmails(bookmark: String?): List<Email> {
        return listOf()
    }
}