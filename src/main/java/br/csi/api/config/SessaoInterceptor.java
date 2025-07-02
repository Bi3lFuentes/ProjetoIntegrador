package br.csi.api.config;

import br.csi.api.model.Usuario;
import br.csi.api.model.TipoUsuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class SessaoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        boolean logado = (session != null && session.getAttribute("usuarioLogado") != null);

        String uri = request.getRequestURI();

        // permitir acesso a paginas publicas
        if (uri.equals("/login") || uri.startsWith("/css/") || uri.startsWith("/static.js/") || uri.startsWith("/imagens/")) {
            return true;
        }

        // bloqueia se nao estiver logado
        if (!logado) {
            response.sendRedirect("/login");
            return false;
        }

        // recupera o usuario da sessao
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        // bloquear acesso a rotas de admin se não for ADMINISTRADOR
        if ((uri.equals("/usuarios/cadastrar") || uri.matches("/usuarios/editar/\\d+") || uri.matches("/usuarios/deletar/\\d+"))
                && usuario.getTipo() != TipoUsuario.ADMINISTRADOR) {
            response.sendRedirect("/acesso-negado");
            return false;
        }

        return true;
    }
}
