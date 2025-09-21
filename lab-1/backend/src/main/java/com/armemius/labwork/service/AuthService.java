package com.armemius.labwork.service;

import com.armemius.labwork.data.domain.AppUser;
import com.armemius.labwork.data.domain.SystemUserRole;
import com.armemius.labwork.data.dto.CredentialsDto;
import com.armemius.labwork.data.dto.JwtResponseDto;
import com.armemius.labwork.data.repository.AppUserRepository;
import com.armemius.labwork.exceptions.auth.UsernameTakenException;
import com.armemius.labwork.security.JwtProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authManager;
    private final JwtProvider jwtProvider;
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private SystemUserRole determineDefaultRole() {
        if (userRepository.count() == 0) {
            return SystemUserRole.ROLE_ADMIN;
        }
        return SystemUserRole.ROLE_USER;
    }

    private AppUser createUser(String username, String password) throws UsernameTakenException {
        if (userRepository.findFirstByUsername(username).isPresent()) {
            throw new UsernameTakenException("Username is already in use");
        }

        AppUser user = new AppUser(
                null,
                username,
                passwordEncoder.encode(password),
                determineDefaultRole()
        );
        return userRepository.save(user);
    }

    private Authentication getUserAuthentication(String username, String password) {
        return authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );
    }

    private String getTokenForUser(String username, String password) {
        Authentication auth = getUserAuthentication(username, password);
        return  "Bearer " + jwtProvider.generateToken(auth.getName());
    }

    @Transactional
    public JwtResponseDto registerUser(@Valid CredentialsDto credentials) {
        AppUser user = createUser(
                credentials.getUsername(),
                credentials.getPassword()
        );

        return new JwtResponseDto(
                user.getRole(),
                user.getUsername(),
                getTokenForUser(credentials.getUsername(), credentials.getPassword())
        );
    }

    @Transactional(readOnly = true)
    public JwtResponseDto loginUser(@Valid CredentialsDto credentials) {
        String username = credentials.getUsername();
        AppUser user = userRepository.findFirstByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username: " + username)
                );
        return new JwtResponseDto(
                user.getRole(),
                user.getUsername(),
                getTokenForUser(credentials.getUsername(), credentials.getPassword())
        );
    }
}
