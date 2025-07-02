package br.csi.api.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AcessoController {

    @GetMapping("/acesso-negado")
    public String acessoNegado() {
        return "acesso-negado";
    }

}
