package com.festus.pixels_doctor.resolver

import com.festus.pixels_doctor.entity.Model
import com.festus.pixels_doctor.entity.Team
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.stereotype.Controller
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.transaction.annotation.Transactional

import com.festus.pixels_doctor.entity.User
import com.festus.pixels_doctor.entity.Work
import com.festus.pixels_doctor.input.AuthChangePasswordInput
import com.festus.pixels_doctor.input.AuthSingInInput
import com.festus.pixels_doctor.input.CreateWorkInput
import com.festus.pixels_doctor.ouput.SingInOut
import com.festus.pixels_doctor.repository.IModelRepository

import graphql.GraphQLException
import java.util.UUID
import org.springframework.graphql.data.method.annotation.QueryMapping

@Controller
class ModelResolver(
	val modelRepository: IModelRepository,
) {

	@QueryMapping(value = "models")
	fun findAllTeams(): MutableList<Model> {
		return modelRepository.findAll()
	}

	@Transactional
	@MutationMapping
	fun createModel(@Argument name: String): Model {
		val model = Model(
			uuid =  UUID.randomUUID(),
			name = name
		)

		return modelRepository.save(model)
	}

	@Transactional
	@MutationMapping
	fun removeModel(@Argument uuid: UUID): Model? {
		val model = modelRepository.findById(uuid).orElseThrow {
			GraphQLException("Model not found")
		}

		modelRepository.delete(model)

		return model
	}

}