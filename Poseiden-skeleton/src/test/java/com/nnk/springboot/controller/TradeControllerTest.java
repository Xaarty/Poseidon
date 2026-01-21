package com.nnk.springboot.controller;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.service.TradeService;
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
class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeService tradeService;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void list() throws Exception {
        when(tradeService.findAll()).thenReturn(List.of(new Trade(), new Trade()));

        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(model().attributeExists("trades"));

        verify(tradeService).findAll();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void addToTradeList() throws Exception {
        mockMvc.perform(get("/trade/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"))
                .andExpect(model().attributeExists("trade"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void validateInvalid() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        // champs @NotBlank dans ton entity : account / type
                        .param("account", "")
                        .param("type", "")
                        // optionnel
                        .param("buyQuantity", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"));

        verify(tradeService, never()).create(any());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void validateToList() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account", "account-1")
                        .param("type", "type-1")
                        .param("buyQuantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService).create(any(Trade.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getTrade() throws Exception {
        Trade trade = new Trade();
        trade.setTradeId(1);

        when(tradeService.findById(1)).thenReturn(trade);

        mockMvc.perform(get("/trade/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attributeExists("trade"));

        verify(tradeService).findById(1);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void updateInvalidNoSave() throws Exception {
        mockMvc.perform(post("/trade/update/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account", "")
                        .param("type", "")
                        .param("buyQuantity", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"));

        verify(tradeService, never()).update(anyInt(), any());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void updateToList() throws Exception {
        mockMvc.perform(post("/trade/update/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account", "account-upd")
                        .param("type", "type-upd")
                        .param("buyQuantity", "15"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService).update(eq(1), any(Trade.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteToList() throws Exception {
        mockMvc.perform(get("/trade/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService).delete(1);
    }
}