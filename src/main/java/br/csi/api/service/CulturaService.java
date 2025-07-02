package br.csi.api.service;

import br.csi.api.model.*;
import br.csi.api.repository.*;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CulturaService {

    @Autowired
    private CulturaRepository culturaRepository;

    public List<Cultura> listarTodas(){
        return culturaRepository.findAll();
    }

    public Optional<Cultura> buscarPorId(Long id) {
        return culturaRepository.findById(id);
    }

}