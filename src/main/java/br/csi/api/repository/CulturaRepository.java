package br.csi.api.repository;

import br.csi.api.model.Cultura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CulturaRepository extends JpaRepository<Cultura, Long> {
    Optional<Cultura> findById(Long id);
}
