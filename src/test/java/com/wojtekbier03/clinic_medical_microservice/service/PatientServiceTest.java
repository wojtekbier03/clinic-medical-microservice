package com.wojtekbier03.clinic_medical_microservice.service;

import com.wojtekbier03.clinic_medical_microservice.client.PatientClient;
import com.wojtekbier03.clinic_medical_microservice.dto.AppointmentDto;
import com.wojtekbier03.clinic_medical_microservice.exception.ClinicMedicalMicroserviceException;
import com.wojtekbier03.clinic_medical_microservice.exception.DoctorNotFoundException;
import com.wojtekbier03.clinic_medical_microservice.exception.PatientNotFoundException;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientClient patientClient;

    @InjectMocks
    private PatientService patientService;

    private AppointmentDto appointmentDto;
    private Long patientId;
    private Long doctorId;
    private String specialization;
    private LocalDate date;

    @BeforeEach
    void setUp() {
        appointmentDto = new AppointmentDto();
        patientId = 1L;
        doctorId = 1L;
        specialization = "Cardiology";
        date = LocalDate.now();
    }

    @Test
    void getAppointmentByPatientId_shouldReturnAppointments_whenPatientExists() {
        List<AppointmentDto> expectedAppointments = Collections.singletonList(appointmentDto);
        when(patientClient.getAppointmentsByPatientId(patientId)).thenReturn(expectedAppointments);

        List<AppointmentDto> actualAppointments = patientService.getAppointmentByPatientId(patientId);

        assertEquals(expectedAppointments, actualAppointments);
        verify(patientClient, times(1)).getAppointmentsByPatientId(patientId);
    }

    @Test
    void getAppointmentByPatientId_shouldThrowPatientNotFoundException_whenPatientDoesNotExist() {
        when(patientClient.getAppointmentsByPatientId(patientId)).thenThrow(FeignException.NotFound.class);

        assertThrows(PatientNotFoundException.class, () -> patientService.getAppointmentByPatientId(patientId));
        verify(patientClient, times(1)).getAppointmentsByPatientId(patientId);
    }

    @Test
    void bookAppointment_shouldReturnAppointment_whenBookingIsSuccessful() {
        when(patientClient.bookAppointment(appointmentDto, patientId)).thenReturn(appointmentDto);

        AppointmentDto bookedAppointment = patientService.bookAppointment(appointmentDto, patientId);

        assertEquals(appointmentDto, bookedAppointment);
        verify(patientClient, times(1)).bookAppointment(appointmentDto, patientId);
    }

    @Test
    void bookAppointment_shouldThrowPatientNotFoundException_whenPatientDoesNotExist() {
        when(patientClient.bookAppointment(appointmentDto, patientId)).thenThrow(FeignException.NotFound.class);

        assertThrows(PatientNotFoundException.class, () -> patientService.bookAppointment(appointmentDto, patientId));
        verify(patientClient, times(1)).bookAppointment(appointmentDto, patientId);
    }

    @Test
    void getAppointmentsByDoctorId_shouldReturnAppointments_whenDoctorExists() {
        List<AppointmentDto> expectedAppointments = Collections.singletonList(appointmentDto);
        when(patientClient.getAppointmentsByDoctorId(doctorId)).thenReturn(expectedAppointments);

        List<AppointmentDto> actualAppointments = patientService.getAppointmentsByDoctorId(doctorId);

        assertEquals(expectedAppointments, actualAppointments);
        verify(patientClient, times(1)).getAppointmentsByDoctorId(doctorId);
    }

    @Test
    void getAppointmentsByDoctorId_shouldThrowDoctorNotFoundException_whenDoctorDoesNotExist() {
        when(patientClient.getAppointmentsByDoctorId(doctorId)).thenThrow(FeignException.NotFound.class);

        assertThrows(DoctorNotFoundException.class, () -> patientService.getAppointmentsByDoctorId(doctorId));
        verify(patientClient, times(1)).getAppointmentsByDoctorId(doctorId);
    }

    @Test
    void getAppointmentsBySpecializationAndDate_shouldReturnAppointments_whenDataIsValid() {
        List<AppointmentDto> expectedAppointments = Collections.singletonList(appointmentDto);
        when(patientClient.getAppointmentsBySpecializationAndDate(specialization, date)).thenReturn(expectedAppointments);

        List<AppointmentDto> actualAppointments = patientService.getAppointmentsBySpecializationAndDate(specialization, date);

        assertEquals(expectedAppointments, actualAppointments);
        verify(patientClient, times(1)).getAppointmentsBySpecializationAndDate(specialization, date);
    }

    @Test
    void getAppointmentsBySpecializationAndDate_shouldThrowClinicMedicalMicroserviceException_whenFeignExceptionOccurs() {
        when(patientClient.getAppointmentsBySpecializationAndDate(specialization, date)).thenThrow(FeignException.class);

        assertThrows(ClinicMedicalMicroserviceException.class, () -> patientService.getAppointmentsBySpecializationAndDate(specialization, date));
        verify(patientClient, times(1)).getAppointmentsBySpecializationAndDate(specialization, date);
    }
}