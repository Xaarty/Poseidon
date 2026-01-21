package com.nnk.springboot.service;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
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
class CurvePointServiceTest {

    @Mock
    private CurvePointRepository curvePointRepository;

    @InjectMocks
    private CurvePointService curvePointService;

    @Test
    void findAll() {
        when(curvePointRepository.findAll()).thenReturn(List.of(
                new CurvePoint(1, 10d, 100d),
                new CurvePoint(2, 20d, 200d)
        ));

        List<CurvePoint> res = curvePointService.findAll();

        assertEquals(2, res.size());
        verify(curvePointRepository).findAll();
    }

    @Test
    void findByIdSuccess() {
        CurvePoint cp = new CurvePoint(1, 10d, 100d);
        cp.setId(1);

        when(curvePointRepository.findById(1)).thenReturn(Optional.of(cp));

        CurvePoint res = curvePointService.findById(1);

        assertNotNull(res);
        assertEquals(1, res.getId());
        verify(curvePointRepository).findById(1);
    }

    @Test
    void findByIdError() {
        when(curvePointRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> curvePointService.findById(999));
    }

    @Test
    void create() {
        CurvePoint cp = new CurvePoint(1, 10d, 100d);
        when(curvePointRepository.save(cp)).thenReturn(cp);

        CurvePoint saved = curvePointService.create(cp);

        assertNotNull(saved);
        verify(curvePointRepository).save(cp);
    }

    @Test
    void update() {
        Integer id = 1;

        CurvePoint existing = new CurvePoint(1, 10d, 100d);
        existing.setId(id);

        CurvePoint newData = new CurvePoint(2, 20d, 200d);

        when(curvePointRepository.findById(id)).thenReturn(Optional.of(existing));
        when(curvePointRepository.save(any(CurvePoint.class))).thenAnswer(inv -> inv.getArgument(0));

        CurvePoint updated = curvePointService.update(id, newData);

        assertEquals(id, updated.getId());
        assertEquals(2, updated.getCurveId().intValue());
        assertEquals(20d, updated.getTerm(), 0.0001);
        assertEquals(200d, updated.getValue(), 0.0001);

        verify(curvePointRepository).save(existing);
    }

    @Test
    void delete() {
        Integer id = 1;

        CurvePoint existing = new CurvePoint(1, 10d, 100d);
        existing.setId(id);

        when(curvePointRepository.findById(id)).thenReturn(Optional.of(existing));

        curvePointService.delete(id);

        verify(curvePointRepository).delete(existing);
    }
}
