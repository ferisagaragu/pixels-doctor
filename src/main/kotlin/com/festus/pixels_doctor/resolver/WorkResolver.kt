package com.festus.pixels_doctor.resolver

import com.festus.pixels_doctor.entity.Work
import com.festus.pixels_doctor.input.CreateWorkInput
import com.festus.pixels_doctor.repository.IUserRepository
import com.festus.pixels_doctor.repository.IWorkRepository

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.graphql.data.method.annotation.QueryMapping

import graphql.GraphQLException
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
	fun findWorkedMonthsByYear(@Argument year: String): List<String> {
		val user = userRepository.findById(
			UUID.fromString(authContext.payload.toString())
		).orElseThrow {
			GraphQLException("User not found")
		}

		return workRepository.findWorkedMonthsByYear(year)
	}

	@Transactional
	@QueryMapping
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
		workOut.description = work.description
		workOut.user = user

		return workRepository.save(workOut)
	}

}