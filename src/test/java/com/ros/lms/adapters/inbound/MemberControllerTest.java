package com.ros.lms.adapters.inbound;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ros.lms.domain.dtos.AddMemberDTO;
import com.ros.lms.domain.enums.Sex;
import com.ros.lms.domain.exceptions.EmailAlreadyExistsException;
import com.ros.lms.domain.exceptions.MemberAlreadyExistsException;
import com.ros.lms.domain.exceptions.StaffNotFoundException;
import com.ros.lms.domain.exceptions.UsernameAlreadyExistsException;
import com.ros.lms.infraestructure.aop.audit_service.MemberAuditService;
import com.ros.lms.ports.inbound.service_contracts.MemberService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.validation.metadata.ConstraintDescriptor;
import org.hibernate.validator.internal.engine.path.PathImpl;
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

import java.time.LocalDate;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    @MockitoBean
    private MemberAuditService memberAuditService;

    @Autowired
    private ObjectMapper objectMapper;

    private AddMemberDTO validMemberDTO;

    @BeforeEach
    void setup() {
        validMemberDTO = new AddMemberDTO(
                "123123123",
                "carlos alexander",
                "rosario sanchez",
                "6474256438",
                LocalDate.of(1997, 10, 19),
                Sex.MALE,
                "test19@gmail.com",
                "carlos19",
                "test123",
                "staff"
        );
    }

    @Test
    @WithMockUser(username = "member", roles={"MEMBER"})
    void saveMember_shouldReturnOk_whenMemberIsSaved() throws Exception {
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validMemberDTO)))
                .andExpect(status().isOk());

        verify(memberService).add(validMemberDTO);
    }

    @Test
    @WithMockUser(username = "member", roles={"MEMBER"})
    void saveMember_shouldReturnConflict_whenMemberAlreadyExists() throws Exception{
        Mockito.doThrow(new MemberAlreadyExistsException("Member already exists in the database"))
                .when(memberService).add(Mockito.any(AddMemberDTO.class));

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validMemberDTO)))
                .andExpect(status().isConflict());

        verify(memberService).add(validMemberDTO);
    }

    @Test
    @WithMockUser(username = "member", roles={"MEMBER"})
    void saveMember_shouldReturnConflict_whenUsernameAlreadyExists() throws Exception{
        Mockito.doThrow(new UsernameAlreadyExistsException("Username already exists in the database"))
                .when(memberService).add(Mockito.any(AddMemberDTO.class));

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validMemberDTO)))
                .andExpect(status().isConflict());

        verify(memberService).add(validMemberDTO);
    }

    @Test
    @WithMockUser(username = "member", roles={"MEMBER"})
    void saveMember_shouldReturnConflict_whenEmailAlreadyExists() throws Exception{
        Mockito.doThrow(new EmailAlreadyExistsException("Email already exists in the database"))
                .when(memberService).add(Mockito.any(AddMemberDTO.class));

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validMemberDTO)))
                .andExpect(status().isConflict());

        verify(memberService).add(validMemberDTO);
    }

    @Test
    @WithMockUser(username = "member", roles = {"MEMBER"})
    void saveMember_shouldReturnBadRequest_whenDateOfBirthIsInFuture() throws Exception {
        AddMemberDTO invalidDto = new AddMemberDTO(
                "123123123",
                "carlos alexander",
                "rosario sanchez",
                "6474256438",
                LocalDate.now().plusDays(1), // Invalid future date
                Sex.MALE,
                "test.future@gmail.com",
                "carlos_future",
                "test123",
                "staff"
        );

        ConstraintViolation<AddMemberDTO> mockViolation = new ConstraintViolation<AddMemberDTO>() {
            @Override
            public String getMessage() {
                return "Date of birth must be in the past.";
            }
            @Override
            public String getMessageTemplate() {
                return "Date of birth must be in the past.";
            }
            @Override
            public AddMemberDTO getRootBean() {
                return invalidDto;
            }
            @Override
            public Class<AddMemberDTO> getRootBeanClass() {
                return AddMemberDTO.class;
            }
            @Override
            public Object getLeafBean() {
                return invalidDto;
            }
            @Override
            public Object[] getExecutableParameters() {
                return null;
            }
            @Override
            public Object getExecutableReturnValue() {
                return null;
            }
            @Override
            public Path getPropertyPath() {
                return PathImpl.createPathFromString("dateOfBirth");
            }
            @Override
            public Object getInvalidValue() {
                return invalidDto.dateOfBirth();
            }
            @Override
            public ConstraintDescriptor<?> getConstraintDescriptor() {
                return null;
            }
            @Override
            public <U> U unwrap(Class<U> aClass) {
                return null;
            }
        };

        ConstraintViolationException validationException =
                new ConstraintViolationException("Validation failed", Set.of(mockViolation));

        Mockito.doThrow(validationException)
                .when(memberService).add(Mockito.any(AddMemberDTO.class));

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "member", roles = {"MEMBER"})
    void saveMember_shouldReturnNotFound_whenStaffIsNotFound() throws Exception {
        // Arrange
        Mockito.doThrow(new StaffNotFoundException("Staff not found with username: " + validMemberDTO.staffUsername()))
                .when(memberService).add(Mockito.any(AddMemberDTO.class));

        // Act & Assert
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validMemberDTO)))
                .andExpect(status().isNotFound());

        verify(memberService).add(validMemberDTO);
    }



}
