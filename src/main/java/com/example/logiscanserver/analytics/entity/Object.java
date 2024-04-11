package com.example.logiscanserver.analytics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "objects")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Object implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "object", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ObjectCount> objectCounts;
}
