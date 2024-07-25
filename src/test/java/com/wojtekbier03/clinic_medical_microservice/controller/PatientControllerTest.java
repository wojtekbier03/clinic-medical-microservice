package com.wojtekbier03.clinic_medical_microservice.controller;

import com.wojtekbier03.clinic_medical_microservice.dto.AppointmentDto;
import com.wojtekbier03.clinic_medical_microservice.exception.ClinicMedicalMicroserviceException;
import com.wojtekbier03.clinic_medical_microservice.exception.DoctorNotFoundException;
import com.wojtekbier03.clinic_medical_microservice.exception.PatientNotFoundException;
import com.wojtekbier03.clinic_medical_microservice.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientControllerTest {

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

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
    void getAppointmentByPatientId_ReturnAppointments_PatientExists() {
        List<AppointmentDto> expectedAppointments = Collections.singletonList(appointmentDto);
        when(patientService.getAppointmentByPatientId(patientId)).thenReturn(expectedAppointments);

        List<AppointmentDto> actualAppointments = patientController.getAppointmentByPatientId(patientId);

        assertEquals(expectedAppointments, actualAppointments);
        verify(patientService, times(1)).getAppointmentByPatientId(patientId);
    }

    @Test
    void getAppointmentByPatientId_ThrowResponseStatusException_PatientNotFound() {
        when(patientService.getAppointmentByPatientId(patientId)).thenThrow(PatientNotFoundException.class);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> patientController.getAppointmentByPatientId(patientId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(patientService, times(1)).getAppointmentByPatientId(patientId);
    }

    @Test
    void bookAppointment_ReturnAppointment_BookingIsSuccessful() {
        when(patientService.bookAppointment(appointmentDto, patientId)).thenReturn(appointmentDto);

        AppointmentDto bookedAppointment = patientController.bookAppointment(appointmentDto, patientId);

        assertEquals(appointmentDto, bookedAppointment);
        verify(patientService, times(1)).bookAppointment(appointmentDto, patientId);
    }

    @Test
    void bookAppointment_ThrowResponseStatusException_PatientNotFound() {
        when(patientService.bookAppointment(appointmentDto, patientId)).thenThrow(PatientNotFoundException.class);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> patientController.bookAppointment(appointmentDto, patientId));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(patientService, times(1)).bookAppointment(appointmentDto, patientId);
    }

    @Test
    void getAppointmentsByDoctorId_ReturnAppointments_DoctorExists() {
        List<AppointmentDto> expectedAppointments = Collections.singletonList(appointmentDto);
        when(patientService.getAppointmentsByDoctorId(doctorId)).thenReturn(expectedAppointments);

        List<AppointmentDto> actualAppointments = patientController.getAppointmentsByDoctorId(doctorId);

        assertEquals(expectedAppointments, actualAppointments);
        verify(patientService, times(1)).getAppointmentsByDoctorId(doctorId);
    }

    @Test
    void getAppointmentsByDoctorId_ThrowResponseStatusException_DoctorNotFound() {
        when(patientService.getAppointmentsByDoctorId(doctorId)).thenThrow(DoctorNotFoundException.class);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> patientController.getAppointmentsByDoctorId(doctorId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(patientService, times(1)).getAppointmentsByDoctorId(doctorId);
    }

    @Test
    void getAppointmentsBySpecializationAndDate_Correct_ReturnAppointments() {
        List<AppointmentDto> expectedAppointments = Collections.singletonList(appointmentDto);
        when(patientService.getAppointmentsBySpecializationAndDate(specialization, date)).thenReturn(expectedAppointments);

        List<AppointmentDto> actualAppointments = patientController.getAppointmentsBySpecializationAndDate(specialization, date);

        assertEquals(expectedAppointments, actualAppointments);
        verify(patientService, times(1)).getAppointmentsBySpecializationAndDate(specialization, date);
    }

    @Test
    void getAppointmentsBySpecializationAndDate_Error_ThrowResponseStatusException() {
        when(patientService.getAppointmentsBySpecializationAndDate(specialization, date)).thenThrow(ClinicMedicalMicroserviceException.class);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> patientController.getAppointmentsBySpecializationAndDate(specialization, date));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        verify(patientService, times(1)).getAppointmentsBySpecializationAndDate(specialization, date);
    }
}

