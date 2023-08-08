package Sujith.MailCrud.Controller;

import Sujith.MailCrud.Entity.User;
import Sujith.MailCrud.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/users")
public class UserController
{
    private UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers()
    {
        List<User> users=userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user)
    {
        User savedUser=userService.saveUser(user);
        return new ResponseEntity<>(savedUser,HttpStatus.OK);
    }

}
