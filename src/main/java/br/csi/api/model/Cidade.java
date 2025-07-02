package br.csi.api.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cidades")
@Getter
@Setter
public class Cidade {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 50)
    private String nome;

    @Column(nullable = false, length = 2)
    private char uf;
}
