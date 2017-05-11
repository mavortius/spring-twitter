package br.com.mavortius.twitter.web.api;

import br.com.mavortius.twitter.domain.User;
import br.com.mavortius.twitter.error.EntityNotFoundException;
import br.com.mavortius.twitter.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserApiController {

    private UserRepository repository;

    public UserApiController(UserRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> findAll() {
        return repository.findAll();
    }

    @RequestMapping(value = "/user/{email}", method = RequestMethod.GET)
    public User findOne(@PathVariable String email) throws EntityNotFoundException {
        return repository.findOne(email);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<User> create(@RequestBody User user) {
        HttpStatus status = HttpStatus.OK;

        if (!repository.exists(user.getEmail())) {
            status = HttpStatus.CREATED;
        }

        User saved = repository.save(user);

        return new ResponseEntity<>(saved, status);
    }

    @RequestMapping(value = "/user/{email}", method = RequestMethod.PUT)
    public ResponseEntity<User> update(@PathVariable String email, @RequestBody User user) throws EntityNotFoundException {
        User saved = repository.save(email, user);

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/user/{email}", method = RequestMethod.DELETE)
    public ResponseEntity<User> delete(@PathVariable String email) throws EntityNotFoundException {
        repository.delete(email);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
