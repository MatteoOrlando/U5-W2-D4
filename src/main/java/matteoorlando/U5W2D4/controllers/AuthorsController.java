package matteoorlando.U5W2D4.controllers;

import matteoorlando.U5W2D4.entities.Author;
import matteoorlando.U5W2D4.exceptions.BadRequestException;
import matteoorlando.U5W2D4.payloads.NewAuthorDTO;
import matteoorlando.U5W2D4.payloads.NewAuthorRespDTO;
import matteoorlando.U5W2D4.services.AuthorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authors")
public class AuthorsController {
    @Autowired
    AuthorsService authorsService;

    // 1. - POST http://localhost:3001/authors (+ req.body)
   /* @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED) // <-- 201
    public Author saveAuthor(@RequestBody Author body) throws Exception {
        return authorsService.save(body);
    }*/

    // 2. - GET http://localhost:3001/authors
    @GetMapping("")
    public Page<Author> getAuthors(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy) {
        return authorsService.getAuthors(page, size, sortBy);
    }

    //AGGIUNGO VALIDAZIONE = @Validated valida il payload in base ai validatori impiegati nella classe NewAuthorDTO (U5D9)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewAuthorRespDTO saveAuthor(@RequestBody @Validated NewAuthorDTO payload, BindingResult validation){
        if(validation.hasErrors()){ throw new BadRequestException(validation.getAllErrors().toString());
        }
        return new NewAuthorDTO(this.authorService.save(payload).getId());
    }


    // 3. - GET http://localhost:3001/authors/{id}
    @GetMapping("/{authorId}")
    public Author findById(@PathVariable int authorId){
        return authorsService.findById(authorId);
    }

    // 4. - PUT http://localhost:3001/authors/{id} (+ req.body)
    @PutMapping("/{authorId}")
    public Author findAndUpdate(@PathVariable int authorId, @RequestBody Author body){
        return authorsService.findByIdAndUpdate(authorId, body);
    }

    // 5. - DELETE http://localhost:3001/authors/{id}
    @DeleteMapping("/{authorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // <-- 204 NO CONTENT
    public void findAndDelete(@PathVariable int authorId) {
        authorsService.findByIdAndDelete(authorId);
    }


}
