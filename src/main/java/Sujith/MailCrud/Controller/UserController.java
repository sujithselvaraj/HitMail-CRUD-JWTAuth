package Sujith.MailCrud.Controller;

import Sujith.MailCrud.Entity.ApiResponse;
import Sujith.MailCrud.Entity.User;
import Sujith.MailCrud.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/users")
@CrossOrigin(origins = "*",maxAge = 3600)
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
    public ResponseEntity<ApiResponse<?>> saveUser(@RequestBody User user)
    {

        User savedUser=userService.saveUser(user);
        ApiResponse<String> response = new ApiResponse<>("success", "User Created Successfully", "User Created Successfully as " + user.getEmail());
        return ResponseEntity.ok(response);
    }

}
