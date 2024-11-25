package com.andrey.projeto.carro;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Controller
@RequestMapping("/")
public class Controller {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;
    
    @GetMapping
    public String index(
        @RequestParam(value = "categoria", required = false) String categoria,
        @RequestParam(value = "nome", required = false) String nome,
        Model model
    ) {

        // Busca carros de acordo com a categoria e o nome fornecidos
        List<Carro> carros;
        if ((categoria == null || categoria.equals("todos")) && (nome == null || nome.isEmpty())) {
            carros = carroRepository.findAll();
        } else if (nome != null && !nome.isEmpty()) {
            carros = carroRepository.findByNomeContainingIgnoreCase(nome);
        } else {
            carros = carroRepository.findByCategoriaIgnoreCase(categoria);
        }

        model.addAttribute("carros", carros);
        model.addAttribute("categoriaSelecionada", categoria);
        model.addAttribute("nomePesquisa", nome);

        return "tela1";
    }

    @GetMapping("/detalhes/{id}")
    public String listarProdutos(@PathVariable Long id, Model model) {

        // Busca o carro pelo ID
        Optional<Carro> carroOptional = carroRepository.findById(id);

        // Verifica se o carro foi encontrado
        if (carroOptional.isPresent()) {
            Carro carro = carroOptional.get();
            model.addAttribute("carro", carro);
            List<Comentario> comentarios = comentarioRepository.findByIdCarro(id.intValue());
            model.addAttribute("comentarios", comentarios);
            return "tela2";
        } else {
            return "erro"; 
        }
    }
    
    
    @GetMapping("/adicionar")
    public String adicionarCarro() {
        return "tela3";
    }

    @PostMapping("/adicionar")
    public String salvarCarro(
        @RequestParam("car-name") String nome,
        @RequestParam("car-category") String categoria,
        @RequestParam("car-description") String descricao,
        @RequestParam("car-usage-time") int tempoUso,
        @RequestParam("car-foto") String foto, // Para o upload da foto
        @RequestParam("avaliacaoPneus") int avaliacaoPneus,
        @RequestParam("avaliacaoFreios") int avaliacaoFreios,
        @RequestParam("avaliacaoPintura") int avaliacaoPintura,
        HttpServletRequest request,
        HttpSession session,
        Model model) {

    // Verifica se o usuário está logado pela existência do usuarioId na sessão
    Long usuarioId = (Long) session.getAttribute("usuarioId");

    if (usuarioId == null) {
        // Usuário não está logado, redireciona para a tela de login com alerta
        model.addAttribute("mensagemErro", "Faça login antes");
        return "tela5";
    }

    // Cria um objeto Carro e salva no banco de dados
    Carro carro = new Carro();
    carro.setNome(nome);
    carro.setCategoria(categoria);
    carro.setDescricao(descricao);
    carro.setTempoUso(tempoUso);
    carro.setUsuarioId(usuarioId); // Associa o carro ao usuário logado
    carro.setFoto(foto);

    // Define as avaliações
    carro.setAvaliacaoPneus(avaliacaoPneus);
    carro.setAvaliacaoFreios(avaliacaoFreios);
    carro.setAvaliacaoPintura(avaliacaoPintura);

    // Salva o carro usando o serviço
    carroRepository.save(carro);

    model.addAttribute("carros", carroRepository.findAll());
    model.addAttribute("mensagem", "Carro adicionado com sucesso!");
    return "tela1";
    }


    @GetMapping("/suporte")
    public String suporte() {
        return "tela4";
    }

    @GetMapping("/login")
    public String login() {
        return "tela5";
    }

    @PostMapping("/login")
    public String validarLogin(
            @RequestParam("login") String login,
            @RequestParam("senha") String senha,
            Model model,
            HttpSession session) {

        // Buscar o usuário pelo login e senha
        Usuario usuario = usuarioRepository.findByLoginAndSenha(login, senha);

        if (usuario != null) {
            // Se o login estiver correto, salvar o ID do usuário na sessão
            session.setAttribute("usuarioId", usuario.getId());
            // Exibir mensagem de sucesso
            model.addAttribute("mensagem", "Login realizado com sucesso!");
            model.addAttribute("carros", carroRepository.findAll());
            return "tela1"; // Página de sucesso ou redirecionamento
        } else {
            // Exibir mensagem de erro
            model.addAttribute("mensagemErro", "Login ou senha incorretos.");
            return "tela5"; // Voltar para a mesma página de login
        }
    }


    @GetMapping("/cadastro")
    public String cadastro() {
        return "tela6";
    }

    @PostMapping("/cadastro")
    public String cadastrarUsuario(
            @RequestParam("nome") String nome,
            @RequestParam("login") String login,
            @RequestParam("senha") String senha,
            Model model) {

        // Criar um novo objeto Usuario
        Usuario usuario = new Usuario(nome, login, senha);
        
        // Salvar o usuário no banco de dados
        usuarioRepository.save(usuario);
        
        model.addAttribute("mensagem", "Usuário cadastrado com sucesso!");

        return "tela6"; 
    }

    @PostMapping("/detalhes/{id}/comentario")
    public String adicionarComentario(@PathVariable Long id, 
                                    @RequestParam("comentario") String comentario, 
                                    HttpSession session, Model model) {
        
        // Verifica se o usuário está logado
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            model.addAttribute("mensagemErro", "Faça login antes");
            return "tela5"; 
        }
        
        // Busca o carro pelo ID
        Optional<Carro> carroOptional = carroRepository.findById(id);
        if (carroOptional.isPresent()) {
            Carro carro = carroOptional.get();
            
            // Cria o comentário e associa ao carro e ao usuário
            Comentario novoComentario = new Comentario();
            novoComentario.setIdCarro(id);
            novoComentario.setTexto(comentario);
            
            // Busca o nome do usuário pelo ID (Assumindo que existe um UserRepository)
            Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioId);
            if (usuarioOptional.isPresent()) {
                Usuario usuario = usuarioOptional.get();
                novoComentario.setNomeUsuario(usuario.getNome()); // Assumindo que o modelo de comentário tem um campo para o nome do usuário
            }
            
            // Salva o comentário no banco de dados
            comentarioRepository.save(novoComentario);
            
            // Redireciona de volta para a página de detalhes do carro
            model.addAttribute("carro", carro);
            return "redirect:/detalhes/" + id;
        } else {
            return "erro";
        }
    }

    @GetMapping("/trocar_senha")
    public String trocar_senha() {
        return "tela7";
    }

    @PostMapping("/trocar_senha")
    public String trocar_senha_post(
            @RequestParam("login") String login,
            @RequestParam("senha") String senha,
            Model model,
            HttpSession session){

                Usuario usuario = usuarioRepository.findByLogin(login);

                if (usuario != null) {
                    usuario.setSenha(senha);
                    usuarioRepository.save(usuario); 
                    model.addAttribute("mensagem", "Senha Trocada com Sucesso!");
                }else{
                    model.addAttribute("mensagemErro", "Login não encontrado!");
                }

                return "tela7";
            }
}
