package com.nnk.springboot.controller;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.service.CurvePointService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CurveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurvePointService curvePointService;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void list() throws Exception {
        when(curvePointService.findAll())
                .thenReturn(List.of(new CurvePoint(), new CurvePoint()));

        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/list"))
                .andExpect(model().attributeExists("curvePoints"));

        verify(curvePointService).findAll();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void addToCurveList() throws Exception {
        mockMvc.perform(get("/curvePoint/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(model().attributeExists("curvePoint"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void validateInvalid() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("curveId", "")
                        .param("term", "")
                        .param("value", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"));

        verify(curvePointService, never()).create(any());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void validateToList() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("curveId", "1")
                        .param("term", "10")
                        .param("value", "25"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService).create(any(CurvePoint.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getCurvePoint() throws Exception {
        CurvePoint curvePoint = new CurvePoint();
        curvePoint.setId(1);

        when(curvePointService.findById(1)).thenReturn(curvePoint);

        mockMvc.perform(get("/curvePoint/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().attributeExists("curvePoint"));

        verify(curvePointService).findById(1);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void updateInvalidNoSave() throws Exception {
        mockMvc.perform(post("/curvePoint/update/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("curveId", "")
                        .param("term", "")
                        .param("value", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"));

        verify(curvePointService, never()).update(anyInt(), any());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void updateToList() throws Exception {
        mockMvc.perform(post("/curvePoint/update/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("curveId", "1")
                        .param("term", "5")
                        .param("value", "12"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService).update(eq(1), any(CurvePoint.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteToList() throws Exception {
        mockMvc.perform(get("/curvePoint/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService).delete(1);
    }
}