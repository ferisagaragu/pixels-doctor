package com.festus.pixels_doctor.input

class CreateWorkInput(
	val serialNumber: String,
	val ledFix: Int,
	val model: String,
	val dt: Boolean,
	val description: String,
	val physicalDamage: Boolean,
	val factoryDefect: Boolean,
	val beyondRepair: Boolean,
)
