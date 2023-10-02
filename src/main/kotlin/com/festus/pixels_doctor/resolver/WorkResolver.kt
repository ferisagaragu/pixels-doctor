package com.festus.pixels_doctor.resolver

import com.festus.pixels_doctor.entity.Work
import com.festus.pixels_doctor.input.CreateWorkInput
import com.festus.pixels_doctor.input.FindDaysOfMonthInput
import com.festus.pixels_doctor.repository.IUserRepository
import com.festus.pixels_doctor.repository.IWorkRepository

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.graphql.data.method.annotation.QueryMapping

import graphql.GraphQLException
import java.time.YearMonth
import java.util.UUID

import org.urx.security.AuthContext
import org.urx.service.Response

@Controller
class WorkResolver(
	val workRepository: IWorkRepository,
	val userRepository: IUserRepository,
	val authContext: AuthContext,
	val response: Response
){

	@Transactional
	@QueryMapping
	fun findWorkedDates(): List<String> {
		val user = userRepository.findById(
			UUID.fromString(authContext.payload.toString())
		).orElseThrow {
			GraphQLException("User not found")
		}

		return workRepository.findWorkedDatesByUserUuid(user.uuid)
	}

	@Transactional
	@QueryMapping
	fun findWorkedYears(): List<String> {
		val user = userRepository.findById(
			UUID.fromString(authContext.payload.toString())
		).orElseThrow {
			GraphQLException("User not found")
		}

		return workRepository.findWorkedYears()
	}

	@Transactional
	@QueryMapping
	fun findDaysOfMonth(@Argument work: FindDaysOfMonthInput): List<String> {
		val out = arrayListOf<String>()

		repeat(YearMonth.of(work.year, work.month).lengthOfMonth()) {
			out.add("${if((it + 1) >= 10) "" else "0"}${it + 1}")
		}

		return out
	}

	@Transactional
	@QueryMapping
	fun findWorkedMonthsByYear(@Argument year: String): List<String> {
		return workRepository.findWorkedMonthsByYear(year)
	}

	@Transactional
	@QueryMapping(value = "works")
	fun findAllWorks(): MutableList<Work> {
		return workRepository.findAllWorks()
	}

	@Transactional
	@QueryMapping()
	fun findWorksByDate(@Argument date: String): MutableList<Work> {
		val user = userRepository.findById(
			UUID.fromString(authContext.payload.toString())
		).orElseThrow {
			GraphQLException("User not found")
		}

		return workRepository.findWorksByDate(user.uuid, date)
	}

	@Transactional
	@QueryMapping
	fun findAllWorksByDate(@Argument date: String): List<LinkedHashMap<String, Any?>> {
		return response.toListMap(
			workRepository.findAllWorksByDate(date)
		).json()
	}

	@Transactional
	@MutationMapping
	fun createWork(@Argument work: CreateWorkInput): Work {
		val user = userRepository.findById(
			UUID.fromString(authContext.payload.toString())
		).orElseThrow {
			GraphQLException("User not found")
		}

		val workOut = Work()
		workOut.serialNumber = work.serialNumber
		workOut.ledFix = work.ledFix
		workOut.model = work.model
		workOut.dt = work.dt
		workOut.description = work.description
		workOut.physicalDamage = work.physicalDamage
		workOut.factoryDefect = work.factoryDefect
		workOut.beyondRepair = work.beyondRepair
		workOut.user = user
		workOut.team = user.team

		return workRepository.save(workOut)
	}

	@Transactional
	@MutationMapping
	fun deleteWork(@Argument uuid: UUID): Work {
		val work = workRepository.findById(uuid).orElseThrow {
			GraphQLException("Work not found")
		}

		work.user = null
		val workOut = workRepository.save(work)
		workRepository.delete(workOut)

		return workOut
	}

}