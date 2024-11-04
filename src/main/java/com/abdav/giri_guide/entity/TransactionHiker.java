package com.abdav.giri_guide.entity;

import com.abdav.giri_guide.constant.PathDb;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Entity
@Table(name = PathDb.TRANSACTION_DETAIL)
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TransactionHiker extends AuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String fullName;

    private String nik;

    private Date birthDate;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;
}
