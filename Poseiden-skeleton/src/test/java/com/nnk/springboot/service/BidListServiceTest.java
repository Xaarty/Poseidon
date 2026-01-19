package com.nnk.springboot.service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
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
class BidListServiceTest {

    @Mock
    private BidListRepository bidListRepository;

    @InjectMocks
    private BidListService bidListService;

    @Test
    void findAll() {
        when(bidListRepository.findAll()).thenReturn(List.of(
                new BidList("A1", "T1", 1d),
                new BidList("A2", "T2", 2d)
        ));

        List<BidList> res = bidListService.findAll();

        assertEquals(2, res.size());
        verify(bidListRepository).findAll();
    }

    @Test
    void findByIdSuccess() {
        BidList bid = new BidList("Acc", "Type", 10d);
        bid.setBidListId(1);

        when(bidListRepository.findById(1)).thenReturn(Optional.of(bid));

        BidList res = bidListService.findById(1);

        assertNotNull(res);
        assertEquals(1, res.getBidListId());
        verify(bidListRepository).findById(1);
    }

    @Test
    void findByIdError() {
        when(bidListRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> bidListService.findById(999));
    }

    @Test
    void create() {
        BidList bid = new BidList("Account Test", "Type Test", 10d);
        when(bidListRepository.save(bid)).thenReturn(bid);

        BidList saved = bidListService.create(bid);

        assertNotNull(saved);
        verify(bidListRepository).save(bid);
    }

    @Test
    void update() {
        Integer id = 1;

        BidList existing = new BidList("OldAcc", "OldType", 10d);
        existing.setBidListId(id);

        BidList newData = new BidList("NewAcc", "NewType", 20d);
        newData.setAskQuantity(5d);
        newData.setBid(1.1d);
        newData.setAsk(2.2d);
        newData.setBenchmark("bench");
        newData.setCommentary("comment");

        when(bidListRepository.findById(id)).thenReturn(Optional.of(existing));
        when(bidListRepository.save(any(BidList.class))).thenAnswer(inv -> inv.getArgument(0));

        BidList updated = bidListService.update(id, newData);

        assertEquals("NewAcc", updated.getAccount());
        assertEquals("NewType", updated.getType());
        assertEquals(20d, updated.getBidQuantity(), 0.0001);
        assertEquals(5d, updated.getAskQuantity(), 0.0001);
        assertEquals(1.1d, updated.getBid(), 0.0001);
        assertEquals(2.2d, updated.getAsk(), 0.0001);
        assertEquals("bench", updated.getBenchmark());
        assertEquals("comment", updated.getCommentary());

        verify(bidListRepository).save(existing);
    }

    @Test
    void delete() {
        Integer id = 1;

        BidList existing = new BidList("Acc", "Type", 10d);
        existing.setBidListId(id);

        when(bidListRepository.findById(id)).thenReturn(Optional.of(existing));

        bidListService.delete(id);

        verify(bidListRepository).delete(existing);
    }
}