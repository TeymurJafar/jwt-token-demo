package com.example.jwttokendemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import java.util.Date;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@DynamicInsert
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( unique = true,nullable = false)
    private String name;

    @CreationTimestamp
    @Column(updatable = false)
    private Date created;

    @ColumnDefault(value = "1")
    private Integer active = 1;

    public Role(String name) {
        this.name =name;
    }
}
