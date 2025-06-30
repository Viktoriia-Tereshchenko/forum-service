package ait.cohort5860.accounting.service;

import ait.cohort5860.accounting.dto.RegistrationDto;
import ait.cohort5860.accounting.dto.RoleDto;
import ait.cohort5860.accounting.dto.UserDto;
import ait.cohort5860.accounting.dto.UserUpdateDto;

public interface UserAccountService {
    UserDto register(RegistrationDto registrationDto);

    UserDto login();

    UserDto deleteUser(String login);

    UserDto updateUser(String login, UserUpdateDto userUpdateDto);

    UserDto getUser(String login);

    RoleDto changeRolesList(String login, String role, boolean isAddRole);

    void changePassword(String login, String newPassword);

}
