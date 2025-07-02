package br.csi.api.controller;

import br.csi.api.service.UsuarioService;
import br.csi.api.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/cadastrar")
    public String cadastroUsuario() {
        return "cadastroUsuario";
    }

    @PostMapping("/criarUsuario")
    public String criarUsuario(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        usuarioService.salvarUsuario(usuario);
        redirectAttributes.addFlashAttribute("sucesso", "Usuário cadastrado!");
        return "redirect:/usuarios/listar";
    }

    @GetMapping("/listar")
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.listarTodos();
        model.addAttribute("usuarios", usuarios); // envia para a página
        return "listarUser";
    }

    @GetMapping("/{id}")
    public Usuario buscarUsuarioPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id).orElse(null);
    }

    @GetMapping("/deletar/{id}")
    public String deletarUsuarioPorId(@PathVariable Long id) {
        usuarioService.deletarPorId(id);
        return "redirect:/usuarios/listar";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id).orElse(null);

        if (usuario == null) return "redirect:/usuarios/listar";
        model.addAttribute("usuario", usuario);
        return "editarUsuario";
    }

    @PostMapping("/atualizar")
    public String atualizarUsuario(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        Usuario usuarioExistente = usuarioService.buscarPorId(usuario.getId()).orElse(null);
        if (usuarioExistente == null) return "redirect:/usuarios/listar";

        if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
            usuario.setSenha(usuarioExistente.getSenha());
        }

        if (usuario.getTipo() == null) {
            usuario.setTipo(usuarioExistente.getTipo());
        }

        usuarioService.salvarUsuario(usuario);
        redirectAttributes.addFlashAttribute("sucesso", "Usuário atualizado com sucesso!");
        return "redirect:/usuarios/listar";
    }

    @GetMapping("/meus-dados")
    public String meusDados(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        model.addAttribute("usuario", usuario);
        return "editarUsuario";
    }

}