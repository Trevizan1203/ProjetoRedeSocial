package estudos.SpringSecurity;

import estudos.SpringSecurity.entities.Role;
import estudos.SpringSecurity.entities.User;
import estudos.SpringSecurity.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional //garante que, apos o teste, o que foi inserido no bd seja revertido
public class AuthTest {

    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();

        userRepository.deleteAll();
        userRepository.flush();

        User user = new User();
        user.setUsername("gustavo");

        Role role = new Role();
        role.setRoleName("BASIC");

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode("123456"));

        userRepository.save(user);
        userRepository.flush();
    }

    @Test
    public void testeErroCredenciaisInvalidasBancoDados() throws Exception {
        //URI e uma estrutura para armazenamento de urls
        URI uri = new URI("/auth/login");

        String conteudo = "{\"username\" : \"gustavo\", \"password\" : \"123456\"}";

        //o teste feito pelo mock de fato
        mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .content(conteudo)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());
    }

}
