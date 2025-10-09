package com.ros.lms.adapters.inbound;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ros.lms.domain.dtos.AddLoanDTO;
import com.ros.lms.domain.dtos.ReturnBookDTO;
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

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoanService loanService;

    @Autowired
    private ObjectMapper objectMapper;

    private AddLoanDTO validLoanDTO;
    private ReturnBookDTO validReturnBookDTO;

    @BeforeEach
    void setup() {
        validLoanDTO = new AddLoanDTO("9780000000001", "member001", "staff001");
        validReturnBookDTO = new ReturnBookDTO("9780000000001", "staff001");
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
        doThrow(new BookNotFoundException("Book not found"))
                .when(loanService).add(Mockito.any(AddLoanDTO.class));

        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoanDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "staff001", roles = {"STAFF"})
    void issueBook_shouldReturnBadRequest_whenBookNotAvailable() throws Exception {
        doThrow(new BookNotAvailableException("Book not available"))
                .when(loanService).add(Mockito.any(AddLoanDTO.class));

        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoanDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "staff001", roles = {"STAFF"})
    void issueBook_shouldReturnNotFound_whenMemberNotFound() throws Exception {
        doThrow(new MemberNotFoundException("Member not found"))
                .when(loanService).add(Mockito.any(AddLoanDTO.class));

        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoanDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "staff001", roles = {"STAFF"})
    void issueBook_shouldReturnNotFound_whenStaffNotFound() throws Exception {
        doThrow(new StaffNotFoundException("Staff not Found"))
                .when(loanService).add(Mockito.any(AddLoanDTO.class));

        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoanDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "staff001", roles = {"STAFF"})
    void issueBook_shouldReturnBadRequest_whenMemberHasActiveLoan() throws Exception {
        doThrow(new MemberHasActiveLoanException("Member has active loan"))
                .when(loanService).add(Mockito.any(AddLoanDTO.class));

        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoanDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "staff001", roles = {"STAFF"})
    void issueBook_shouldReturnBadRequest_whenMemberHasOverdueLoan() throws Exception {
        doThrow(new MemberHasOverdueLoanException("Member has overdue loan"))
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

    @Test
    @WithMockUser(username = "staff001", roles = {"STAFF"})
    void returnBook_shouldReturnOk_whenReturnIsSuccessful() throws Exception {
        mockMvc.perform(put("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validReturnBookDTO)))
                .andExpect(status().isOk());

        verify(loanService).returnBook(validReturnBookDTO);
    }

    @Test
    @WithMockUser(username = "staff001", roles = {"STAFF"})
    void returnBook_shouldReturnBadRequest_whenBookNotRegistered() throws Exception {
        doThrow(new BookNotRegisteredException("Book not registered"))
                .when(loanService).returnBook(Mockito.any(ReturnBookDTO.class));

        mockMvc.perform(put("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validReturnBookDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "staff001", roles = {"STAFF"})
    void returnBook_shouldReturnBadRequest_whenBookAlreadyInStock() throws Exception {
        doThrow(new BookAlreadyInStockException("Book already in stock"))
                .when(loanService).returnBook(Mockito.any(ReturnBookDTO.class));

        mockMvc.perform(put("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validReturnBookDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "staff001", roles = {"STAFF"})
    void returnBook_shouldReturnUnauthorized_whenInvalidStaff() throws Exception {
        doThrow(new InvalidStaffException("Invalid staff"))
                .when(loanService).returnBook(Mockito.any(ReturnBookDTO.class));

        mockMvc.perform(put("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validReturnBookDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "member001", roles = {"MEMBER"})
    void returnBook_shouldReturnForbidden_whenUserIsNotStaff() throws Exception {
        mockMvc.perform(put("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validReturnBookDTO)))
                .andExpect(status().isForbidden());
    }


}
