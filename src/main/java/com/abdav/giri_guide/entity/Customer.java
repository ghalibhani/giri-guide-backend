package com.abdav.giri_guide.entity;

import java.util.Date;

import com.abdav.giri_guide.constant.EGender;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Customer extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "full_name")
    private String fullName;

    private Date birthDate;

    private String nik;

    private String address;

    @Enumerated(EnumType.STRING)
    private EGender gender;

    @OneToOne
    @JoinColumn(name = "m_user_id", nullable = false, unique = true)
    private User user;
}
