package ait.cohort5860.accounting.controller;

import ait.cohort5860.accounting.dto.*;
import ait.cohort5860.accounting.service.UserAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class UserAccountController {

    private final UserAccountService userAccountService;

    // @Valid - if conditions in RegistrationDto for fields are not satisfied, 404 will be returned
    @PostMapping("/register")
    public UserDto register(@RequestBody @Valid RegistrationDto registrationDto) {
        return userAccountService.register(registrationDto);
    }

    @PostMapping("/login")
    public UserDto login(Principal principal) {
        return userAccountService.getUser(principal.getName());
    }

    @DeleteMapping("/user/{login}")
    public UserDto deleteUser(@PathVariable String login) {
        return userAccountService.deleteUser(login);
    }

    @PatchMapping("/user/{login}")
    public UserDto updateUser(@PathVariable String login, @RequestBody @Valid UserUpdateDto userUpdateDto) {
        return userAccountService.updateUser(login, userUpdateDto);
    }

    @PatchMapping("/user/{login}/role/{role}")
    public RoleDto addRole(@PathVariable String login, @PathVariable String role) {
        return userAccountService.changeRolesList(login, role, true);
    }

    @DeleteMapping("/user/{login}/role/{role}")
    public RoleDto deleteRole(@PathVariable String login, @PathVariable String role) {
        return userAccountService.changeRolesList(login, role, false);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/password")
    public void changePassword(Principal principal, @RequestHeader("X-Password") String newPassword) {
        userAccountService.changePassword(principal.getName(), newPassword);
    }

    @GetMapping("/user/{login}")
    public UserDto getUser(@PathVariable String login) {
        return userAccountService.getUser(login);
    }

    @PostMapping("/email")
    public void sendEmail(@RequestBody @Valid EmailDto emailDto) {
        userAccountService.sendEmail(emailDto);
    }
}
