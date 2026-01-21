package com.nnk.springboot.controller;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.service.RatingService;
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
class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingService ratingService;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void list() throws Exception {
        when(ratingService.findAll()).thenReturn(List.of(new Rating(), new Rating()));

        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/list"))
                .andExpect(model().attributeExists("ratings"));

        verify(ratingService).findAll();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void addToRatingList() throws Exception {
        mockMvc.perform(get("/rating/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void validateInvalid() throws Exception {
        mockMvc.perform(post("/rating/validate")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("moodysRating", "")
                        .param("sandPRating", "")
                        .param("fitchRating", "")
                        .param("orderNumber", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        verify(ratingService).create(any(Rating.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void validateToList() throws Exception {
        mockMvc.perform(post("/rating/validate")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("moodysRating", "1")
                        .param("sandPRating", "2")
                        .param("fitchRating", "3")
                        .param("order", "A"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        verify(ratingService).create(any(Rating.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getRating() throws Exception {
        Rating rating = new Rating();
        rating.setId(1);
        when(ratingService.findById(1)).thenReturn(rating);

        mockMvc.perform(get("/rating/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"))
                .andExpect(model().attributeExists("rating"));

        verify(ratingService).findById(1);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void updateInvalidNoSave() throws Exception {
        mockMvc.perform(post("/rating/update/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("moodysRating", "")
                        .param("sandPRating", "")
                        .param("fitchRating", "")
                        .param("orderNumber", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        verify(ratingService).update(eq(1), any(Rating.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void updateToList() throws Exception {
        mockMvc.perform(post("/rating/update/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("moodysRating", "10")
                        .param("sandPRating", "20")
                        .param("fitchRating", "30")
                        .param("order", "B"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        verify(ratingService).update(eq(1), any(Rating.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteToList() throws Exception {
        mockMvc.perform(get("/rating/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        verify(ratingService).delete(1);
    }
}
