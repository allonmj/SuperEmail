package com.allonapps.superemail.data.email

import com.allonapps.superemail.model.Email

interface EmailDataSource {
    suspend fun getEmails(bookmark: String?): List<Email>
}