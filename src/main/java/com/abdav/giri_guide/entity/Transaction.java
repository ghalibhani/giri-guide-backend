package com.abdav.giri_guide.entity;

import com.abdav.giri_guide.constant.ETransactionStatus;
import com.abdav.giri_guide.constant.PathDb;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = PathDb.TRANSACTION)
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Transaction extends AuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "tourguide_id")
    private TourGuide tourGuide;

    @ManyToOne
    private HikingPoint hikingPoint;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private ETransactionStatus status;

    private Integer porterQty;

    private Long totalPorterPrice;

    private Long totalTourGuidePrice;

    private Long additionalPriceTourGuide;

    private Long totalSimaksiPrice;

    private Long totalEntryPrice;

    private Long adminCost;

    private Long totalPrice;


    @OneToMany(mappedBy = "transaction", targetEntity = TransactionHiker.class)
    @JsonManagedReference
    private List<TransactionHiker> transactionHikers;
}

