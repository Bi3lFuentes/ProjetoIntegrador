package br.csi.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "cultivos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cultivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_propriedade", nullable = false)
    private Propriedade propriedade;

    @ManyToOne
    @JoinColumn(name = "id_cultura")
    private Cultura cultura;

    @ManyToOne
    @JoinColumn(name = "id_canal")
    private CanalVendas canal;

    @Column(precision = 10, scale = 2)
    private BigDecimal receita;

    @Column(precision = 10, scale = 2)
    private BigDecimal venda_canal;

    @Column(precision = 10, scale = 2)
    private BigDecimal distancia_entrega;

    @NotNull(message = "O ano de implantação é obrigatório.")
    private Integer ano_implantacao;

    @NotNull(message = "O número de plantas é obrigatório.")
    @Min(value = 0, message = "O número de plantas não pode ser negativo.")
    private Integer numero_plantas;

    @NotNull(message = "O número de pontos de venda é obrigatório.")
    @Min(value = 0, message = "O número de pontos de venda não pode ser negativo.")
    private Integer numero_pontos_venda;
}