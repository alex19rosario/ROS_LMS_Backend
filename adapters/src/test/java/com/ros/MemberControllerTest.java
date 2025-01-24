package com.ros;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ros.dtos.AddMemberDTO;
import com.ros.exceptions.MemberAlreadyExistsException;
import com.ros.exceptions.UsernameAlreadyExistsException;
import com.ros.ports_inbound.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

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
                (byte) 27,
                'M',
                "test19@gmail.com",
                "carlos19",
                "test123"
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
    void saveBook_shouldReturnConflict_whenMemberAlreadyExists() throws Exception{
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
    void saveBook_shouldReturnConflict_whenUsernameAlreadyExists() throws Exception{
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
    void saveBook_shouldReturnConflict_whenEmailAlreadyExists() throws Exception{
        Mockito.doThrow(new UsernameAlreadyExistsException("Username already exists in the database"))
                .when(memberService).add(Mockito.any(AddMemberDTO.class));

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validMemberDTO)))
                .andExpect(status().isConflict());

        verify(memberService).add(validMemberDTO);
    }




}
