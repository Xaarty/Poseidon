package com.nnk.springboot.service;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
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
class TradeServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    @InjectMocks
    private TradeService tradeService;

    @Test
    void findAll_shouldReturnList() {
        when(tradeRepository.findAll()).thenReturn(List.of(
                new Trade("A1", "T1"),
                new Trade("A2", "T2")
        ));

        List<Trade> res = tradeService.findAll();

        assertEquals(2, res.size());
        verify(tradeRepository).findAll();
    }

    @Test
    void findById_shouldThrow_whenNotFound() {
        when(tradeRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> tradeService.findById(999));
    }

    @Test
    void create_shouldSave() {
        Trade t = new Trade("Acc", "Type");
        when(tradeRepository.save(t)).thenReturn(t);

        Trade saved = tradeService.create(t);

        assertNotNull(saved);
        verify(tradeRepository).save(t);
    }

    @Test
    void update_shouldCopyFields_andSave() {
        Integer id = 1;

        Trade existing = new Trade("OldAcc", "OldType");
        existing.setTradeId(id);

        Trade newData = new Trade("NewAcc", "NewType");

        when(tradeRepository.findById(id)).thenReturn(Optional.of(existing));
        when(tradeRepository.save(any(Trade.class))).thenAnswer(inv -> inv.getArgument(0));

        Trade updated = tradeService.update(id, newData);

        assertEquals("NewAcc", updated.getAccount());
        assertEquals("NewType", updated.getType());

        verify(tradeRepository).save(existing);
    }

    @Test
    void delete_shouldFindAndDelete() {
        Integer id = 1;

        Trade existing = new Trade("Acc", "Type");
        existing.setTradeId(id);

        when(tradeRepository.findById(id)).thenReturn(Optional.of(existing));

        tradeService.delete(id);

        verify(tradeRepository).delete(existing);
    }
}
