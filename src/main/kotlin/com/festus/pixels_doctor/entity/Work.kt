package com.festus.pixels_doctor.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Lob
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

import java.util.Date
import java.util.UUID
import org.urx.service.annotation.Key
import org.urx.service.enums.DefaultValue

@Entity
@Table(name = "works")
class Work(
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	var uuid: UUID,
	var serialNumber: String,
	var ledFix: Int,
	@Lob
	var description: String?,
	var createDate: Date,

	@ManyToOne
	var user: User?
) {

	constructor(): this(
		uuid = UUID.randomUUID(),
		serialNumber = "",
		ledFix = 0,
		description = null,
		createDate = Date(),
		user = null
	)


	@Key(name = "user", autoCall = true, DefaultValue.NULL)
	fun getUser(): String? {
		return user?.name
	}

	@Key(name = "team", autoCall = true, DefaultValue.NULL)
	fun getTeam(): String? {
		return user?.team?.name
	}

}