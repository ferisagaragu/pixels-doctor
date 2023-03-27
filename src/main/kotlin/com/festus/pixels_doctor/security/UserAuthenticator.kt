package com.festus.pixels_doctor.security

import com.festus.pixels_doctor.repository.IUserRepository
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

import org.urx.security.interfaces.SecurityAuthentication

@Component
@EnableWebSecurity
@EnableMethodSecurity
class UserAuthenticator(
	val userRepository: IUserRepository
): SecurityAuthentication {

	override fun validateUserName(userName: String): Boolean {
		return userRepository.existsByUserName(userName)
	}

	override fun validateAuthorities(authorities: List<String>): Boolean {
		return true
	}

	override fun customAuthentication(
		token: String,
		request: HttpServletRequest,
		response: HttpServletResponse
	): Authentication? {
		return null
	}

}