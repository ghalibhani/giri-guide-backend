package com.abdav.giri_guide.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "m_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends AuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private  String id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
