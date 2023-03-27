package com.festus.pixels_doctor.resolver

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.stereotype.Controller
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.transaction.annotation.Transactional

import com.festus.pixels_doctor.entity.User
import com.festus.pixels_doctor.input.UpdateUserInput
import com.festus.pixels_doctor.repository.IRoleRepository
import com.festus.pixels_doctor.repository.IUserRepository

import graphql.GraphQLException
import java.util.UUID
import org.springframework.security.access.prepost.PreAuthorize
import org.urx.security.AuthContext
import org.urx.service.Response

@Controller
class UserResolver(
	val userRepository: IUserRepository,
	val roleRepository: IRoleRepository,
	val response: Response,
	val authContext: AuthContext
) {

	@QueryMapping(value = "users")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	fun findAllUsers(): List<LinkedHashMap<String, Any?>> {
		return response.toListMap(
			userRepository.findAllWithOutAdmin(
				UUID.fromString(authContext.payload.toString())
			)
		).json()
	}

	@QueryMapping(value = "findUsersWithOutTeam")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	fun findUsersWithOutTeam(): List<User> {
		return userRepository.findUsersWithOutTeam()
	}

	@Transactional
	@MutationMapping
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	fun updateUser(@Argument user: UpdateUserInput): User {
		val userUpdate = userRepository.findById(user.uuid).orElseThrow {
			throw GraphQLException("User not found")
		}

		userUpdate.name = user.name
		userUpdate.roles.clear()
		userUpdate.roles.add(
			roleRepository.findByName(user.role).orElseThrow {
				throw GraphQLException("Role not found")
			}
		)

		return userUpdate
	}

	@Transactional
	@MutationMapping
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	fun removeUser(@Argument uuid: String): User {
		val userRemove = userRepository.findById(UUID.fromString(uuid)).orElseThrow {
			throw GraphQLException("User not found")
		}

		userRemove.roles = mutableListOf()
		userRepository.removeWorkRelationByUser(userRemove.uuid)
		userRepository.removeTeamRelationByUser(userRemove.uuid)
		userRepository.delete(userRemove)

		return userRemove
	}

}