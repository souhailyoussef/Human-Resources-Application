package com.example.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Roles")
public class Role {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer role_id;
    @Column(unique = true)
    private String rolename;
}
