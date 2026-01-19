package com.nnk.springboot.service;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RuleNameService {

    private final RuleNameRepository ruleNameRepository;

    public RuleNameService(RuleNameRepository ruleNameRepository) {
        this.ruleNameRepository = ruleNameRepository;
    }

    public List<RuleName> findAll() {
        return ruleNameRepository.findAll();
    }

    public RuleName findById(Integer id) {
        return ruleNameRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("RuleName introuvable id=" + id));
    }

    public RuleName create(RuleName ruleName) {
        return ruleNameRepository.save(ruleName);
    }

    public RuleName update(Integer id, RuleName ruleName) {
        RuleName existing = findById(id);

        existing.setName(ruleName.getName());
        existing.setDescription(ruleName.getDescription());
        existing.setJson(ruleName.getJson());
        existing.setTemplate(ruleName.getTemplate());
        existing.setSqlStr(ruleName.getSqlStr());
        existing.setSqlPart(ruleName.getSqlPart());

        return ruleNameRepository.save(existing);
    }

    public void delete(Integer id) {
        RuleName existing = findById(id);
        ruleNameRepository.delete(existing);
    }
}
