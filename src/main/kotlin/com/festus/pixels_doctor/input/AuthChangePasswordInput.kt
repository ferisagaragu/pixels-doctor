package com.festus.pixels_doctor.input

import java.util.UUID

class AuthChangePasswordInput(
	val uuid: UUID,
	val oldPassword: String,
	val password: String
)