package ait.cohort5860.security;

import ait.cohort5860.accounting.dao.UserAccountRepository;
import ait.cohort5860.accounting.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

//Realization of AUTHENTICATION

@Service // now Spring Security will NOT create its user + password
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserAccountRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // find username (our login) in the database
        // find user (UserAccount)
        // create and return UserDetails with all user data for verification


        // all roles in Spring Security must be named with ROLE_ (ROLE_USER, ROLE_ADMIN)
        UserAccount userAccount = repository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username));
        Collection<String> roles = userAccount.getRoles()
                .stream()
                .map(r -> "ROLE_" + r.name())
                .toList();
        return new User(username, userAccount.getPassword(), AuthorityUtils.createAuthorityList(roles));
        // "{noop}" + userAccount.getPassword() =>
        // {noop} -  no operation = no need to de-hash the password
        // this is NOT to be used! passwords should be stored in encrypted form
    }
}
