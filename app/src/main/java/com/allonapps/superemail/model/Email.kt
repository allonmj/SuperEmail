package com.allonapps.superemail.model

data class Email(
    val sender: String,
    val subject: String,
    val message: String,
    val timeDisplay: String
)