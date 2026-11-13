package com.example.client;


import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Integer id;
    private String name;
    private String rule;
    private Integer playerCount;
}
