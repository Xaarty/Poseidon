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

    /**
     * Récupération de tous les CurvePoint
     */
    public List<CurvePoint> findAll() {
        return curvePointRepository.findAll();
    }

    /**
     * Récupération d'un CurvePoint par identifiant unique
     */
    public CurvePoint findById(Integer id) {
        return curvePointRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("CurvePoint introuvable id=" + id));
    }

    /**
     * Création d'un CurvePoint
     */
    public CurvePoint create(CurvePoint curvePoint) {
        return curvePointRepository.save(curvePoint);
    }

    /**
     * Modification d'un CurvePoint par identifiant unique
     */
    public CurvePoint update(Integer id, CurvePoint curvePoint) {
        CurvePoint existing = findById(id);

        existing.setCurveId(curvePoint.getCurveId());
        existing.setTerm(curvePoint.getTerm());
        existing.setValue(curvePoint.getValue());

        return curvePointRepository.save(existing);
    }

    /**
     * Suppression d'un CurvePoint par identifiant unique
     */
    public void delete(Integer id) {
        CurvePoint existing = findById(id);
        curvePointRepository.delete(existing);
    }
}