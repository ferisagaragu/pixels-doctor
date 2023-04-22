package com.festus.pixels_doctor.repository

import com.festus.pixels_doctor.entity.Team

import java.util.UUID

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ITeamRepository: JpaRepository<Team, UUID> {

	fun existsTeamByName(name: String): Boolean

	@Query("select team from Team team order by team.createDate desc")
	override fun findAll(): MutableList<Team>

	@Query(
		nativeQuery = true,
		value =
			"select count(w.create_date) from teams " +
			"inner join users u on teams.uuid = u.team_uuid " +
			"inner join works w on u.uuid = w.user_uuid " +
			"where teams.uuid = :teamUuid " +
			"and to_char(w.create_date, 'mm-yyyy') = :mothYear"
	)
	fun findAllTeamWorks(teamUuid: UUID, mothYear: String): MutableMap<String, Any>

	@Query(
		nativeQuery = true,
		value =
		"select count(w.create_date) from teams " +
		"inner join users u on teams.uuid = u.team_uuid " +
		"inner join works w on u.uuid = w.user_uuid " +
		"where teams.uuid = :teamUuid " +
		"and u.uuid = :userUuid " +
		"and to_char(w.create_date, 'dd-mm-yyyy') = :dayMothYear"
	)
	fun findAllTeamWorksByDayMonthYear(teamUuid: UUID, userUuid: UUID, dayMothYear: String): MutableMap<String, Any>

}