package br.csi.api.controller;

import br.csi.api.model.CanalVendas;
import br.csi.api.model.Cultura;
import br.csi.api.model.Cultivo;
import br.csi.api.model.Propriedade;
import br.csi.api.repository.CanalVendasRepository;
import br.csi.api.repository.CulturaRepository;
import br.csi.api.repository.PropriedadeRepository;
import br.csi.api.service.CanalVendasService;
import br.csi.api.service.CulturaService;
import br.csi.api.service.CultivoService;
import br.csi.api.service.PropriedadeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cultivos")
public class CultivoController {


    @Autowired
    private CultivoService cultivoService;
    @Autowired
    private CulturaService culturaService;
    @Autowired
    private CanalVendasService canalVendasService;
    @Autowired
    private CulturaRepository culturaRepository;
    @Autowired
    private CanalVendasRepository canalVendasRepository;
    @Autowired
    private PropriedadeService propriedadeService;


    @GetMapping("/cadastrar")
    public String mostrarFormularioCadastro(@RequestParam("propriedadeId") Long propriedadeId, Model model) {
        Propriedade propriedade = propriedadeService.buscarPorId(propriedadeId)
                .orElseThrow(() -> new IllegalArgumentException("Propriedade inválida ID:" + propriedadeId));

        Cultivo cultivo = new Cultivo();
        cultivo.setPropriedade(propriedade);

        // 3. NOVO: Busca a lista de cultivos que JÁ EXISTEM para esta propriedade.
        //    Estou usando o cultivoService, o que é uma boa prática.
        List<Cultivo> cultivosDaPropriedade = cultivoService.listarPorPropriedade(propriedade);

        model.addAttribute("cultivo", cultivo);
        model.addAttribute("culturas", culturaService.listarTodas());
        model.addAttribute("canaisVendas", canalVendasService.listarTodos());

        // 5. NOVO: Adiciona a lista de cultivos existentes para a tabela que criamos.
        model.addAttribute("cultivosDaPropriedade", cultivosDaPropriedade);

        return "cadastroCultivo";
    }


    @PostMapping("/salvar")
    @Transactional
    public String salvarCultivo(@Valid @ModelAttribute("cultivo") Cultivo cultivo,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        cultivoService.salvarCultivo(cultivo);

        redirectAttributes.addFlashAttribute("sucesso", "Cultivo cadastrado com sucesso!");
        return "redirect:/propriedade/listar"; // Ou para onde for mais apropriado
    }

    /**
     * Exibe a lista de todos os cultivos cadastrados.
     * (Este método pode ser mantido para uma visão geral, se desejado)
     */
    @GetMapping("/listar")
    public String listarCultivos(Model model) {
        List<Cultivo> todosOsCultivos = cultivoService.listarTodosParaTabela();
        model.addAttribute("cultivos", todosOsCultivos);
        return "listar-cultivos";
    }

