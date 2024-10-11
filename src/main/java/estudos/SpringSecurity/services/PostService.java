package estudos.SpringSecurity.services;

import estudos.SpringSecurity.api.v1.controller.DTO.CreatePost;
import estudos.SpringSecurity.entities.Post;
import estudos.SpringSecurity.entities.Role;
import estudos.SpringSecurity.repository.PostRepository;
import estudos.SpringSecurity.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Component
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public void create(@RequestBody CreatePost postDto, JwtAuthenticationToken token) {
        var user = userRepository.findById(UUID.fromString(token.getName()));
        if(user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado");
        var post = new Post();
        post.setUser(user.get());
        post.setConteudo(postDto.conteudo());
        postRepository.save(post);
    }

    public void edit(@RequestBody CreatePost postDtoNew, JwtAuthenticationToken token, @PathVariable("id") Long postId) {

        var post = postRepository.findById(postId);
        if(post.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post nao encontrado");
        var user = userRepository.findById(UUID.fromString(token.getName()));
        if(user.isEmpty() || !user.get().getUsername().equals(post.get().getUser().getUsername()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User nao existente ou diferente do criador do post");

        post.get().setConteudo(postDtoNew.conteudo());

        postRepository.save(post.get());
    }

    public void read(@PathVariable("id") Long postId, JwtAuthenticationToken token) {
        var post = postRepository.findById(postId);
        if(post.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post nao encontrado");
        var user = userRepository.findById(UUID.fromString(token.getName()));
        if(user.isEmpty())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Voce precisa ser um usuario para ler qualquer post");
        System.out.println("Post de: " + post.get().getUser().getUsername());
        System.out.println("Conteudo: " + post.get().getConteudo());
    }

    public void delete(@PathVariable("id") Long postId, JwtAuthenticationToken token) {
        var user = userRepository.findById(UUID.fromString(token.getName()));
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post nao encontrado"));

        var isAdmin = user.orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario nao encontrado"))
                .getRoles()
                .stream()
                .anyMatch(role -> role.getRoleName().equalsIgnoreCase(Role.Values.ADMIN.name()));

        if(isAdmin || post.getUser().getId().equals(UUID.fromString(token.getName()))) {
            postRepository.deleteById(postId);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Post nao encontrado");
        }
    }
}
