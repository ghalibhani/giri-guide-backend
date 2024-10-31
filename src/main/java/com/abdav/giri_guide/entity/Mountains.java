package com.abdav.giri_guide.entity;

import com.abdav.giri_guide.constant.EMountainStatus;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "m_mountains")
@EqualsAndHashCode(callSuper = true)
@Entity
public class Mountains extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;

    @Nullable
    private String image;
    private String city;

    @Column(columnDefinition = "Text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EMountainStatus status = EMountainStatus.NORMAL;

    @Nullable
    @Column(columnDefinition = "Text")
    private String message;
}
