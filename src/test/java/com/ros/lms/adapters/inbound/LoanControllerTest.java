package com.ros.lms.adapters.inbound;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ros.lms.domain.dtos.AddLoanDTO;
import com.ros.lms.domain.exceptions.*;
import com.ros.lms.ports.inbound.service_contracts.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoanService loanService;

    @Autowired
    private ObjectMapper objectMapper;

    private AddLoanDTO validLoanDTO;

    @BeforeEach
    void setup() {
        validLoanDTO = new AddLoanDTO(1L, "member001", "staff001");
    }

    @Test
    @WithMockUser(username = "staff001", roles = {"STAFF"})
    void issueBook_shouldReturnOk_whenLoanIsSuccessful() throws Exception {
        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoanDTO)))
                .andExpect(status().isOk());

        verify(loanService).add(validLoanDTO);
    }

    @Test
    @WithMockUser(username = "staff001", roles = {"STAFF"})
    void issueBook_shouldReturnNotFound_whenBookNotFound() throws Exception {
        Mockito.doThrow(new BookNotFoundException("Book not found"))
                .when(loanService).add(Mockito.any(AddLoanDTO.class));

        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoanDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "staff001", roles = {"STAFF"})
    void issueBook_shouldReturnBadRequest_whenBookNotAvailable() throws Exception {
        Mockito.doThrow(new BookNotAvailableException("Book not available"))
                .when(loanService).add(Mockito.any(AddLoanDTO.class));

        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoanDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "staff001", roles = {"STAFF"})
    void issueBook_shouldReturnNotFound_whenMemberNotFound() throws Exception {
        Mockito.doThrow(new MemberNotFoundException("Member not found"))
                .when(loanService).add(Mockito.any(AddLoanDTO.class));

        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoanDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "staff001", roles = {"STAFF"})
    void issueBook_shouldReturnNotFound_whenStaffNotFound() throws Exception {
        Mockito.doThrow(new StaffNotFoundException("Staff not found"))
                .when(loanService).add(Mockito.any(AddLoanDTO.class));

        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoanDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "staff001", roles = {"STAFF"})
    void issueBook_shouldReturnBadRequest_whenMemberHasActiveLoan() throws Exception {
        Mockito.doThrow(new MemberHasActiveLoanException("Member has active loan"))
                .when(loanService).add(Mockito.any(AddLoanDTO.class));

        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoanDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "staff001", roles = {"STAFF"})
    void issueBook_shouldReturnBadRequest_whenMemberHasOverdueLoan() throws Exception {
        Mockito.doThrow(new MemberHasOverdueLoanException("Member has overdue loan"))
                .when(loanService).add(Mockito.any(AddLoanDTO.class));

        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoanDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "member001", roles = {"MEMBER"}) // Not STAFF
    void issueBook_shouldReturnForbidden_whenUserIsNotStaff() throws Exception {
        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoanDTO)))
                .andExpect(status().isForbidden()); // Spring Security blocks this
    }


}
