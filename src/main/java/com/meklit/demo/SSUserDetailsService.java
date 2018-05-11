package com.meklit.demo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
public class SSUserDetailsService implements UserDetailsService
{

    private PasswordEncoder encoder;
    private UserRepository userRepository;


    public SSUserDetailsService(UserRepository userRepository){
        this.userRepository=userRepository;
        encoder = new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        try {

            AppUser user = userRepository.findByUsername(username);
            if (user == null) {
                return null;
            }
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    encoder.encode(user.getPassword()),
                    getAuthorities(user));
        } catch (Exception e){
            throw new UsernameNotFoundException("User not found");
        }
    }
    private Set<GrantedAuthority> getAuthorities(AppUser user){
        Set<GrantedAuthority> authorities
                = new HashSet<GrantedAuthority>();
        for(Role role : user.getRoles()){
            GrantedAuthority grantedAuthority
                    = new SimpleGrantedAuthority(role.getRole());
            authorities.add(grantedAuthority);
        }
        return authorities;
    }

}