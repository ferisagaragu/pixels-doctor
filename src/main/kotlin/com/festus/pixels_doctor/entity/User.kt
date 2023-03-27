package com.festus.pixels_doctor.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

import java.util.Date
import java.util.UUID

import org.urx.service.annotation.Key

@Entity
@Table(name = "users")
class User(
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	var uuid: UUID,
	var name: String,
	var userName: String,
	var password: String,
	var createDate: Date,

	@ManyToMany
	var roles: MutableList<Role>,

	@ManyToOne
	var team: Team?,

	@OneToMany(mappedBy = "user")
	var works: MutableList<Work>?
) {

	constructor(): this(
		uuid = UUID.randomUUID(),
		name = "",
		userName = "",
		password = "",
		createDate = Date(),
		roles = mutableListOf(),
		team = null,
		works = null
	)

	@Key(name = "role", autoCall = true)
	fun role(): String {
		return roles.first().name
	}

}