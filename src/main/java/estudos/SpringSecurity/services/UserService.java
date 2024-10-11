package estudos.SpringSecurity.services;

import estudos.SpringSecurity.api.v1.controller.DTO.CreateUser;
import estudos.SpringSecurity.entities.Role;
import estudos.SpringSecurity.entities.User;
import estudos.SpringSecurity.repository.RoleRepository;
import estudos.SpringSecurity.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Component
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(@RequestBody CreateUser user) {
        var basicRole = roleRepository.findByRoleName(Role.Values.BASIC.name());

        var userDb = userRepository.findByUsername(user.username());
        //verificacao da existencia
        if(userDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Usuario ja existente"); // exceção de erro de negócio
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
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
