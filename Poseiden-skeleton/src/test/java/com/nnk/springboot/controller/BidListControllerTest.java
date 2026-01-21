package com.nnk.springboot.controller;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.service.BidListService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
class BidListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BidListService bidListService;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void list() throws Exception {
        when(bidListService.findAll()).thenReturn(List.of(new BidList(), new BidList()));

        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/list"))
                .andExpect(model().attributeExists("bidLists"));

        verify(bidListService).findAll();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void addToList() throws Exception {
        mockMvc.perform(get("/bidList/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().attributeExists("bidList"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void validateToList() throws Exception {
        mockMvc.perform(post("/bidList/validate")
                        .with(csrf())
                        .param("account", "account")
                        .param("type", "type")
                        .param("bidQuantity", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        verify(bidListService).create(any(BidList.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void validateInvalid() throws Exception {
        mockMvc.perform(post("/bidList/validate")
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"));

        verify(bidListService, never()).create(any());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getBidList() throws Exception {
        BidList bid = new BidList();
        bid.setBidListId(1);
        when(bidListService.findById(1)).thenReturn(bid);

        mockMvc.perform(get("/bidList/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().attributeExists("bidList"));

        verify(bidListService).findById(1);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void updateToList() throws Exception {
        mockMvc.perform(post("/bidList/update/1")
                        .with(csrf())
                        .param("account", "account")
                        .param("type", "type")
                        .param("bidQuantity", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        verify(bidListService).update(eq(1), any(BidList.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void updateInvalidNoSave() throws Exception {
        mockMvc.perform(post("/bidList/update/1")
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"));

        verify(bidListService, never()).update(anyInt(), any());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteToList() throws Exception {
        mockMvc.perform(get("/bidList/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        verify(bidListService).delete(1);
    }

    @Test
    void unLogToLogin() throws Exception {
        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().is3xxRedirection());
    }
}
