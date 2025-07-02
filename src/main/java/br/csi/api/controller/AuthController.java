package br.csi.api.controller;

import br.csi.api.model.TipoUsuario;
import br.csi.api.model.Usuario;
import br.csi.api.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UsuarioRepository usuarioRepository;

    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String senha,
            HttpSession session
    ) {
        // Busca usuário com email e senha em texto claro
        Usuario usuario = usuarioRepository.findByEmailAndSenha(email, senha);

        if (usuario != null) {
            session.setAttribute("usuarioLogado", usuario);
            System.out.println("Tipo do usuário: " + usuario.getTipo());
            session.setMaxInactiveInterval(30 * 60);
            return "redirect:/dashboard";
        } else {
            System.out.println("Usuário não encontrado");
            return "redirect:/login?erro";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Destrói a sessão
        return "redirect:/login";
    }
}