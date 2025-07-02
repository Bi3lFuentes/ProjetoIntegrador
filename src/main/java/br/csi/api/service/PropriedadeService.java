package br.csi.api.service;

import br.csi.api.model.Propriedade;
import br.csi.api.model.Usuario;
import br.csi.api.repository.PropriedadeRepository;
import br.csi.api.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropriedadeService {

    @Autowired
    private PropriedadeRepository   propriedadeRepository;

    public Propriedade salvarPropriedade(Propriedade propriedade) {
        // regras de negócio, validações, logs, etc
        return propriedadeRepository.save(propriedade);
    }

    public List<Propriedade> listarTodas() {
        // chama a consulta super otimizada
        return propriedadeRepository.findAllWithAllAssociations();
    }

    /*
    public List<Propriedade> listarTodas(){
        return propriedadeRepository.findAll();
    }
*/
    public Optional<Propriedade> buscarPorId(Long id) {
        return propriedadeRepository.findById(id);
    }

    public void deletarPorId(Long id) {
        propriedadeRepository.deleteById(id);
    }


}
