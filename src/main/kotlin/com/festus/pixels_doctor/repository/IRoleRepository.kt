package com.festus.pixels_doctor.repository

import com.festus.pixels_doctor.entity.Role

import java.util.Optional
import java.util.UUID

import org.springframework.data.jpa.repository.JpaRepository

interface IRoleRepository: JpaRepository<Role, UUID> {
	fun findByName(name: String): Optional<Role>
}