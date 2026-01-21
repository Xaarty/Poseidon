package com.nnk.springboot.controller;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void list() throws Exception {
        when(userService.findAll()).thenReturn(List.of(new User(), new User()));

        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attributeExists("users"));

        verify(userService).findAll();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void addUser() throws Exception {
        mockMvc.perform(get("/user/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void validateInvalid() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)

                        .param("username", "")
                        .param("password", "")
                        .param("fullname", "")
                        .param("role", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));

        verify(userService, never()).create(any());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void validateToList() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "john")
                        .param("password", "Password1!")
                        .param("fullname", "John Doe")
                        .param("role", "ADMIN"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService).create(any(User.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getUser_shouldReturnUpdateView_andBlankPasswordInModel() throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("john");
        user.setPassword("$2a$10$hashhashhash");
        user.setFullname("John Doe");
        user.setRole("ADMIN");

        when(userService.findById(1)).thenReturn(user);

        mockMvc.perform(get("/user/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().attributeExists("user"))

                .andExpect(model().attribute("user", org.hamcrest.Matchers.hasProperty("password", org.hamcrest.Matchers.is(""))));

        verify(userService).findById(1);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void updateInvalidNoSave() throws Exception {
        mockMvc.perform(post("/user/update/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "")
                        .param("password", "")
                        .param("fullname", "")
                        .param("role", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"));

        verify(userService, never()).update(anyInt(), any());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void updateToList() throws Exception {
        mockMvc.perform(post("/user/update/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "john-upd")
                        .param("password", "Password1!")
                        .param("fullname", "John Doe Updated")
                        .param("role", "ADMIN"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService).update(eq(1), any(User.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteToList() throws Exception {
        mockMvc.perform(get("/user/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService).delete(1);
    }
}