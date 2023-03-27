package com.festus.pixels_doctor.resolver

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.stereotype.Controller
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.transaction.annotation.Transactional

import com.festus.pixels_doctor.entity.User
import com.festus.pixels_doctor.input.AuthChangePasswordInput
import com.festus.pixels_doctor.input.AuthSingInInput
import com.festus.pixels_doctor.input.AuthSingUpInput
import com.festus.pixels_doctor.ouput.SingInOut
import com.festus.pixels_doctor.repository.IRoleRepository
import com.festus.pixels_doctor.repository.IUserRepository

import graphql.GraphQLException

import org.urx.security.AuthHelper
import org.urx.security.JwtProvider

@Controller
class AuthResolver(
	val userRepository: IUserRepository,
	val roleRepository: IRoleRepository,
	val passwordEncoder: PasswordEncoder,
	val jwtProvider: JwtProvider,
	val authHelper: AuthHelper
) {

	@Transactional
	@MutationMapping
	fun signUp(@Argument user: AuthSingUpInput): User {
		if (userRepository.existsByUserName(user.userName)) {
			throw GraphQLException("Username already exist")
		}

		val userOut = User()
		userOut.name = user.name
		userOut.userName = user.userName
		userOut.password = passwordEncoder.encode(user.password)
		userOut.roles.add(
			roleRepository.findByName(user.role).orElseThrow {
				throw GraphQLException("Role is invalid")
			}
		)

		return userRepository.save(userOut)
	}

	@Transactional
	@MutationMapping
	fun signIn(@Argument user: AuthSingInInput): SingInOut {
		val erroMessage = GraphQLException("The user name or password is incorrect, Try again")

		val userOut = userRepository.findByUserName(user.userName).orElseThrow {
			throw erroMessage
		}

		if (!passwordEncoder.matches(user.password, userOut.password))
			throw erroMessage

		return SingInOut(
			userOut.uuid,
			userOut.name,
			userOut.userName,
			userOut.roles.first().name,
			jwtProvider.generateJwtToken(
				authHelper.generateAuthentication(
					userOut.userName,
					userOut.roles.map { role -> SimpleGrantedAuthority(role.name) },
					userOut.uuid
				)
			)["token"] as String
		)
	}

	@Transactional
	@MutationMapping
	fun changePassword(@Argument user: AuthChangePasswordInput): User {
		val erroMessage = GraphQLException("The password is incorrect, Try again")

		val userOut = userRepository.findById(user.uuid).orElseThrow {
			throw erroMessage
		}

		if (!passwordEncoder.matches(user.oldPassword, userOut.password))
			throw erroMessage

		userOut.password = passwordEncoder.encode(user.password)

		return userOut
	}

}