package com.festus.pixels_doctor.controller

import com.festus.pixels_doctor.repository.IWorkRepository
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.UUID
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperExportManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.JasperPrint
import net.sf.jasperreports.engine.JasperReport
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController


@CrossOrigin(methods = [RequestMethod.GET])
@RestController
@RequestMapping("/rest/works")
class ReportController(
	val iWorkRepository: IWorkRepository
) {

	@GetMapping("/report/{startDate}/{endDate}/{teamUuid}")
	@Transactional
	fun generateWorkReport(
		@PathVariable startDate: String,
		@PathVariable endDate: String,
		@PathVariable teamUuid: UUID
	): ResponseEntity<InputStreamResource?>? {
		val out: MutableList<Map<String?, String?>> = ArrayList()
		val parameters: MutableMap<String, Any> = HashMap()
		val works = iWorkRepository.findAllWorksBetweenDates(startDate, endDate, teamUuid)
		var levelOneCount = 0
		var levelTwoCount = 0
		var levelThreeCount = 0
		var levelFourCount = 0
		var levelFiveCount = 0
		var rmaNumber = ""

		works.forEach { work ->
			val data: MutableMap<String?, String?> = HashMap()
			data["serial"] = work.serialNumber
			data["description"] = work.description
			data["physicalDamage"] = if (work.physicalDamage == true) "X" else ""
			data["factoryDefect"] = if (work.factoryDefect == true) "X" else ""
			data["beyondRepair"] = if (work.beyondRepair == true) "X" else ""
			data["dt"] = if (work.dt == true) "X" else ""

			data["levelOne"] = ""
			if (work.ledFix in 1..4) {
				data["levelOne"] =  "${work.ledFix}"
				levelOneCount++
			}

			data["levelTwo"] = ""
			if (work.ledFix in 5..8) {
				data["levelTwo"] = "${work.ledFix}"
				levelTwoCount++
			}

			data["levelThree"] = ""
			if (work.ledFix in 9..12) {
				data["levelThree"] = "${work.ledFix}"
				levelThreeCount++
			}

			data["levelFour"] = ""
			if (work.ledFix in 13..16) {
				data["levelFour"] =  "${work.ledFix}"
				levelFourCount++
			}

			data["levelFive"] = ""
			if (work.ledFix >= 17) {
				data["levelFive"] =  "${work.ledFix}"
				levelFiveCount++
			}

			out.add(data)
			rmaNumber = work.getTeam()!!
		}

		val file = ClassPathResource("work-report.jrxml").inputStream
		val jasperReport: JasperReport = JasperCompileManager.compileReport(file)
		val dataSource = JRBeanCollectionDataSource(out)

		parameters["rmaNumber"] = rmaNumber
		parameters["levelOneCount"] = "$levelOneCount"
		parameters["levelTwoCount"] = "$levelTwoCount"
		parameters["levelThreeCount"] = "$levelThreeCount"
		parameters["levelFourCount"] = "$levelFourCount"
		parameters["levelFiveCount"] = "$levelFiveCount"
		parameters["grandTotal"] = "${works.size}"

		val jasperPrint: JasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource)
		val outputStream = ByteArrayOutputStream()
		JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream)
		val targetStream: InputStream = ByteArrayInputStream(outputStream.toByteArray())

		return ResponseEntity.ok()
			.contentType(MediaType.parseMediaType("application/pdf"))
			.header(
				HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + UUID.randomUUID() + ".pdf\""
			).body(InputStreamResource(targetStream))
	}

}