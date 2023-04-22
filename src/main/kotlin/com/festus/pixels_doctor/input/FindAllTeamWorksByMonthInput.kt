package com.festus.pixels_doctor.input

import java.util.UUID

class FindAllTeamWorksByMonthInput(
	val year: Int,
	val month: Int,
	val teamUuid: UUID
)