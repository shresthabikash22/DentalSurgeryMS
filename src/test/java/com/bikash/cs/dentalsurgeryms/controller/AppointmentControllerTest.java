package com.bikash.cs.dentalsurgeryms.controller;

import com.bikash.cs.dentalsurgeryms.dto.request.AppointmentRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.DentistBasicResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.PatientBasicResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.SurgeryBasicResponseDto;
import com.bikash.cs.dentalsurgeryms.enums.AppointmentStatus;
import com.bikash.cs.dentalsurgeryms.exception.ResourceNotFoundException;
import com.bikash.cs.dentalsurgeryms.service.AppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ActiveProfiles("test") 
@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AppointmentService appointmentService;
    @Autowired
    private ObjectMapper objectMapper;

    private static final Long PATIENT_ID = 1L;
    private static final Long DENTIST_ID = 2L;
    private static final Long SURGERY_ID = 3L;
    private static final Long APPOINTMENT_ID = 4L;
    private AppointmentRequestDto appointmentRequestDto;
    private AppointmentResponseDto appointmentResponseDto;
    private PatientBasicResponseDto patientBasicResponseDto;
    private DentistBasicResponseDto dentistBasicResponseDto;
    private SurgeryBasicResponseDto surgeryBasicResponseDto;

    @BeforeEach()
    void setUp() {

        PatientBasicResponseDto patientBasicResponseDto = new PatientBasicResponseDto(PATIENT_ID, "John", "Doe", "1234567890", "maileMe@johndoe.com");
        DentistBasicResponseDto dentistBasicResponseDto = new DentistBasicResponseDto(DENTIST_ID, "Amanda", "Clarke", "General");
        SurgeryBasicResponseDto surgeryBasicResponseDto = new SurgeryBasicResponseDto(SURGERY_ID, "The Smiles dental", "CT-RRG", "435 West St, Irving Tx");

        appointmentRequestDto = new AppointmentRequestDto(
                LocalDateTime.of(2025, 6, 22, 8, 30, 0), AppointmentStatus.REQUESTED, 1L, 2L, 3L);
        appointmentResponseDto = new AppointmentResponseDto(
                APPOINTMENT_ID,
                LocalDateTime.of(2025, 6, 22, 8, 30, 0),
                AppointmentStatus.SCHEDULED,
                patientBasicResponseDto,
                dentistBasicResponseDto,
                surgeryBasicResponseDto);

    }


    @Test
    @DisplayName("POST /appointments should create and return appointment")
    void givenAppointmentRequestDto_whenCreate_thenReturnAppointmentResponseDto() throws Exception {
        Mockito.when(appointmentService.createAppointment(appointmentRequestDto)).thenReturn(appointmentResponseDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/appointments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(appointmentRequestDto))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(appointmentResponseDto)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("GET /appointments/{id} should return appointment")
    void givenAppointmentId_whenGetById_thenReturnAppointmentResponseDto() throws Exception {
        Mockito.when(appointmentService.getAppointmentById(100L)).thenReturn(appointmentResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/appointments/{id}", 100L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(appointmentResponseDto)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("GET /appointments/{id} if not found should return NOT FOUND")
    void givenNonExistingAppointmentId_whenGetById_thenReturnNotFound() throws Exception {
        Mockito.when(appointmentService.getAppointmentById(999L))
                .thenThrow(new ResourceNotFoundException("Appointment not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/appointments/{id}", 999L))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Appointment not found with id: 999"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("PUT /appointments/{id}/cancel should cancel and return updated appointment")
    void givenAppointmentId_whenCancel_thenReturnCancelledAppointmentResponseDto() throws Exception {
        AppointmentResponseDto cancelled = new AppointmentResponseDto(
                APPOINTMENT_ID,
                LocalDateTime.of(2025, 6, 22, 8, 30, 0),
                AppointmentStatus.CANCELLED,
                patientBasicResponseDto,
                dentistBasicResponseDto,
                surgeryBasicResponseDto);


        Mockito.when(appointmentService.cancelAppointment(100L)).thenReturn(cancelled);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/appointments/{id}/cancel", 100L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(cancelled)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("PUT /appointments/{id} should update and return appointment")
    void givenAppointmentIdAndRequestDto_whenUpdate_thenReturnUpdatedAppointmentResponseDto() throws Exception {
        AppointmentResponseDto updated = new AppointmentResponseDto(
                APPOINTMENT_ID,
                LocalDateTime.of(2025, 6, 22, 8, 30, 0),
                AppointmentStatus.SCHEDULED,
                patientBasicResponseDto,
                dentistBasicResponseDto,
                surgeryBasicResponseDto);
        Mockito.when(appointmentService.updateAppointment(eq(100L), any(AppointmentRequestDto.class))).thenReturn(updated);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/appointments/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(updated)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("DELETE /appointments/{id} should delete appointment")
    void givenAppointmentId_whenDelete_thenNoContent() throws Exception {
        Mockito.doNothing().when(appointmentService).deleteAppointment(100L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/appointments/{id}", 100L))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());
    }
}
