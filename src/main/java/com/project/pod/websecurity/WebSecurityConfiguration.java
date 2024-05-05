package com.project.pod.websecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter{
    
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(new BCryptPasswordEncoder());
		return provider;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http.authorizeRequests()
		    .antMatchers("/")
		    .permitAll()
		    .antMatchers("/home")
		    .permitAll()
		    .antMatchers("/login")
		    .permitAll()
		    .antMatchers("/register")
		    .permitAll()
		    .antMatchers("/verify")
		    .permitAll()
		    .antMatchers("/forgot password")
		    .permitAll()
		    .antMatchers("/verifyotp")
		    .permitAll()
		    .antMatchers("/admin")
		    .permitAll()
		    .antMatchers("/upload")
		    .permitAll()
		    .antMatchers("/TrainsSchedule")
		    .permitAll()
		    .antMatchers("/addTrain")
		    .permitAll()
		    .antMatchers("/showUpdateForm")
		    .permitAll()
		    .antMatchers("/DeleteRow")
		    .permitAll()
		    .antMatchers("/bookTicket")
		    .permitAll()
		    .antMatchers("/DeleteTrains")
		    .permitAll()
		    .antMatchers("/bookNow")
		    .permitAll()
		    .antMatchers("/edit")
		    .permitAll()
		    .antMatchers("/tickets")
		    .permitAll()
		    .antMatchers("/cancel")
		    .permitAll()
		    .antMatchers("/checkout")
		    .permitAll()
		    .antMatchers("/cash")
		    .permitAll()
		    .antMatchers("/history")
		    .permitAll()
		    .antMatchers("/cancelTicket")
		    .permitAll()
		    .antMatchers("/cancelit")
		    .permitAll()
		    .antMatchers("/upcoming")
		    .permitAll()
		    .antMatchers("/card")
		    .permitAll()
		    .antMatchers("/editProfile")
		    .permitAll()
		    .antMatchers("/savenew")
		    .permitAll()
		    .antMatchers("/logout")
		    .permitAll()
		    .antMatchers("/adminlogout")
		    .permitAll()
		    .antMatchers("/adminprofile")
		    .permitAll()
		    .antMatchers("/contactus")
		    .permitAll()
		    .antMatchers("/traingraphs")
		    .permitAll()
		    .antMatchers("/paymentgraph")
		    .permitAll()
		    .antMatchers("/passenger")
		    .permitAll()
		    .antMatchers("/modify")
		    .permitAll()
		    .antMatchers("/modifyticket")
		    .permitAll()
		    .antMatchers("/guest"
		    		+ "")
		    .permitAll()
		    .anyRequest()
		    .authenticated()
		    .and()
		    .httpBasic();
		
	}
	
	
	
	
	
	
	
}
