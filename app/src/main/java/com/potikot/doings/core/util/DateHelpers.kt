package com.potikot.doings.core.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

fun LocalDateTime.toIso(): String =
    format(formatter)

fun String.toLocalDateTime(): LocalDateTime =
    LocalDateTime.parse(this, formatter)