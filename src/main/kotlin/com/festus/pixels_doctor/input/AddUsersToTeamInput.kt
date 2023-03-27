package com.festus.pixels_doctor.input

import java.util.UUID

class AddUsersToTeamInput(
	val teamUuid: UUID,
	val userUuids: MutableList<UUID>
)