package br.com.mavortius.twitter.web.api;

import br.com.mavortius.twitter.domain.User;
import br.com.mavortius.twitter.repository.UserRepository;
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

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public User create(@RequestBody User user) {
        return repository.save(user);
    }

    @RequestMapping(value = "/user/{email}", method = RequestMethod.PUT)
    public User update(@PathVariable String email, @RequestBody User user) {
        return repository.save(email, user);
    }

    @RequestMapping(value = "/user/{email}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String email) {
        repository.delete(email);
    }
}
