package ru.netology.patient.service.medical;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MedicalServiceImplTest {
    @Mock
    private PatientInfoRepository patientInfoRepository;
    @InjectMocks
    private SendAlertService alertService;
    private MedicalServiceImpl medicalService;
    @Captor
    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    @BeforeEach
    public void setUp() {
        patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        alertService = Mockito.mock(SendAlertService.class);
        medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);
    }


    @Test
    public void checkBloodPressureTest() {
        BloodPressure currentPressure = new BloodPressure(120, 80);
        String patientId = "123";
        PatientInfo patientInfo = new PatientInfo(patientId,
                "Саша", "Нестеров",
                LocalDate.of(1998, 8, 15),
                new HealthInfo(new BigDecimal("36.65"), currentPressure));

        when(patientInfoRepository.getById(patientId)).thenReturn(patientInfo);
        medicalService.checkBloodPressure(patientId, new BloodPressure(140, 90));

        verify(patientInfoRepository).getById(patientId);
        verify(alertService).send(messageCaptor.capture());

        String expectedMessage = String.format("Warning, patient with id: %s, need help", patientId);
        String actualMessage = messageCaptor.getValue();
        assertEquals(expectedMessage, actualMessage);
    }
    @Test
    public void checkBloodPressureNormalTest() {
        BloodPressure currentPressure = new BloodPressure(120, 80);
        String patientId = "123";
        PatientInfo patientInfo = new PatientInfo(patientId,
                "Саша", "Нестеров",
                LocalDate.of(1998, 8, 15),
                new HealthInfo(new BigDecimal("36.65"), currentPressure));

        when(patientInfoRepository.getById(patientId)).thenReturn(patientInfo);
        medicalService.checkBloodPressure(patientId, new BloodPressure(120, 80));

        verify(patientInfoRepository).getById(patientId);
        verify(alertService, never()).send(messageCaptor.capture());
    }
    @Test
    public void testCheckTemperature() {
        BloodPressure currentPressure = new BloodPressure(120, 80);
        String patientId = "123";
        PatientInfo patientInfo = new PatientInfo(patientId,
                "Саша", "Нестеров",
                LocalDate.of(1998, 8, 15),
                new HealthInfo(new BigDecimal("36.65"), currentPressure));

        BigDecimal currentTemperature = new BigDecimal("35.0");
        when(patientInfoRepository.getById(patientId)).thenReturn(patientInfo);
        medicalService.checkTemperature(patientId, currentTemperature);

        verify(patientInfoRepository).getById(patientId);
        verify(alertService).send(messageCaptor.capture());

        String expectedMessage = String.format("Warning, patient with id: %s, need help", patientId);
        String actualMessage = messageCaptor.getValue();
        assertEquals(expectedMessage, actualMessage);
    }
    @Test
    public void checkTemperatureNormalTest() {
        BloodPressure currentPressure = new BloodPressure(120, 80);
        String patientId = "123";
        PatientInfo patientInfo = new PatientInfo(patientId,
                "Саша", "Нестеров",
                LocalDate.of(1998, 8, 15),
                new HealthInfo(new BigDecimal("36.50"), currentPressure));

        BigDecimal currentTemperature = new BigDecimal("36.7");
        when(patientInfoRepository.getById(patientId)).thenReturn(patientInfo);
        medicalService.checkTemperature(patientId, currentTemperature);

        verify(patientInfoRepository).getById(patientId);
        verify(alertService, never()).send(messageCaptor.capture());
    }


}
