package com.nnk.springboot.service;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CurvePointService {

    private final CurvePointRepository curvePointRepository;

    public CurvePointService(CurvePointRepository curvePointRepository) {
        this.curvePointRepository = curvePointRepository;
    }

    public List<CurvePoint> findAll() {
        return curvePointRepository.findAll();
    }

    public CurvePoint findById(Integer id) {
        return curvePointRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("CurvePoint introuvable id=" + id));
    }

    public CurvePoint create(CurvePoint curvePoint) {
        return curvePointRepository.save(curvePoint);
    }

    public CurvePoint update(Integer id, CurvePoint curvePoint) {
        CurvePoint existing = findById(id);

        existing.setCurveId(curvePoint.getCurveId());
        existing.setTerm(curvePoint.getTerm());
        existing.setValue(curvePoint.getValue());

        return curvePointRepository.save(existing);
    }

    public void delete(Integer id) {
        CurvePoint existing = findById(id);
        curvePointRepository.delete(existing);
    }
}