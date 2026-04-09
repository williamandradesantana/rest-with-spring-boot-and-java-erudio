package br.com.AuthenticationJWTAndSpringSecurity.services;

import br.com.AuthenticationJWTAndSpringSecurity.data.dto.security.AccountCredentialsDTO;
import br.com.AuthenticationJWTAndSpringSecurity.data.dto.security.TokenDTO;
import br.com.AuthenticationJWTAndSpringSecurity.repository.UserRepository;
import br.com.AuthenticationJWTAndSpringSecurity.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthServices {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private UserRepository repository;

    public ResponseEntity<TokenDTO> sign(AccountCredentialsDTO credentials) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword())
        );
        var user = repository.findByUsername(credentials.getUsername());
        if (user == null)
            throw new UsernameNotFoundException("Username " + credentials.getUsername() + " not found!");

        var token = tokenProvider.createAccessToken(credentials.getUsername(), user.getRoles());
        return ResponseEntity.ok(token);
    }

    public ResponseEntity<TokenDTO> refreshToken(String username, String refreshToken) {
        var user = repository.findByUsername(username);
        TokenDTO token;
        if (user != null) {
            token = tokenProvider.refreshToken(refreshToken);
        } else {
            throw new UsernameNotFoundException("Username " + username + " not found!");
        }
        return ResponseEntity.ok(token);
    }
}
