package com.abdav.giri_guide.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "m_deposit")
@EqualsAndHashCode(callSuper = true)
@Entity
public class Deposit extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "tour_guide_id", referencedColumnName = "id")
    private TourGuide tourGuide;

    @Builder.Default
    private Long money = 0L;

    @Builder.Default
    @OneToMany(mappedBy = "deposit")
    private List<DepositHistory> histories = new ArrayList<>();
}
