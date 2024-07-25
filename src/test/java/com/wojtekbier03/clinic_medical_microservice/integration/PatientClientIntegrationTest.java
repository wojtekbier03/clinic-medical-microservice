package com.wojtekbier03.clinic_medical_microservice.integration;

import com.wojtekbier03.clinic_medical_microservice.client.PatientClient;
import com.wojtekbier03.clinic_medical_microservice.dto.AppointmentDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import feign.FeignException;
import feign.RetryableException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ContextConfiguration(initializers = {PatientClientIntegrationTest.Initializer.class})
public class PatientClientIntegrationTest {

    private static WireMockServer wireMockServer;

    @Autowired
    private PatientClient patientClient;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public static void setup() {
        wireMockServer = new WireMockServer(8888);
        WireMock.configureFor("localhost", 8888);
        wireMockServer.start();
    }

    @AfterAll
    public static void end() {
        wireMockServer.stop();
    }

    @Test
    public void testGetAppointmentsByPatientId_Status200() throws Exception {
        Long patientId = 1L;

        AppointmentDto appointmentDto1 = AppointmentDto.builder()
                .id(1L)
                .startTime(LocalDateTime.of(2023, 7, 28, 10, 0))
                .endTime(LocalDateTime.of(2023, 7, 28, 11, 0))
                .build();

        AppointmentDto appointmentDto2 = AppointmentDto.builder()
                .id(2L)
                .startTime(LocalDateTime.of(2023, 7, 29, 14, 0))
                .endTime(LocalDateTime.of(2023, 7, 29, 15, 0))
                .build();

        List<AppointmentDto> expectedAppointmentDtoList = List.of(appointmentDto1, appointmentDto2);

        wireMockServer.stubFor(get(urlEqualTo("/appointments/patient/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(expectedAppointmentDtoList))));

        List<AppointmentDto> result = patientClient.getAppointmentsByPatientId(1L);

        assertEquals(expectedAppointmentDtoList, result);
    }

    @Test
    public void testGetAppointmentsByPatientId_Status404() throws Exception {
        wireMockServer.stubFor(get(urlEqualTo("/appointments/patient/999"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"message\": \"Patient not found\"}")));

        FeignException.NotFound exception = assertThrows(FeignException.NotFound.class, () -> {
            patientClient.getAppointmentsByPatientId(999L);
        });

        assertEquals("404 Not Found: [Patient not found]", exception.getMessage());
    }

    @Test
    public void testGetAppointmentsByPatientId_Status503() throws Exception {
        wireMockServer.stubFor(get(urlEqualTo("/appointments/patient/1"))
                .willReturn(aResponse()
                        .withStatus(503)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"message\": \"Service is unavailable\"}")));

        RetryableException exception = assertThrows(RetryableException.class, () -> {
            patientClient.getAppointmentsByPatientId(1L);
        });

        assertEquals("503 Service Unavailable: [Service is unavailable]", exception.getMessage());
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "patient.client.url=http://localhost:8888"
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
