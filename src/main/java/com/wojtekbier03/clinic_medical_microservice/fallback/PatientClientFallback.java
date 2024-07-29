package com.wojtekbier03.clinic_medical_microservice.fallback;

import com.wojtekbier03.clinic_medical_microservice.client.PatientClient;
import com.wojtekbier03.clinic_medical_microservice.dto.AppointmentDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Component
public class PatientClientFallback implements PatientClient {

    @Override
    public List<AppointmentDto> getAppointmentsByPatientId(Long patientId) {
        return Collections.emptyList();
    }

    @Override
    public AppointmentDto bookAppointment(AppointmentDto appointmentDto, Long patientId) {
        return new AppointmentDto();
    }

    @Override
    public List<AppointmentDto> getAppointmentsByDoctorId(Long doctorId) {
        return Collections.emptyList();
    }

    @Override
    public List<AppointmentDto> getAppointmentsBySpecializationAndDate(String specialization, LocalDate date) {
        return Collections.emptyList();
    }
}

