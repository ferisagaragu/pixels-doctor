package com.festus.pixels_doctor.ouput

class ChartOut(
	var label: String,
	var data: MutableList<Long>
) {

	constructor(): this(
		label = "",
		data = mutableListOf()
	)

}