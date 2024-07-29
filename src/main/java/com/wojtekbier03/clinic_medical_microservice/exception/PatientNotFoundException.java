package com.wojtekbier03.clinic_medical_microservice.exception;
public class PatientNotFoundException extends ClinicMedicalMicroserviceException {
    public PatientNotFoundException(String message) {
        super(message);
    }
}
