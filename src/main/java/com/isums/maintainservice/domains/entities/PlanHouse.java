package com.isums.maintainservice.domains.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "plan_houses",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"plan_id","house_id"})
        })
public class PlanHouse {

    @Id
    @UuidGenerator
    @GeneratedValue
    private UUID id;

    private UUID planId;

    private UUID houseId;

    private Instant createdAt;
}
