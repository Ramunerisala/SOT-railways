package com.project.pod.websecurity;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService{
    
	private WebRepository webRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=webRepository.findByUsername(username);
		if(user==null) {throw new UsernameNotFoundException("User Not Found");}
		return new CustomUserDetails(user);
	}

}
