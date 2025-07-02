package br.csi.api.service;

import br.csi.api.model.Cidade;
import br.csi.api.model.Propriedade;
import br.csi.api.repository.CidadeRepository;
import br.csi.api.repository.PropriedadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CidadeService {

    @Autowired
    private CidadeRepository cidadeRepository;

    public List<Cidade> listarTodas(){
        return cidadeRepository.findAll();
    }

    public Optional<Cidade> buscarPorId(Long id) {
        return cidadeRepository.findById(id);
    }
}
