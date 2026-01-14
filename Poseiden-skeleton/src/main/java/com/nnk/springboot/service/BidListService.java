package com.nnk.springboot.service;

import com.nnk.springboot.domain.BidList;
import java.util.List;
import java.util.NoSuchElementException;

import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.stereotype.Service;
@Service
public class BidListService {

    private final BidListRepository bidListRepository;

    public BidListService(BidListRepository bidListRepository) {
        this.bidListRepository = bidListRepository;
    }

    public List<BidList> findAll() {
        return bidListRepository.findAll();
    }

    public BidList findById(Integer id) {
        return bidListRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("BidList not found id=" + id));
    }

    public BidList create(BidList bid) {
        return bidListRepository.save(bid);
    }

    public BidList update(Integer id, BidList bid) {
        BidList existing = findById(id);

        existing.setAccount(bid.getAccount());
        existing.setType(bid.getType());
        existing.setBidQuantity(bid.getBidQuantity());
        existing.setAskQuantity(bid.getAskQuantity());
        existing.setBid(bid.getBid());
        existing.setAsk(bid.getAsk());
        existing.setBenchmark(bid.getBenchmark());
        existing.setBidListDate(bid.getBidListDate());
        existing.setCommentary(bid.getCommentary());

        return bidListRepository.save(existing);
    }

    public void delete(Integer id) {
        BidList existing = findById(id);
        bidListRepository.delete(existing);
    }
}