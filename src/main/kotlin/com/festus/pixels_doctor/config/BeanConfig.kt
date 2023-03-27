package com.festus.pixels_doctor.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan("org.urx.exception_graphql")
@ComponentScan("org.urx.security")
@ComponentScan("org.urx.service")
class BeanConfig