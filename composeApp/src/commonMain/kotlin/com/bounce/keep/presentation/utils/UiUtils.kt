package com.bounce.keep.presentation.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.todayIn
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun getCurrentDate(): String {

    val format = LocalDate.Format {
        day()
        char(' ')
        monthName(MonthNames.ENGLISH_ABBREVIATED)
        char(' ')
        year()
    }

    val localDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
    return localDate.format(format)
}

fun getRandomColor(): Long {
    val colors = listOf(
        0xFFFFF0F8,
        0xFFEDF4FF,
        0xFFFFEFD0,
        0xFFF4E6D4,
        0xFFEAE8F0,
        0xFFDEF3FF,
        0xFFE6F4E2,
        0xFFF6EFD9,
        0xFFFFF4C8,
        0xFFF6E8FA,
        0xFFE4F5FF,
        0xFFECF7E0,
        0xFFFFFAE5,
        0xFFFCEDED,
        0xFFE6F4E6
    )

    return colors[Random.nextInt(colors.size)]
}