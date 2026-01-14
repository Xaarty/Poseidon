package com.nnk.springboot.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "BidList")
public class BidList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bidListId;

    @NotBlank
    private String account;

    @NotBlank
    private String type;

    @NotNull
    private Double bidQuantity;

    private Double askQuantity;
    private Double bid;
    private Double ask;
    private String benchmark;
    private Timestamp bidListDate;
    private String commentary;

    public BidList() {
    }

    public BidList(String account, String type, Double bidQuantity) {
        this.account = account;
        this.type = type;
        this.bidQuantity = bidQuantity;
    }

    public Integer getBidListId() { return bidListId; }
    public void setBidListId(Integer bidListId) { this.bidListId = bidListId; }

    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Double getBidQuantity() { return bidQuantity; }
    public void setBidQuantity(Double bidQuantity) { this.bidQuantity = bidQuantity; }

    public Double getAskQuantity() { return askQuantity; }
    public void setAskQuantity(Double askQuantity) { this.askQuantity = askQuantity; }

    public Double getBid() { return bid; }
    public void setBid(Double bid) { this.bid = bid; }

    public Double getAsk() { return ask; }
    public void setAsk(Double ask) { this.ask = ask; }

    public String getBenchmark() { return benchmark; }
    public void setBenchmark(String benchmark) { this.benchmark = benchmark; }

    public Timestamp getBidListDate() { return bidListDate; }
    public void setBidListDate(Timestamp bidListDate) { this.bidListDate = bidListDate; }

    public String getCommentary() { return commentary; }
    public void setCommentary(String commentary) { this.commentary = commentary; }
}