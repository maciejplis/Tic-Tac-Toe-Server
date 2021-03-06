package matthias.tictactoe.web.authentication.controllers;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.web.authentication.model.Role;
import matthias.tictactoe.web.authentication.model.User;
import matthias.tictactoe.web.authentication.model.dtos.UserCredentials;
import matthias.tictactoe.web.authentication.model.dtos.UserRegistration;
import matthias.tictactoe.web.authentication.services.GuestGenerator;
import matthias.tictactoe.web.authentication.services.UserService;
import matthias.tictactoe.web.authentication.utils.JWTUtils;
import matthias.tictactoe.web.authentication.utils.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "${client.url}")
@RequiredArgsConstructor
public class AuthenticationController {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserService userService;
    private final GuestGenerator guestGenerator;

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody UserRegistration userRegistration, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new Object() {
                        public String message = "Please correct the errors and try again";
                        public Map<String, String> fieldErrors = fieldErrorsToMap(bindingResult.getFieldErrors());
                    });
        }

        User user = UserMapper.mapToUserWithRoles(userRegistration, Role.USER);
        user.setPassword( passwordEncoder.encode(user.getPassword()) );
        userService.saveUser(user);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody UserCredentials userCredentials, HttpServletResponse response) {

        User user = userService.findUserByUsername(userCredentials.getUsername());

        if(user == null || !passwordEncoder.matches(userCredentials.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Object() {
                        public String message = "Incorrect username or password";
                    });
        }

        Object userAuth = JWTUtils.generateJWTForUser(user);

        return ResponseEntity.ok().body(userAuth);
    }

    @GetMapping("/login-guest")
    public ResponseEntity<Object> loginAsGuest() {
        User guestUser = guestGenerator.generateGuestUser();

        if(guestUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Object() {
                        public String message = "Couldn't authorize guest account";
                    });
        }

        Object userAuth = JWTUtils.generateJWTForUser(guestUser);

        return ResponseEntity.ok().body(userAuth);
    }

    @GetMapping("/")
    public boolean isOnline() {return true;}

    private Map<String, String> fieldErrorsToMap(List<FieldError> fieldErrors) {
        return fieldErrors
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }

}
