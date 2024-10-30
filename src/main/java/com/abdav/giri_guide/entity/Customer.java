package com.abdav.giri_guide.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer extends AuditEntity{
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
