package estudos.SpringSecurity.api.v1.controller;

import estudos.SpringSecurity.api.v1.controller.DTO.CreatePost;
import estudos.SpringSecurity.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Posts")
@RestController
@RequestMapping("/v1/posts")
public class PostController {

    @Autowired
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "Criacao de Posts", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/create")
    public ResponseEntity<Void> createPost(@RequestBody CreatePost postDto, JwtAuthenticationToken token) {
        postService.create(postDto, token);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Edicao de Posts", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<Void> editPost(@RequestBody CreatePost postDtoNew, JwtAuthenticationToken token, @PathVariable("id") Long postId) {
        postService.edit(postDtoNew, token, postId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Leitura de Posts (atualmente no console)", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<Void> readPostById(@PathVariable("id") Long postId, JwtAuthenticationToken token) {
        postService.read(postId, token);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delecao de Posts", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long postId, JwtAuthenticationToken token) {
        postService.delete(postId, token);
        return ResponseEntity.ok().build();
    }
}
