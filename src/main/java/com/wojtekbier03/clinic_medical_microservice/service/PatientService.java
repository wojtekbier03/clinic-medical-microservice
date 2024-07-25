package com.wojtekbier03.clinic_medical_microservice.service;

import com.wojtekbier03.clinic_medical_microservice.client.PatientClient;
import com.wojtekbier03.clinic_medical_microservice.dto.AppointmentDto;
import com.wojtekbier03.clinic_medical_microservice.exception.ClinicMedicalMicroserviceException;
import com.wojtekbier03.clinic_medical_microservice.exception.DoctorNotFoundException;
import com.wojtekbier03.clinic_medical_microservice.exception.PatientNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import feign.FeignException;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class PatientService {
    private final PatientClient patientClient;

    public List<AppointmentDto> getAppointmentByPatientId(Long patientId) {
        try {
            return patientClient.getAppointmentsByPatientId(patientId);
        } catch (FeignException.NotFound e) {
            throw new PatientNotFoundException("Patient not found with ID: " + patientId);
        } catch (FeignException e) {
            throw new ClinicMedicalMicroserviceException("Error retrieving appointments: " + e.getMessage());
        }
    }

    public AppointmentDto bookAppointment(AppointmentDto appointmentDto, Long patientId) {
        try {
            return patientClient.bookAppointment(appointmentDto, patientId);
        } catch (FeignException.NotFound e) {
            throw new PatientNotFoundException("Patient not found with ID: " + patientId);
        } catch (FeignException e) {
            throw new ClinicMedicalMicroserviceException("Error booking appointment: " + e.getMessage());
        }
    }

    public List<AppointmentDto> getAppointmentsByDoctorId(Long doctorId) {
        try {
            return patientClient.getAppointmentsByDoctorId(doctorId);
        } catch (FeignException.NotFound e) {
            throw new DoctorNotFoundException("Doctor not found with ID: " + doctorId);
        } catch (FeignException e) {
            throw new ClinicMedicalMicroserviceException("Error retrieving appointments: " + e.getMessage());
        }
    }

    public List<AppointmentDto> getAppointmentsBySpecializationAndDate(String specialization, LocalDate localDate) {
        try {
            return patientClient.getAppointmentsBySpecializationAndDate(specialization, localDate);
        } catch (FeignException e) {
            throw new ClinicMedicalMicroserviceException("Error retrieving appointments: " + e.getMessage());
        }
    }
}