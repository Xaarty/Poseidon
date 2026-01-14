package com.nnk.springboot.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "CurvePoint")
public class CurvePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private Integer curveId;

    private Timestamp asOfDate;
    private Double term;
    private Double value;
    private Timestamp creationDate;

    public CurvePoint() {
    }

    public CurvePoint(Integer curveId, Double term, Double value) {
        this.curveId = curveId;
        this.term = term;
        this.value = value;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getCurveId() { return curveId; }
    public void setCurveId(Integer curveId) { this.curveId = curveId; }

    public Double getTerm() { return term; }
    public void setTerm(Double term) { this.term = term; }

    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }
}