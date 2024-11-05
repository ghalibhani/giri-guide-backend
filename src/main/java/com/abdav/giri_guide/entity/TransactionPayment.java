package com.abdav.giri_guide.entity;

import com.abdav.giri_guide.constant.PathDb;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = PathDb.TRANSACTION_PAYMENT)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TransactionPayment extends AuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    private Long amount;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss a", timezone = "Asia/Jakarta")
    private Date date;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @PrePersist
    protected void onCreate(){
        date = new Date();
    }
}
