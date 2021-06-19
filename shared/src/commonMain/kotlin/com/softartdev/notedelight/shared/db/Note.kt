package com.softartdev.notedelight.shared.db

import kotlin.Long
import kotlin.String
import kotlinx.datetime.LocalDateTime

public data class Note(
    public val id: Long,
    public val title: String,
    public val text: String,
    public val dateCreated: LocalDateTime,
    public val dateModified: LocalDateTime
) {
    public override fun toString(): String = """
  |Note [
  |  id: $id
  |  title: $title
  |  text: $text
  |  dateCreated: $dateCreated
  |  dateModified: $dateModified
  |]
  """.trimMargin()
}
