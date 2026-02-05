package com.nnk.springboot.service;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    /**
     * Récupération de tous les Rating
     */
    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }

    /**
     * Récupération d'un Rating par identifiant unique
     */
    public Rating findById(Integer id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Rating introuvable id=" + id));
    }

    /**
     * Création d'un Rating
     */
    public Rating create(Rating rating) {
        return ratingRepository.save(rating);
    }

    /**
     * Modification d'un Rating par identifiant unique
     */
    public Rating update(Integer id, Rating rating) {
        Rating existing = findById(id);

        existing.setMoodysRating(rating.getMoodysRating());
        existing.setSandPRating(rating.getSandPRating());
        existing.setFitchRating(rating.getFitchRating());
        existing.setOrderNumber(rating.getOrderNumber());

        return ratingRepository.save(existing);
    }

    /**
     * Suppression d'un Rating par identifiant unique
     */
    public void delete(Integer id) {
        Rating existing = findById(id);
        ratingRepository.delete(existing);
    }
}