package com.kolege.assignment.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "conversion")
@Data
@NoArgsConstructor
public class Conversion{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private ZonedDateTime createdDate = ZonedDateTime.now();

    @Column(name = "source_unit")
    private String source;

    @Column(name = "target_unit")
    private String target;

    @Column(name = "source_amount")
    private Double sourceAmount;

    @Column(name = "exchange_rate")
    private Double rate;

    @Column(name = "target_amount")
    private Double targetAmount;


    public Conversion(String source, String target, Double sourceAmount, Double targetAmount, Double rate){
        this.source = source;
        this.target = target;
        this.sourceAmount = sourceAmount;
        this.targetAmount = targetAmount;
        this.rate = rate;
    }

}
