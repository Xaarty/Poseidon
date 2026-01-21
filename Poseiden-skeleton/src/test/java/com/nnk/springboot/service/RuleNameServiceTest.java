package com.nnk.springboot.service;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
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
class RuleNameServiceTest {

    @Mock
    private RuleNameRepository ruleNameRepository;

    @InjectMocks
    private RuleNameService ruleNameService;

    @Test
    void findAll() {
        when(ruleNameRepository.findAll()).thenReturn(List.of(
                new RuleName("n1", "d1", "j1", "t1", "s1", "p1"),
                new RuleName("n2", "d2", "j2", "t2", "s2", "p2")
        ));

        List<RuleName> res = ruleNameService.findAll();

        assertEquals(2, res.size());
        verify(ruleNameRepository).findAll();
    }

    @Test
    void findByIdError() {
        when(ruleNameRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> ruleNameService.findById(999));
    }

    @Test
    void create() {
        RuleName rn = new RuleName("n", "d", "j", "t", "s", "p");
        when(ruleNameRepository.save(rn)).thenReturn(rn);

        RuleName saved = ruleNameService.create(rn);

        assertNotNull(saved);
        verify(ruleNameRepository).save(rn);
    }

    @Test
    void update() {
        Integer id = 1;

        RuleName existing = new RuleName("oldN", "oldD", "oldJ", "oldT", "oldS", "oldP");
        existing.setId(id);

        RuleName newData = new RuleName("newN", "newD", "newJ", "newT", "newS", "newP");

        when(ruleNameRepository.findById(id)).thenReturn(Optional.of(existing));
        when(ruleNameRepository.save(any(RuleName.class))).thenAnswer(inv -> inv.getArgument(0));

        RuleName updated = ruleNameService.update(id, newData);

        assertEquals("newN", updated.getName());
        assertEquals("newD", updated.getDescription());
        assertEquals("newJ", updated.getJson());
        assertEquals("newT", updated.getTemplate());
        assertEquals("newS", updated.getSqlStr());
        assertEquals("newP", updated.getSqlPart());

        verify(ruleNameRepository).save(existing);
    }

    @Test
    void delete() {
        Integer id = 1;
        RuleName existing = new RuleName("n", "d", "j", "t", "s", "p");
        existing.setId(id);

        when(ruleNameRepository.findById(id)).thenReturn(Optional.of(existing));

        ruleNameService.delete(id);

        verify(ruleNameRepository).delete(existing);
    }
}