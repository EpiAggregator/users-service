package com.epiaggregator.services.users.web;

import com.epiaggregator.services.users.model.User;
import com.epiaggregator.services.users.model.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UsersControllerTests {
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MockHttpServletRequest mockHttpServletRequest;

    @Test
    public void testRegisterUser() throws Exception {
        mockMvc.perform(post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@epiaggr.fr\",\"password\":\"weak12\"}"))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void testRegisterUserShouldBadRequest() throws Exception {
        mockMvc.perform(post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@epiaggr.fr\",\"password\":\"weak\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetUserByEmail() throws Exception {
        when(userRepository.findByEmail("test@epiaggr.fr"))
                .thenReturn(new User("test@epiaggr.fr", "$31$16$j5bwd53MZmVVUTRQCh0u744-j6TGXpapOWwnz3Q0ykw"));
        mockMvc.perform(get("/v1/users?email=test@epiaggr.fr")
                .accept(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", equalTo("test@epiaggr.fr")))
                .andExpect(jsonPath("$.password", equalTo("$31$16$j5bwd53MZmVVUTRQCh0u744-j6TGXpapOWwnz3Q0ykw")));
    }
}
