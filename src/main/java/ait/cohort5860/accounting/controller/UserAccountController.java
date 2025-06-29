package ait.cohort5860.accounting.controller;

import ait.cohort5860.accounting.dto.RegistrationDto;
import ait.cohort5860.accounting.dto.RoleDto;
import ait.cohort5860.accounting.dto.UserDto;
import ait.cohort5860.accounting.dto.UserUpdateDto;
import ait.cohort5860.accounting.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class UserAccountController {

    private final UserAccountService userAccountService;

    @ResponseStatus(HttpStatus.CONFLICT)
    @PostMapping("/register")
    public UserDto register(@RequestBody RegistrationDto registrationDto) {
        return userAccountService.register(registrationDto);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @PostMapping("/login")
    public UserDto login() {
        return userAccountService.login();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    //@ResponseStatus(HttpStatus.FORBIDDEN)
    @DeleteMapping("/user/{user}")
    public UserDto deleteUser(@PathVariable("user") String login,
                              @RequestHeader("Authorization") String value) {
        return userAccountService.deleteUser(login);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    //@ResponseStatus(HttpStatus.FORBIDDEN)
    @PatchMapping("/user/{user}")
    public UserDto updateUser(@PathVariable("user") String login, @RequestBody UserUpdateDto userUpdateDto) {
        return userAccountService.updateUser(login, userUpdateDto);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    //@ResponseStatus(HttpStatus.FORBIDDEN)
    @PatchMapping("/user/{user}/role/{role}")
    public RoleDto addRole(@PathVariable("user") String login, @PathVariable String role) {
        return userAccountService.addRole(login, role);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    //@ResponseStatus(HttpStatus.FORBIDDEN)
    @DeleteMapping("/user/{user}/role/{role}")
    public RoleDto deleteRole(@PathVariable("user") String login, @PathVariable String role) {
        return userAccountService.deleteRole(login, role);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/password")
    public void changePassword() {
        userAccountService.changePassword();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @GetMapping("/user/{user}")
    public UserDto getUser(@PathVariable("user") String login) {
        return userAccountService.getUser(login);
    }
}
