package estudos.SpringSecurity.api.v1.controller;

import estudos.SpringSecurity.api.v1.controller.DTO.UserDTO;
import estudos.SpringSecurity.entities.User;
import estudos.SpringSecurity.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User")
@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Criacao de Usuario")
    @PostMapping("/create")
    @Transactional
    public ResponseEntity<Void> createUser(@RequestBody UserDTO user) {

        userService.createUser(user);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Listagem de Usuarios", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        var users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Delecao de usuario", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/delete")
    @Transactional
    public ResponseEntity<Void> deleteUser(@RequestBody UserDTO user, JwtAuthenticationToken token) {
        userService.deleteUser(token, user);
        return ResponseEntity.ok().build();
    }

}
