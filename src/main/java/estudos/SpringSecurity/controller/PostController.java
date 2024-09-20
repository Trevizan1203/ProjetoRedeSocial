package estudos.SpringSecurity.controller;

import estudos.SpringSecurity.controller.DTO.CreatePost;
import estudos.SpringSecurity.entities.Post;
import estudos.SpringSecurity.entities.Role;
import estudos.SpringSecurity.repository.PostRepository;
import estudos.SpringSecurity.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostController(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createPost(@RequestBody CreatePost postDto, JwtAuthenticationToken token) {

        var user = userRepository.findById(UUID.fromString(token.getName()));
        if(user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        var post = new Post();
        post.setUser(user.get());
        post.setConteudo(postDto.conteudo());
        postRepository.save(post);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<Void> editPost(@RequestBody CreatePost postDtoNew, JwtAuthenticationToken token, @PathVariable("id") Long postId) {

        var post = postRepository.findById(postId);
        if(post.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post nao encontrado");
        var user = userRepository.findById(UUID.fromString(token.getName()));
        if(user.isEmpty() || !user.get().getUsername().equals(post.get().getUser().getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User nao existente ou diferente do criador do post");

        Post editedPost = new Post();
        editedPost.setPostId(postId);
        editedPost.setConteudo(postDtoNew.conteudo());
        editedPost.setUser(user.get());

        postRepository.deleteById(postId);
        postRepository.save(editedPost);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> readPostById(@PathVariable("id") Long postId, JwtAuthenticationToken token) {

        var post = postRepository.findById(postId);
        if(post.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post nao encontrado");
        var user = userRepository.findById(UUID.fromString(token.getName()));
        if(user.isEmpty())
            throw new ResponseStatusException(HttpStatus.LOCKED, "Voce precisa ser um usuario para ler qualquer post");
        System.out.println("Post de: " + user.get().getUsername());
        System.out.println("Conteudo: " + post.get().getConteudo());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long postId, JwtAuthenticationToken token) {

        var user = userRepository.findById(UUID.fromString(token.getName()));
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var isAdmin = user.get().getRoles()
                .stream()
                .anyMatch(role -> role.getRoleName().equalsIgnoreCase(Role.Values.ADMIN.name()));

        if(isAdmin || post.getUser().getId().equals(UUID.fromString(token.getName()))) {
            postRepository.deleteById(postId);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok().build();
    }
}
