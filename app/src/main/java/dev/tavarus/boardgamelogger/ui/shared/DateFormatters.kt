package dev.tavarus.boardgamelogger.ui.shared

import dev.tavarus.boardgamelogger.domain.Play
import java.text.DateFormat
import java.util.Date

fun Play.formatDate(): String =
    DateFormat.getDateInstance(DateFormat.SHORT).format(Date(timestamp))