package com.festus.pixels_doctor.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

import com.festus.pixels_doctor.entity.Work

import java.util.UUID

interface IWorkRepository: JpaRepository<Work, UUID> {

	@Query(
		nativeQuery = true,
		value =
		"select distinct to_char(works.create_date, 'MM-YYYY') from works " +
			"inner join users u on u.uuid = works.user_uuid " +
			"where user_uuid = :userUuid order by to_char(works.create_date, 'MM-YYYY') desc"
	)
	fun findWorkedDatesByUserUuid(userUuid: UUID): List<String>

	@Query(
		nativeQuery = true,
		value =
		"select works.* from works " +
			"inner join users u on u.uuid = works.user_uuid " +
			"where user_uuid = :userUuid " +
			"and to_char(works.create_date, 'MM-YYYY') = :date " +
			"order by works.create_date"
	)
	fun findWorksByDate(userUuid: UUID, date: String): MutableList<Work>

	@Query(
		nativeQuery = true,
		value =
		"select DISTINCT to_char(create_date, 'YYYY') from works " +
			"order by to_char(create_date, 'YYYY') desc"
	)
	fun findWorkedYears(): List<String>

	@Query(
		nativeQuery = true,
		value =
		"select DISTINCT to_char(create_date, 'MM') from works WHERE " +
			"to_char(create_date, 'yyyy') = :year " +
			"ORDER BY to_char(create_date, 'MM') desc"
	)
	fun findWorkedMonthsByYear(year: String): List<String>

	@Query(
		nativeQuery = true,
		value =
		"select works.* from works " +
			"inner join users u on works.user_uuid = u.uuid " +
			"where to_char(works.create_date, 'mm-yyyy') = :monthYear " +
			"order by works.create_date desc"
	)
	fun findAllWorksByDate(monthYear: String): MutableList<Work>

	@Query(
		nativeQuery = true,
		value =
		"select works.* from works " +
			"inner join users u on works.user_uuid = u.uuid " +
			"order by works.create_date desc"
	)
	fun findAllWorks(): MutableList<Work>

	@Query(
		nativeQuery = true,
		value =
		"select w.* from works w " +
		"inner join teams t on t.uuid = w.team_uuid " +
		"where team_uuid = :teamUuid and w.create_date between " +
		"to_timestamp(:startDate, 'YYYY-MM-DD') and " +
		"to_timestamp(:endDate, 'YYYY-MM-DD') "
	)
	fun findAllWorksBetweenDates(
		startDate: String,
		endDate: String,
		teamUuid: UUID
	): List<Work>

}