package estudos.SpringSecurity.repository;

import estudos.SpringSecurity.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username); //query feita automaticamente, via JPA
    //optional usado visto que, a funcao pode ou nao retornar algo, onde nesses casos se torna necessario o tratamento de nao retorno
}
