package com.wojtekbier03.clinic_medical_microservice.exception;

public class ClinicMedicalMicroserviceException extends RuntimeException {
    public ClinicMedicalMicroserviceException(String message) {
        super(message);
    }

    public ClinicMedicalMicroserviceException(String message, Throwable cause) {
        super(message, cause);
    }
}