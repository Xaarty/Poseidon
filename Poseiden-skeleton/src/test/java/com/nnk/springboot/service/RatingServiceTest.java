package com.nnk.springboot.service;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingService ratingService;

    @Test
    void findAll_shouldReturnList() {
        when(ratingRepository.findAll()).thenReturn(List.of(
                new Rating("M1", "S1", "F1", 1),
                new Rating("M2", "S2", "F2", 2)
        ));

        List<Rating> res = ratingService.findAll();

        assertEquals(2, res.size());
        verify(ratingRepository).findAll();
    }

    @Test
    void findById_shouldReturnEntity_whenFound() {
        Rating r = new Rating("M", "S", "F", 1);
        r.setId(1);

        when(ratingRepository.findById(1)).thenReturn(Optional.of(r));

        Rating res = ratingService.findById(1);

        assertNotNull(res);
        assertEquals(1, res.getId());
        verify(ratingRepository).findById(1);
    }

    @Test
    void findById_shouldThrow_whenNotFound() {
        when(ratingRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> ratingService.findById(999));
    }

    @Test
    void create_shouldSave() {
        Rating r = new Rating("M", "S", "F", 1);
        when(ratingRepository.save(r)).thenReturn(r);

        Rating saved = ratingService.create(r);

        assertNotNull(saved);
        verify(ratingRepository).save(r);
    }

    @Test
    void update_shouldCopyFields_andSave() {
        Integer id = 1;

        Rating existing = new Rating("OldM", "OldS", "OldF", 10);
        existing.setId(id);

        Rating newData = new Rating("NewM", "NewS", "NewF", 20);

        when(ratingRepository.findById(id)).thenReturn(Optional.of(existing));
        when(ratingRepository.save(any(Rating.class))).thenAnswer(inv -> inv.getArgument(0));

        Rating updated = ratingService.update(id, newData);

        assertEquals(id, updated.getId());
        assertEquals("NewM", updated.getMoodysRating());
        assertEquals("NewS", updated.getSandPRating());
        assertEquals("NewF", updated.getFitchRating());
        assertEquals(20, updated.getOrderNumber().intValue());

        verify(ratingRepository).save(existing);
    }

    @Test
    void delete_shouldFindAndDelete() {
        Integer id = 1;

        Rating existing = new Rating("M", "S", "F", 1);
        existing.setId(id);

        when(ratingRepository.findById(id)).thenReturn(Optional.of(existing));

        ratingService.delete(id);

        verify(ratingRepository).delete(existing);
    }
}