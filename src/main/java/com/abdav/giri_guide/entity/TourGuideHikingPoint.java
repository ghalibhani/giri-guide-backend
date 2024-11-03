package com.abdav.giri_guide.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "t_tour_guid_hiking_point")
@EqualsAndHashCode(callSuper = true)
@Entity
public class TourGuideHikingPoint extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "tour_guide_id", referencedColumnName = "id")
    private TourGuide tourGuide;

    @ManyToOne
    @JoinColumn(name = "hiking_point_id", referencedColumnName = "id")
    private HikingPoint hikingPoint;

    @Builder.Default
    private boolean isActive = true;
}
