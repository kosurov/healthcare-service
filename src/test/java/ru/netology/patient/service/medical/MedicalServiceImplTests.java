package ru.netology.patient.service.medical;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicalServiceImplTests {

    PatientInfoRepository patientInfoRepository;
    SendAlertService sendAlertService;
    ArgumentCaptor<String> argumentCaptor;
    MedicalService medicalService;

    @BeforeEach
    public void initialize() {
        patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        sendAlertService = Mockito.mock(SendAlertService.class);
        argumentCaptor = ArgumentCaptor.forClass(String.class);
        medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
    }

    @Test
    public void checkBloodPressureTest() {
        Mockito.when(patientInfoRepository.getById("id1"))
                .thenReturn(new PatientInfo("id1","Иван", "Петров",
                        LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"),
                        new BloodPressure(120, 80))));

        medicalService.checkBloodPressure("id1", new BloodPressure(110, 90));

        Mockito.verify(sendAlertService).send(argumentCaptor.capture());
        Assertions.assertEquals("Warning, patient with id: id1, need help", argumentCaptor.getValue());
    }

    @Test
    public void checkTemperatureTest() {
        Mockito.when(patientInfoRepository.getById("id1"))
                .thenReturn(new PatientInfo("id1","Иван", "Петров",
                        LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"),
                                new BloodPressure(120, 80))));

        medicalService.checkTemperature("id1", new BigDecimal("40"));

        Mockito.verify(sendAlertService).send(argumentCaptor.capture());
        Assertions.assertEquals("Warning, patient with id: id1, need help", argumentCaptor.getValue());
    }

    @Test
    public void normalHealthInfoTest() {
        Mockito.when(patientInfoRepository.getById("id1"))
                .thenReturn(new PatientInfo("id1","Иван", "Петров",
                        LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"),
                                new BloodPressure(120, 80))));

        medicalService.checkTemperature("id1", new BigDecimal("36.9"));
        medicalService.checkBloodPressure("id1", new BloodPressure(120, 80));

        Mockito.verify(sendAlertService, Mockito.times(0)).send(Mockito.any());
    }

}
