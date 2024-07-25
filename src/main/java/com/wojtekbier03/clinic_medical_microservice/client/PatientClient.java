package com.wojtekbier03.clinic_medical_microservice.client;

import com.wojtekbier03.clinic_medical_microservice.dto.AppointmentDto;
import com.wojtekbier03.clinic_medical_microservice.fallback.PatientClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "clinic-medical", configuration = FeignClientProperties.FeignClientConfiguration.class, fallback = PatientClientFallback.class)
public interface PatientClient {

    @GetMapping("/appointments/{patientId}/appointments")
    List<AppointmentDto> getAppointmentsByPatientId(@PathVariable("patientId") Long patientId);

    @PostMapping("/appointments")
    AppointmentDto bookAppointment(@RequestBody AppointmentDto appointmentDto, Long patientId);

    @GetMapping("/doctors/{doctorId}/appointments")
    List<AppointmentDto> getAppointmentsByDoctorId(@PathVariable("doctorId") Long doctorId);

    @GetMapping("/appointments/specialization/{specialization}/date/{date}")
    List<AppointmentDto> getAppointmentsBySpecializationAndDate(@PathVariable("specialization") String specialization, @PathVariable("date") LocalDate date);
}