package com.endava.expensesmanager.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "document")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer documentId;
    @Column(name = "name")
    private String name;
    public Document(String name) {
        this.name = name;
    }
}
