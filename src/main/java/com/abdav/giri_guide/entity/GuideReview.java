package com.abdav.giri_guide.entity;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.*;
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
@Table(name = "m_guide_review")
@EqualsAndHashCode(callSuper = true)
@Entity
public class GuideReview extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "tour_guide_id", referencedColumnName = "id")
    private TourGuide tourGuide;

    // #TODO remove nullable
    @Nullable
    @OneToOne
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    private Transaction transaction;

    private Boolean usePorter;

    @Builder.Default
    private Integer rating = 5;

    @Nullable
    private String review;
}
