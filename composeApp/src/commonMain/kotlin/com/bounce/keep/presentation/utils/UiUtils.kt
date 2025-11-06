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
        0xFFFBE4FF,
        0xFFD4E7FE,
        0xFFFFD6A5,
        0xFFE7CBA9,
        0xFFB9B4C7,
        0xFF99DBF5,
        0xFFA7D397,
        0xFFE7D4B5,
        0xFFFFD966,
        0xFFE2BBE9,
        0xFFB8E8FC,
        0xFFBCE29E,
        0xFFFFF6BD,
        0xFFF3C5C5,
        0xFFC1E1C1
    )

    return colors[Random.nextInt(colors.size)]
}