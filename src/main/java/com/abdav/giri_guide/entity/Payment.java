package com.abdav.giri_guide.entity;

import com.abdav.giri_guide.constant.PathDb;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = PathDb.PAYMENT)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Payment extends AuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String token;

    private String redirectUrl;

    private String transactionStatus;

    @OneToOne(mappedBy = "payment")
    private TransactionPayment transactionPayment;
}
