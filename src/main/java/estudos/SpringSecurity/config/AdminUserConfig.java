package estudos.SpringSecurity.config;

import estudos.SpringSecurity.entities.Role;
import estudos.SpringSecurity.entities.User;
import estudos.SpringSecurity.repository.RoleRepository;
import estudos.SpringSecurity.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfig(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        var roleAdmin = roleRepository.findByRoleName(Role.Values.ADMIN.name());
        var userAdmin = userRepository.findByUsername("admin");

        if(roleAdmin == null) {
            Role role = new Role();
            role.setRoleName(Role.Values.ADMIN.name());
            roleRepository.save(role);
            roleAdmin = roleRepository.findByRoleName(Role.Values.ADMIN.name());
        }

        final var finalRoleAdmin = roleAdmin;

        userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("admin ja existe");
                },
                () -> {
                    var user = new User();
                    user.setUsername("admin");
                    user.setPassword(passwordEncoder.encode("adminnn"));
                    user.setRoles(Set.of(finalRoleAdmin));
                    userRepository.save(user);
                }
        );
    }
}
