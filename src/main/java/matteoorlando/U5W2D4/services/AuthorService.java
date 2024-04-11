package matteoorlando.U5W2D4.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import matteoorlando.U5W2D4.entities.Author;
import matteoorlando.U5W2D4.exceptions.BadRequestException;
import matteoorlando.U5W2D4.exceptions.NotFoundException;
import matteoorlando.U5W2D4.payloads.NewAuthorDTO;
import matteoorlando.U5W2D4.repositories.AuthorsRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class AuthorService {

    @Autowired
    private Cloudinary cloudinaryUploader;
    private AuthorsRepository authorsRepository;

    public Page<Author> getAuthor(int page, int size, String sortBy){
        if(size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.authorsRepository.findAll(pageable);
    }

    public Author save(NewAuthorDTO body){
        // 1. Verifico se l'email è già in uso
        this.authorsRepository.findByEmail(body.email()).ifPresent(
                // 2. Se lo è triggero un errore
                user -> {
                    throw new BadRequestException("L'email " + user.getEmail() + " è già in uso!");
                }
        );

        User newUser = new Author(body.name(), body.surname(), body.email(),
                "https://ui-avatars.com/api/?name="+ body.name() + "+" + body.surname());


        Author newAuthor = new Author();
        return AuthorsRepository.save(newAuthor);
    }

    public Author findById(UUID authorId){
        return (Author) this.authorsRepository.findById(authorId).orElseThrow(() -> new NotFoundException(String.valueOf(authorId)));
    }

    public Author findByIdAndUpdate(UUID authorId, Author modifiedAuthor){
        Author found = (Author) this.findById(authorId);
        found.setName(modifiedAuthor.getName());
        found.setSurname(modifiedAuthor.getSurname());
        found.setEmail(modifiedAuthor.getEmail());
        found.setAvatarURL("https://ui-avatars.com/api/?name="+ modifiedAuthor.getName() + "+" + modifiedAuthor.getSurname());
        return AuthorsRepository.save(found);
    }

    public void findByIdAndDelete(UUID authorId){
        Author found = this.findById(authorId);
        this.authorsRepository.delete(found);
    }

    public String uploadImage(MultipartFile image) throws IOException {
        String url = (String) cloudinaryUploader.uploader().upload(image.getBytes(), ObjectUtils.emptyMap()).get("url");
        return url;
    }
}