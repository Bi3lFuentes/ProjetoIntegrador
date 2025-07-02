package br.csi.api.controller;

import br.csi.api.model.Cidade;
import br.csi.api.model.Propriedade;
import br.csi.api.model.Usuario;
import br.csi.api.repository.CidadeRepository;
import br.csi.api.repository.PropriedadeRepository;
import br.csi.api.service.CidadeService;
import br.csi.api.service.PropriedadeService;
import br.csi.api.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/propriedade")
public class PropriedadeController {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private PropriedadeService propriedadeService;

    @Autowired
    private CidadeService cidadeService;

    @GetMapping("/cadastrar")
    public String cadastroPropriedade(Model model) {

        List<Cidade> cidades = cidadeService.listarTodas(); // ou cidadeService.listarTodas()
        model.addAttribute("cidades", cidades);
        model.addAttribute("propriedade", new Propriedade()); // Supondo que esse é o objeto do form
        return "cadastroProp";
    }

    @PostMapping("/criarPropriedade")
    @Transactional
    public String criarPropriedade(
            @ModelAttribute Propriedade propriedade,
            @RequestParam("cidade.id") Long cidadeId) {

        // busca a cidade existente no banco
        Cidade cidade = cidadeRepository.findById(cidadeId)
                .orElseThrow(() -> new EntityNotFoundException("Cidade não encontrada"));

        propriedade.setCidade(cidade);

        propriedadeService.salvarPropriedade(propriedade);

        return "redirect:/propriedade/listar";
    }

    @GetMapping("listar")
    public String listarPropriedade(Model model) {
        List<Propriedade> propriedades = propriedadeService.listarTodas();
        model.addAttribute("propriedades", propriedades);
        return "listar-propriedades-cultivo";
    }

    @GetMapping("/{id}")
    public Propriedade buscarPropriedadePorId(@PathVariable Long id) {
        return propriedadeService.buscarPorId(id).orElse(null);
    }

    @GetMapping("/deletar/{id}")
    public String deletarPropriedadePorId(@PathVariable Long id) {
        propriedadeService.deletarPorId(id);
        return "listar-propriedades-cultivo";
    }

    @GetMapping("/editar/{id}")
    public String editarPropriedade(@PathVariable Long id, Model model) {
        Propriedade propriedade = propriedadeService.buscarPorId(id).orElse(null);

        if (propriedade != null && propriedade.getData_coleta() == null) {
            propriedade.setData_coleta(LocalDate.now()); // Define data atual como padrão se estiver nula
        }

        model.addAttribute("propriedade", propriedade);
        List<Cidade> cidades = cidadeService.listarTodas();
        model.addAttribute("todasAsCidades", cidades);
        return "editarProp";
    }
    

    @PostMapping("/atualizar")
    @Transactional
    public String atualizarPropriedade(@ModelAttribute("propriedade") Propriedade propriedadeDoFormulario,
                                       RedirectAttributes redirectAttributes) {

        if (propriedadeDoFormulario.getId() == null) {
            redirectAttributes.addFlashAttribute("erro", "ID da propriedade não fornecido para atualização.");
            return "redirect:/propriedade/listar";
        }

        // busca a propriedade existente no banco de dados
        Propriedade propriedadeExistente = propriedadeService.buscarPorId(propriedadeDoFormulario.getId())
                .orElse(null);

        if (propriedadeExistente == null) {
            redirectAttributes.addFlashAttribute("erro", "Propriedade com ID " + propriedadeDoFormulario.getId() + " não encontrada.");
            return "redirect:/propriedade/listar";
        }

        // atualiza os campos da entidade existente com os valores do formulário
        propriedadeExistente.setNome_entrevistado(propriedadeDoFormulario.getNome_entrevistado());
        propriedadeExistente.setLocalidade(propriedadeDoFormulario.getLocalidade());
        propriedadeExistente.setTelefone(propriedadeDoFormulario.getTelefone());
        propriedadeExistente.setDistancia_municipio(propriedadeDoFormulario.getDistancia_municipio());
        propriedadeExistente.setCoordenadas(propriedadeDoFormulario.getCoordenadas());
        propriedadeExistente.setData_coleta(propriedadeDoFormulario.getData_coleta());
        propriedadeExistente.setRenda_fruticultura(propriedadeDoFormulario.getRenda_fruticultura());
        propriedadeExistente.setNumPessoasMaoObraFamiliar(propriedadeDoFormulario.getNumPessoasMaoObraFamiliar());
        propriedadeExistente.setNumPessoasContratadas(propriedadeDoFormulario.getNumPessoasContratadas());
        propriedadeExistente.setMediaDiariasPagasAno(propriedadeDoFormulario.getMediaDiariasPagasAno());
        propriedadeExistente.setSistema_cultivo(propriedadeDoFormulario.getSistema_cultivo());
        propriedadeExistente.setManejo_solo_adubacao(propriedadeDoFormulario.getManejo_solo_adubacao());
        propriedadeExistente.setManejo_fitossanitario_quimico(propriedadeDoFormulario.getManejo_fitossanitario_quimico());
        propriedadeExistente.setManejo_fitossanitario_biologico(propriedadeDoFormulario.getManejo_fitossanitario_biologico());
        propriedadeExistente.setManejo_fitossanitario_cultural(propriedadeDoFormulario.getManejo_fitossanitario_cultural());

        // atualizar todos os campos booleanos de infraestrutura
        propriedadeExistente.setIrrigacao(propriedadeDoFormulario.getIrrigacao());
        propriedadeExistente.setAssistencia_tecnica(propriedadeDoFormulario.getAssistencia_tecnica());
        propriedadeExistente.setCamera_fria(propriedadeDoFormulario.getCamera_fria());
        propriedadeExistente.setGalpao_beneficiamento(propriedadeDoFormulario.getGalpao_beneficiamento());
        propriedadeExistente.setMaquina_colheita(propriedadeDoFormulario.getMaquina_colheita());
        propriedadeExistente.setEstufas(propriedadeDoFormulario.getEstufas());
        propriedadeExistente.setTrator(propriedadeDoFormulario.getTrator());
        propriedadeExistente.setPulverizador_costal(propriedadeDoFormulario.getPulverizador_costal());
        propriedadeExistente.setTesoura_eletrica_poda(propriedadeDoFormulario.getTesoura_eletrica_poda());
        propriedadeExistente.setMicro_trator(propriedadeDoFormulario.getMicro_trator());
        propriedadeExistente.setPulverizador_motorizado(propriedadeDoFormulario.getPulverizador_motorizado());
        propriedadeExistente.setTurbo_atomizador_motorizado(propriedadeDoFormulario.getTurbo_atomizador_motorizado());
        propriedadeExistente.setVeiculo_motorizado_transporte(propriedadeDoFormulario.getVeiculo_motorizado_transporte());



        // lidar com a associação da Cidade
        if (propriedadeDoFormulario.getCidade() != null && propriedadeDoFormulario.getCidade().getId() != null) {
            Cidade cidadeSelecionada = cidadeService.buscarPorId(propriedadeDoFormulario.getCidade().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Cidade selecionada não encontrada com ID: " + propriedadeDoFormulario.getCidade().getId()));
            propriedadeExistente.setCidade(cidadeSelecionada); // Associa a instância GERENCIADA de Cidade
        } else {
            propriedadeExistente.setCidade(null);
        }

        // salva a entidade existente que foi atualizada
        propriedadeService.salvarPropriedade(propriedadeExistente);

        redirectAttributes.addFlashAttribute("sucesso", "Propriedade atualizada com sucesso!");
        return "redirect:/propriedade/listar";
    }



}
