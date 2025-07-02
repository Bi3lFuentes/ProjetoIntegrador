package br.csi.api.service;

import br.csi.api.model.CanalVendas;
import br.csi.api.model.Cidade;
import br.csi.api.repository.CanalVendasRepository;
import br.csi.api.repository.CidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CanalVendasService {

    @Autowired
    private CanalVendasRepository canalVendasRepository;

    public List<CanalVendas> listarTodos(){
        return canalVendasRepository.findAll();
    }

    public Optional<CanalVendas> buscarPorId(Long id) {
        return canalVendasRepository.findById(id);
    }
}
