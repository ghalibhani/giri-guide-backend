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

    private String guide;

    private LocalDateTime schedule;

    @Enumerated(EnumType.STRING)
    private ETransactionStatus status;

    private Integer porterQty;

    private Double pricePorter;

    private Double priceTourGuide;

    private Double additionalPriceTourGuide;

    private Double simaksiPrice;

    private Double entryPrice;

    private Double totalPrice;

    @ManyToOne
    private HikingPoint hikingPoint;

    @OneToMany(mappedBy = "transaction", targetEntity = TransactionHiker.class)
    @JsonManagedReference
    private List<TransactionHiker> transactionHikers;
}

