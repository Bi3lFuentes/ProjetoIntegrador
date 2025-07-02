package br.csi.api.repository;

import br.csi.api.model.Cultivo;
import br.csi.api.model.Cultura;
import br.csi.api.model.Propriedade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

    public interface CultivoRepository extends JpaRepository<Cultivo, Long> {
        Optional<Cultivo> findById(Long id);

        List<Cultivo> findByPropriedade(Propriedade propriedade);

        @Query("SELECT c FROM Cultivo c " +
                "JOIN FETCH c.propriedade " +
                "JOIN FETCH c.cultura " +
                "JOIN FETCH c.canal " +
                "WHERE c.propriedade.id = :propriedadeId " +
                "ORDER BY c.id")
        List<Cultivo> findAllWithAssociationsForListing(@Param("propriedadeId") Long propriedadeId);


        //CANAL VENDA
        @Query("SELECT SUM(c.venda_canal) " +
                "FROM Cultivo c " +
                "WHERE c.propriedade.id = :propriedadeId " +
                "  AND c.cultura.id = :culturaId " +
                "  AND (:idDoCultivoAExcluir IS NULL OR c.id != :idDoCultivoAExcluir)")
        Optional<BigDecimal> sumVendaCanalByPropriedadeAndCultura(
                @Param("propriedadeId") Long propriedadeId,
                @Param("culturaId") Long culturaId,
                @Param("idDoCultivoAExcluir") Long idDoCultivoAExcluir
        );

/*
//RECEITA
@Query("SELECT SUM(c.receita) " +
        "FROM Cultivo c " +
        "WHERE c.propriedade.id = :propriedadeId " +
        "  AND (:idDoCultivoAExcluir IS NULL OR c.id != :idDoCultivoAExcluir)")
// NOME DO MÉTODO E PARÂMETROS FORAM SIMPLIFICADOS PARA REFLETIR A NOVA LÓGICA
Optional<BigDecimal> sumReceitaByPropriedade(
        @Param("propriedadeId") Long propriedadeId,
        @Param("idDoCultivoAExcluir") Long idDoCultivoAExcluir
);
*/
    }