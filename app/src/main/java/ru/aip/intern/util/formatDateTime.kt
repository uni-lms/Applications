package ru.aip.intern.util

import android.text.format.DateUtils
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun LocalDateTime.format(): String {
    return this.atZone(ZoneId.of("UTC"))
        .withZoneSameInstant(
            ZoneId.systemDefault()
        ).format(
            DateTimeFormatter.ofLocalizedDateTime(
                FormatStyle.SHORT
            )
        )
}

fun LocalDateTime.formatTime(): String {
    return this.atZone(ZoneId.of("UTC"))
        .withZoneSameInstant(
            ZoneId.systemDefault()
        ).format(
            DateTimeFormatter.ofPattern("HH:mm")
        )
}

fun LocalDateTime.formatDate(): String {
    return this.atZone(ZoneId.of("UTC"))
        .withZoneSameInstant(
            ZoneId.systemDefault()
        ).format(
            DateTimeFormatter.ofPattern("d.MM.y")
        )
}

fun LocalDateTime.formatRelative(): String {
    return DateUtils.getRelativeTimeSpanString(
        this.atZone(ZoneId.of("UTC"))
            .withZoneSameInstant(
                ZoneId.systemDefault()
            ).toInstant()
            .toEpochMilli(),
        System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS
    ).toString()
}