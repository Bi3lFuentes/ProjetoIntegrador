package br.csi.api.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "propriedades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Propriedade {

    @OneToMany(mappedBy = "propriedade", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Cultivo> cultivos = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_cidade") // Simplificado
    private Cidade cidade;

    @Column(length = 100)
    private String nome_entrevistado;

    @Column(length = 100)
    private String localidade;

    @Column(length = 20)
    private String telefone;

    @Column(precision = 10, scale = 2)
    private BigDecimal distancia_municipio;

    @Column(length = 50)
    private String coordenadas;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate data_coleta;

    @Column(precision = 5, scale = 2)
    private BigDecimal renda_fruticultura;

    @Enumerated(EnumType.STRING)
    private SistemaCultivo sistema_cultivo;

    @Enumerated(EnumType.STRING)
    private ManejoSoloAdubacao manejo_solo_adubacao;

    private Boolean irrigacao;

    private Boolean assistencia_tecnica;

    private Boolean camera_fria;

    private Boolean galpao_beneficiamento;

    private Boolean maquina_colheita;

    private Boolean estufas;

    private Boolean trator;

    private Boolean pulverizador_costal;

    private Boolean tesoura_eletrica_poda;

    private Boolean micro_trator;

    private Boolean pulverizador_motorizado;

    private Boolean turbo_atomizador_motorizado;

    private Boolean veiculo_motorizado_transporte;

    @Column
    private Integer numPessoasMaoObraFamiliar;

    @Column
    private Integer numPessoasContratadas;

    @Column
    private Integer mediaDiariasPagasAno;

    @Column
    private Boolean manejo_fitossanitario_quimico;

    @Column
    private Boolean manejo_fitossanitario_biologico;

    @Column
    private Boolean manejo_fitossanitario_cultural;


}
