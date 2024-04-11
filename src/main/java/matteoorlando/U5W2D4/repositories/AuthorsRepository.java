package matteoorlando.U5W2D4.repositories;

import matteoorlando.U5W2D4.entities.Author;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthorsRepository extends JpaRepository<Author, Integer> {


    static Author save(Author newAuthor) {
        return newAuthor;
    }
    Optional<Author> findByEmail(String email);

    Optional<Object> findById(UUID authorId);
}
