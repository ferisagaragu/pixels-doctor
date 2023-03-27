package com.festus.pixels_doctor.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.util.Date

import java.util.UUID

@Entity
@Table(name = "teams")
class Team(
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	var uuid: UUID,
	var name: String,
	var createDate: Date,

	@OneToMany(mappedBy = "team")
	var users: MutableList<User>?
) {

	constructor(): this(
		uuid = UUID.randomUUID(),
		name = "",
		createDate = Date(),
		users = null
	)

}