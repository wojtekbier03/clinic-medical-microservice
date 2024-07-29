package com.wojtekbier03.clinic_medical_microservice.controller;

import com.wojtekbier03.clinic_medical_microservice.dto.AppointmentDto;
import com.wojtekbier03.clinic_medical_microservice.exception.ClinicMedicalMicroserviceException;
import com.wojtekbier03.clinic_medical_microservice.exception.DoctorNotFoundException;
import com.wojtekbier03.clinic_medical_microservice.exception.PatientNotFoundException;
import com.wojtekbier03.clinic_medical_microservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @Operation(summary = "Retrieve appointments by patient ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of appointments for the patient"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/{patientId}/appointments")
    @ResponseStatus(HttpStatus.OK)
    public List<AppointmentDto> getAppointmentByPatientId(
            @Parameter(description = "ID of the patient") @PathVariable Long patientId) {
        try {
            return patientService.getAppointmentByPatientId(patientId);
        } catch (PatientNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @Operation(summary = "Book an appointment for a patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment successfully booked"),
            @ApiResponse(responseCode = "400", description = "Invalid appointment data or patient not found")
    })
    @PostMapping("/{patientId}/appointments")
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentDto bookAppointment(
            @RequestBody AppointmentDto appointmentDto,
            @Parameter(description = "ID of the patient") @PathVariable Long patientId) {
        try {
            return patientService.bookAppointment(appointmentDto, patientId);
        } catch (PatientNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @Operation(summary = "Retrieve appointments by doctor ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of appointments for the doctor"),
            @ApiResponse(responseCode = "404", description = "Doctor not found")
    })
    @GetMapping("/doctors/{doctorId}/appointments")
    @ResponseStatus(HttpStatus.OK)
    public List<AppointmentDto> getAppointmentsByDoctorId(
            @Parameter(description = "ID of the doctor") @PathVariable Long doctorId) {
        try {
            return patientService.getAppointmentsByDoctorId(doctorId);
        } catch (DoctorNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @Operation(summary = "Retrieve appointments by specialization and date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of appointments for the specialization and date"),
            @ApiResponse(responseCode = "404", description = "No appointments found for the given criteria")
    })
    @GetMapping("/appointments")
    @ResponseStatus(HttpStatus.OK)
    public List<AppointmentDto> getAppointmentsBySpecializationAndDate(
            @Parameter(description = "Specialization of the doctor") @RequestParam String specialization,
            @Parameter(description = "Date for the appointments") @RequestParam LocalDate localDate) {
        try {
            return patientService.getAppointmentsBySpecializationAndDate(specialization, localDate);
        } catch (ClinicMedicalMicroserviceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }
}
