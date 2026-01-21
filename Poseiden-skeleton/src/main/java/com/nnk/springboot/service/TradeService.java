package com.nnk.springboot.service;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TradeService {

    private final TradeRepository tradeRepository;

    public TradeService(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    public List<Trade> findAll() {
        return tradeRepository.findAll();
    }

    public Trade findById(Integer id) {
        return tradeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Trade introuvable id=" + id));
    }

    public Trade create(Trade trade) {
        return tradeRepository.save(trade);
    }

    public Trade update(Integer id, Trade trade) {
        Trade existing = findById(id);

        existing.setAccount(trade.getAccount());
        existing.setType(trade.getType());
        existing.setBuyQuantity(trade.getBuyQuantity());
        existing.setSellQuantity(trade.getSellQuantity());
        existing.setBuyPrice(trade.getBuyPrice());
        existing.setSellPrice(trade.getSellPrice());
        existing.setTradeDate(trade.getTradeDate());

        return tradeRepository.save(existing);
    }

    public void delete(Integer id) {
        Trade existing = findById(id);
        tradeRepository.delete(existing);
    }
}
