package xyz.teamgravity.swipetodeleteedit

import java.util.UUID

data class NameModel(
    val id: UUID = UUID.randomUUID(),
    val name: String
)
