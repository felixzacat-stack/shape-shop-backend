//package com.shapeshop;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import com.shapeshop.security.JwtRequestFilter;
//
//
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableWebSecurity
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//	@Autowired
//	private JwtRequestFilter jwtRequestFilter;
//
//	@Override
//	@Bean
//	public AuthenticationManager authenticationManagerBean() throws Exception {
//		return super.authenticationManagerBean();
//	}
//
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.csrf().disable();
//
////		TODO put CORS back. Was not working for login for admin
//
////		http.csrf().disable().authorizeRequests()
////				.antMatchers("/").permitAll()
////				.antMatchers("/products").permitAll()
////				.antMatchers("/test").permitAll()
////				.antMatchers("/*/products").permitAll()
////				.antMatchers("/orders").permitAll()
////				.antMatchers("/h2-console/**").permitAll()
////				.antMatchers("/*/orders").permitAll()
////				.antMatchers("/authenticate").permitAll()
////				.anyRequest().authenticated()
////			.and().exceptionHandling()
////			//to get h2 console running need these two lines (http://localhost:8080/h2) ...but get cross origin problems
//////			.and().csrf().ignoringAntMatchers("/h2/**") // Make H2-Console non-secured; for debug purposes
//////			.and().headers().frameOptions().sameOrigin()
////			.and().sessionManagement()
////				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//		http.headers().frameOptions().disable();
//
//		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//	}
//
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return NoOpPasswordEncoder.getInstance();
//	}
//
//}
