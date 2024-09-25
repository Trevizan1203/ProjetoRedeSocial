package estudos.SpringSecurity.api.v1.controller;

import estudos.SpringSecurity.api.v1.controller.DTO.CreateUser;
import estudos.SpringSecurity.entities.Role;
import estudos.SpringSecurity.entities.User;
import estudos.SpringSecurity.repository.RoleRepository;
import estudos.SpringSecurity.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<Void> createUser(@RequestBody CreateUser user) {

        var basicRole = roleRepository.findByRoleName(Role.Values.BASIC.name());

        var userDb = userRepository.findByUsername(user.username());
        //verificacao da existencia
        if(userDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY); // exceção de erro de negócio
        }

        if(basicRole == null) {
            Role role = new Role();
            role.setRoleName(Role.Values.BASIC.name());
            roleRepository.save(role);
            basicRole = roleRepository.findByRoleName(Role.Values.BASIC.name());
        }

        final var finalRole = basicRole;

        //criacao de novo user e upar para db
        User newUser = new User();
        newUser.setUsername(user.username());
        newUser.setPassword(passwordEncoder.encode(user.password()));
        newUser.setRoles(Set.of(finalRole));

        userRepository.save(newUser);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        var users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
}
