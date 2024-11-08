package com.abdav.giri_guide.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.abdav.giri_guide.constant.EGender;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "m_tour_guide")
@EqualsAndHashCode(callSuper = true, exclude = "deposit")
@Entity
public class TourGuide extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User users;
    private String name;
    private EGender gender;
    private String nik;
    private Date birthDate;

    @Column(columnDefinition = "Text")
    private String description;

    @Column(columnDefinition = "Text")
    private String address;

    @Nullable
    @OneToOne
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private ImageEntity image;

    @Builder.Default
    private Integer maxHiker = 5;
    private Long price;
    private Long additionalPrice;
    private Integer totalPorter;
    private Long pricePorter;

    @Builder.Default
    private boolean isActive = true;

    @OneToOne(mappedBy = "tourGuide")
    private Deposit deposit;

    @OneToMany(mappedBy = "tourGuide")
    @Builder.Default
    private List<GuideReview> reviews = new ArrayList<>();

}
