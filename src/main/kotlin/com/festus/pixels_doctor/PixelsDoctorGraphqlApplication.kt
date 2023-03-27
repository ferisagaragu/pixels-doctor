package com.festus.pixels_doctor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

import org.urx.security.properties.UrxniumProperties
import org.urx.security.properties.UrxniumSecurityDevProperties
import org.urx.security.properties.UrxniumSecurityProperties

@SpringBootApplication
@EnableConfigurationProperties(value = [
	UrxniumProperties::class,
	UrxniumSecurityProperties::class,
	UrxniumSecurityDevProperties::class
])
class LedDocGraphqlApplication

fun main(args: Array<String>) {
	runApplication<LedDocGraphqlApplication>(*args)
}
