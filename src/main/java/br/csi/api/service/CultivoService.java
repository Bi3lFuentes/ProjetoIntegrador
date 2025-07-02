package br.csi.api.service;

import br.csi.api.model.Cultivo;
import br.csi.api.model.Propriedade;
import br.csi.api.model.Usuario;
import br.csi.api.repository.CultivoRepository;
import br.csi.api.repository.PropriedadeRepository;
import br.csi.api.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CultivoService {

    @Autowired
    private CultivoRepository cultivoRepository;

    public Cultivo  salvarCultivo(Cultivo cultivo) throws IllegalArgumentException {

        // 1. Extrair os IDs necessários para a consulta a partir do objeto recebido.
        Long propriedadeId = cultivo.getPropriedade().getId();
        Long culturaId = cultivo.getCultura().getId();
        Long idDoProprioCultivo = cultivo.getId(); // Será null ao criar, e terá valor ao editar.

        // 2. Chamar o Repositório para obter a soma do 'venda_canal' dos outros registros.
        BigDecimal somaExistente = cultivoRepository
                .sumVendaCanalByPropriedadeAndCultura(propriedadeId, culturaId, idDoProprioCultivo)
                .orElse(BigDecimal.ZERO);

        // 3. Obter o valor do 'venda_canal' do cultivo atual.
        BigDecimal vendaAtual = cultivo.getVenda_canal() != null ? cultivo.getVenda_canal() : BigDecimal.ZERO;

        // 4. Calcular a soma total que teríamos se o cultivo atual fosse salvo.
        BigDecimal somaTotal = somaExistente.add(vendaAtual);

        // 5. A VALIDAÇÃO: Verificar se a soma total ultrapassa 100.
        if (somaTotal.compareTo(new BigDecimal("100")) > 0) {
            // Se ultrapassar, lançamos a exceção e a execução do método para.

            String mensagemDeErro = String.format(
                    "A soma do Canal de Venda para este cultivar ultrapassaria 100%%. (Soma já existente: %.2f%%)",
                    somaExistente
            );
            throw new IllegalArgumentException(mensagemDeErro);
        }

        // 6. PERSISTÊNCIA: Apenas se a validação passar, o cultivo é salvo.
        return cultivoRepository.save(cultivo);
    }

    public List<Cultivo> listarTodos(Long propriedadeId) {
        return cultivoRepository.findAllWithAssociationsForListing(propriedadeId);
    }

    public Optional<Cultivo> buscarPorId(Long id) {
        return cultivoRepository.findById(id);
    }

    public void deletarPorId(Long id) {
        cultivoRepository.deleteById(id);
    }

    public List<Cultivo> listarPorPropriedade(Propriedade propriedade) {
        return cultivoRepository.findByPropriedade(propriedade);
    }


    public List<Cultivo> listarTodosParaTabela() {
        return cultivoRepository.findAll();
    }


}

