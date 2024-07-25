package com.wojtekbier03.clinic_medical_microservice.exception;

    public class DoctorNotFoundException extends ClinicMedicalMicroserviceException {
        public DoctorNotFoundException(String message) {
            super(message);
        }
    }
