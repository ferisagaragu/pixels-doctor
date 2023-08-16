package com.festus.pixels_doctor.repository

import com.festus.pixels_doctor.entity.Model
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface IModelRepository: JpaRepository<Model, UUID>