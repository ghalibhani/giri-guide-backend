package com.abdav.giri_guide.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "m_location_route_node")
@EqualsAndHashCode(callSuper = true)
@Entity
public class LocationRouteNode extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "previous_location")
    private String from;

    @Column(name = "next_location")
    private String to;
    private String transportation;
    private String estimate;

    @Nullable
    @OneToOne
    @JoinColumn(name = "next_node_id", referencedColumnName = "id")
    private LocationRouteNode next;
}
