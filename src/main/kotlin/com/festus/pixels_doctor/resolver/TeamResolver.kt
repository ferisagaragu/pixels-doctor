package com.festus.pixels_doctor.resolver

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.stereotype.Controller
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.transaction.annotation.Transactional
import org.springframework.security.access.prepost.PreAuthorize

import com.festus.pixels_doctor.input.CreateTeamInput
import com.festus.pixels_doctor.repository.ITeamRepository
import com.festus.pixels_doctor.entity.Team
import com.festus.pixels_doctor.input.AddUsersToTeamInput
import com.festus.pixels_doctor.input.UpdateTeamInput
import com.festus.pixels_doctor.ouput.ChartOut
import com.festus.pixels_doctor.repository.IUserRepository

import graphql.GraphQLException
import java.time.Month
import java.util.UUID
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.urx.security.AuthContext

@Controller
class TeamResolver(
	val teamRepository: ITeamRepository,
	val userRepository: IUserRepository,
	val authContext: AuthContext
) {

	@QueryMapping(value = "teams")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	fun findAllTeams(): MutableList<Team> {
		return teamRepository.findAll()
	}

	@QueryMapping
	fun findUserTeam(): Team? {
		val user = userRepository.findById(
			UUID.fromString(authContext.payload.toString())
		).orElseThrow {
			throw GraphQLException("User not found")
		}

		return user.team
	}

	@QueryMapping
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	fun findAllTeamWorks(@Argument year: String): MutableList<ChartOut> {
		val chartOut = mutableListOf<ChartOut>()

		teamRepository.findAll().forEach { team ->
			val chart = ChartOut()
			chart.label = team.name

			Month.values().forEach {
				chart.data.add(
					teamRepository.findAllTeamWorks(
						team.uuid,
						"${if ((it.value) >= 10) "" else "0"}${it.value}-$year"
					)["count"] as Long
				)
			}

			chartOut.add(chart)
		}

		return chartOut
	}

	@Transactional
	@MutationMapping
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	fun createTeam(@Argument team: CreateTeamInput): Team {
		if (teamRepository.existsTeamByName(team.name)) {
			throw GraphQLException("Team already exist")
		}

		val teamOut = Team()
		teamOut.name = team.name

		return teamRepository.save(teamOut)
	}

	@Transactional
	@MutationMapping
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	fun updateTeam(@Argument team: UpdateTeamInput): Team {
		val teamOut = teamRepository.findById(team.uuid).orElseThrow {
			throw GraphQLException("Team already exist")
		}

		if (teamRepository.existsTeamByName(team.name)) {
			throw GraphQLException("Team already exist")
		}

		teamOut.name = team.name

		return teamOut
	}

	@Transactional
	@MutationMapping
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	fun addUserToTeam(@Argument user: AddUsersToTeamInput): Team {
		val teamOut = teamRepository.findById(user.teamUuid).orElseThrow {
			throw GraphQLException("Team not found")
		}

		teamOut.users?.forEach {
			it.team = null
		}

		user.userUuids.forEach {
			userRepository.findById(it).orElseThrow {
				throw GraphQLException("User '${it}' not found")
			}.team = teamOut
		}

		return teamOut
	}

	@Transactional
	@MutationMapping
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	fun removeTeam(@Argument uuid: UUID): Team {
		val teamOut = teamRepository.findById(uuid).orElseThrow {
			throw GraphQLException("Team not found")
		}

		teamOut.users?.forEach {
			userRepository.findById(it.uuid).get().team = null
		}
		teamRepository.delete(teamOut)

		return teamOut
	}

}