    /**
     * Exibe o formulário para editar um cultivo existente.
     * O método em si não precisa de grandes mudanças, pois ele busca o cultivo
     * que já tem a propriedade associada.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model) {

        // PASSO 1: Busca o cultivo a ser editado e suas associações diretas (Propriedade, Cultura, Canal).
        // Usamos o método otimizado para evitar erros de Lazy Loading no próprio formulário.
        Cultivo cultivo = cultivoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de Cultivo inválido: " + id));

        // PASSO 2: DECLARA A VARIÁVEL QUE FALTAVA.
        // Agora que temos o objeto 'cultivo', podemos pegar o ID da sua propriedade.
        Long propriedadeId = cultivo.getPropriedade().getId();

        // PASSO 3: Busca as listas completas para os menus <select> do formulário.
        List<Cultura> culturas = culturaService.listarTodas();
        List<CanalVendas> canaisVendas = canalVendasService.listarTodos();

        List<Cultivo> cultivosDaPropriedade = cultivoService.listarTodos(propriedadeId);

        // PASSO 5: Adiciona tudo o que a página precisa ao Model.
        model.addAttribute("cultivo", cultivo);
        model.addAttribute("culturas", culturas);
        model.addAttribute("canaisVendas", canaisVendas);
        model.addAttribute("cultivosDaPropriedade", cultivosDaPropriedade);

        // PASSO 6: Retorna o nome do template.
        return "cadastroCultivo";
    }

    /**
     * Processa a atualização de um cultivo existente.
     */
    @PostMapping("/atualizar")
    @Transactional
    public String atualizarCultivo(@ModelAttribute("cultivo") Cultivo cultivoDoFormulario,
                                   RedirectAttributes redirectAttributes) {

        if (cultivoDoFormulario.getId() == null) {
            redirectAttributes.addFlashAttribute("erro", "ID do cultivo não fornecido para atualização.");
            return "redirect:/propriedade/listar";
        }

        Cultivo cultivoExistente = cultivoService.buscarPorId(cultivoDoFormulario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Cultivo com ID " + cultivoDoFormulario.getId() + " não encontrado."));

        // Atualiza os campos
        cultivoExistente.setReceita(cultivoDoFormulario.getReceita());
        cultivoExistente.setAno_implantacao(cultivoDoFormulario.getAno_implantacao());
        cultivoExistente.setNumero_plantas(cultivoDoFormulario.getNumero_plantas());
        cultivoExistente.setVenda_canal(cultivoDoFormulario.getVenda_canal());
        cultivoExistente.setNumero_pontos_venda(cultivoDoFormulario.getNumero_pontos_venda());
        cultivoExistente.setDistancia_entrega(cultivoDoFormulario.getDistancia_entrega());

        // associações
        if (cultivoDoFormulario.getCultura() != null && cultivoDoFormulario.getCultura().getId() != null) {
            Cultura culturaSelecionada = culturaRepository.findById(cultivoDoFormulario.getCultura().getId()).orElseThrow(() -> new EntityNotFoundException("Cultura não encontrada"));
            cultivoExistente.setCultura(culturaSelecionada);
        }

        if (cultivoDoFormulario.getCanal() != null && cultivoDoFormulario.getCanal().getId() != null) {
            CanalVendas canalSelecionado = canalVendasRepository.findById(cultivoDoFormulario.getCanal().getId()).orElseThrow(() -> new EntityNotFoundException("Canal não encontrado"));
            cultivoExistente.setCanal(canalSelecionado);
        }

        // garante que a propriedade seja mantida na atualização -
        if (cultivoDoFormulario.getPropriedade() != null && cultivoDoFormulario.getPropriedade().getId() != null) {
            Propriedade propriedadeSelecionada = propriedadeService.buscarPorId(cultivoDoFormulario.getPropriedade().getId()).orElseThrow(() -> new EntityNotFoundException("Propriedade não encontrada"));
            cultivoExistente.setPropriedade(propriedadeSelecionada);
        } else {
            redirectAttributes.addFlashAttribute("erro", "A propriedade associada não pode ser nula.");
            return "redirect:/propriedade/listar";
        }

        cultivoService.salvarCultivo(cultivoExistente);

        redirectAttributes.addFlashAttribute("sucesso", "Cultivo atualizado com sucesso!");

        return "redirect:/propriedade/listar";
    }

    /**
     * Deleta um cultivo pelo seu ID.
     */
    @GetMapping("/deletar/{id}")
    public String deletarCultivo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Cultivo cultivo = cultivoService.buscarPorId(id).orElse(null);
        if (cultivo == null) {
            redirectAttributes.addFlashAttribute("erro", "Cultivo não encontrado.");
            return "redirect:/"; // Redireciona para a home
        }
        Long propriedadeId = cultivo.getPropriedade().getId();

        try {
            cultivoService.deletarPorId(id);
            redirectAttributes.addFlashAttribute("sucesso", "Cultivo deletado com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao deletar o cultivo.");
        }

        // --- A CORREÇÃO ESTÁ AQUI ---
        // 1. Adicione o ID da propriedade como um atributo para o redirecionamento.
        redirectAttributes.addAttribute("propriedadeId", propriedadeId);

        // 2. Retorne apenas o caminho base do mapeamento.
        //    O Spring irá adicionar "?propriedadeId=14" automaticamente.
        return "redirect:/cultivos/cadastrar";
    }
}