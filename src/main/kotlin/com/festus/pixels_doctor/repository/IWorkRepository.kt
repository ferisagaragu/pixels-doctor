package com.festus.pixels_doctor.repository

import com.festus.pixels_doctor.entity.Work
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

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
			"where to_char(create_date, 'mm-yyyy') = :monthYear " +
			"order by works.create_date desc"
	)
	fun findAllWorksByDate(monthYear: String): MutableList<Work>

}