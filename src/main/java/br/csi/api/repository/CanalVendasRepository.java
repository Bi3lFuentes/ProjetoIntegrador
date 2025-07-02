package br.csi.api.repository;

import br.csi.api.model.CanalVendas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CanalVendasRepository extends JpaRepository<CanalVendas, Long> {

    Optional<CanalVendas> findById(Long id);
}
