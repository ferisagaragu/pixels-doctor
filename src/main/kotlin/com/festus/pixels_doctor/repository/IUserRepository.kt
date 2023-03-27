package com.festus.pixels_doctor.repository

import com.festus.pixels_doctor.entity.User
import java.util.Optional

import java.util.UUID

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface IUserRepository: JpaRepository<User, UUID> {
	fun existsByUserName(userName: String): Boolean
	fun findByUserName(userName: String): Optional<User>

	@Query(
		"select user from User user join user.roles role " +
		"where user.uuid != :adminUuid " +
		"order by role.name desc, user.createDate desc"
	)
	fun findAllWithOutAdmin(adminUuid: UUID): List<User>

	@Query(
		"select user from User user join user.roles role " +
		"where user.team = null and role.name = 'ROLE_TECHNICAL'"
	)
	fun findUsersWithOutTeam(): List<User>

	@Modifying
	@Query(
		nativeQuery = true,
		value =
			"UPDATE works SET user_uuid = null WHERE user_uuid = :userUuid"
	)
	fun removeWorkRelationByUser(@Param("userUuid") userUuid: UUID)

	@Modifying
	@Query(
		nativeQuery = true,
		value =
			"UPDATE users SET team_uuid = null WHERE uuid = :userUuid"
	)
	fun removeTeamRelationByUser(@Param("userUuid") userUuid: UUID)

}