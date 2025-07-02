package br.csi.api.repository;

import br.csi.api.model.Propriedade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PropriedadeRepository extends JpaRepository<Propriedade, Long> {
    Optional<Propriedade> findById(Long id);

    @Query("SELECT DISTINCT p FROM Propriedade p " +
            "LEFT JOIN FETCH p.cultivos c " +
            "LEFT JOIN FETCH c.cultura " +
            "LEFT JOIN FETCH c.canal")
    List<Propriedade> findAllWithAllAssociations();
}
;