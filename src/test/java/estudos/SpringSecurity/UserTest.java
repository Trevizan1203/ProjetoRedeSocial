package estudos.SpringSecurity;

import estudos.SpringSecurity.controller.DTO.LoginRequest;
import estudos.SpringSecurity.entities.Role;
import estudos.SpringSecurity.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {

    private PasswordEncoder passwordEncoder;

    @Test
    public void testeCriacaoUser() {
        User user = new User();
        UUID id = UUID.randomUUID();
        user.setId(id);
        user.setUsername("usuarioTeste");
        user.setPassword("senhaTeste");

        assertEquals(id, user.getId());
        assertEquals("usuarioTeste", user.getUsername());
        assertEquals("senhaTeste", user.getPassword());
    }

    @Test
    public void testeAtribuicaoRolesUsuario() {
        User user = new User();
        Role role1 = new Role();
        Role role2 = new Role();

        role1.setRoleName("ROLE_USER");
        role2.setRoleName("ROLE_ADMIN");

        Set<Role> roles = new HashSet<>();
        roles.add(role1);
        roles.add(role2);

        user.setRoles(roles);

        assertEquals(2, user.getRoles().size());
        assertTrue(user.getRoles().contains(role1));
        assertTrue(user.getRoles().contains(role2));
    }

    @BeforeEach
    public void setCrypt() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    public void testeAutenticacaoCorretaUser() {
        User user = new User();
        user.setUsername("usuarioTeste");
        //cria usuario

        String senhaCriptografada = passwordEncoder.encode("senhaTeste");
        //criptografa a senha e atribui ao usuario ja criptografada
        user.setPassword(senhaCriptografada);

        LoginRequest loginRequest = new LoginRequest("usuarioTeste", "senhaTeste");
        //cria um login request com o usuario e senha raw
        //testa o login request com o user ja criado e verifica se a criptografia dos dados funciona corretamente
        assertTrue(user.isLoginCorrect(loginRequest, passwordEncoder));
    }
}
