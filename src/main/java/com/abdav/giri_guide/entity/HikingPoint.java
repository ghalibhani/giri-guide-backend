package com.abdav.giri_guide.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "m_hiking_point")
@EqualsAndHashCode(callSuper = true)
@Entity
public class HikingPoint extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(targetEntity = Mountains.class)
    @JoinColumn(name = "mountain_id", referencedColumnName = "id", nullable = false)
    private Mountains mountain;

    private String name;
    private String coordinate;

    @Builder.Default
    private Long price = 0L;

    @Builder.Default
    @OneToMany(mappedBy = "hikingPoint")
    List<TourGuideHikingPoint> tourGuideHikingPoints = new ArrayList<>();
}
