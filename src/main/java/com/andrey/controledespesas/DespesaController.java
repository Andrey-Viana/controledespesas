package com.andrey.controledespesas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/despesas")
public class DespesaController {

    @Autowired
    private DespesaRepository despesaRepository;

    // Listar todas as despesas
    @GetMapping
    public String listarDespesas(Model model) {
        model.addAttribute("despesas", despesaRepository.findAll());
        return "listar"; // HTML: listar.html
    }

    // Mostrar formulário para nova despesa
    @GetMapping("/nova")
    public String mostrarFormulario(Model model) {
        model.addAttribute("despesa", new Despesa());
        return "cadastro"; // HTML: cadastro.html
    }

    // Salvar nova despesa no banco de dados
    @PostMapping
    public String salvarDespesa(@ModelAttribute Despesa despesa) {
        despesaRepository.save(despesa);
        return "redirect:/despesas";
    }

    // Redirecionar /cadastro para o formulário correto
    @GetMapping("/cadastro")
    public String redirecionarCadastro() {
        return "redirect:/despesas/nova";
    }

    // ========= NOVAS FUNÇÕES =========

    // Editar (GET): Mostrar formulário com dados preenchidos
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model) {
        Despesa despesa = despesaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("despesa", despesa);
        return "editar"; // HTML: editar.html
    }

    // Editar (POST): Atualizar os dados da despesa
    @PostMapping("/editar/{id}")
    public String atualizarDespesa(@PathVariable Long id, @ModelAttribute Despesa novaDespesa) {
        Despesa despesaExistente = despesaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));

        despesaExistente.setDescricao(novaDespesa.getDescricao());
        despesaExistente.setCategoria(novaDespesa.getCategoria());
        despesaExistente.setValor(novaDespesa.getValor());
        despesaExistente.setDataVencimento(novaDespesa.getDataVencimento());
        despesaExistente.setStatus(novaDespesa.getStatus());

        despesaRepository.save(despesaExistente);
        return "redirect:/despesas";
    }

    // Excluir (GET)
    @GetMapping("/excluir/{id}")
    public String excluirDespesa(@PathVariable Long id) {
        despesaRepository.deleteById(id);
        return "redirect:/despesas";
    }
}
