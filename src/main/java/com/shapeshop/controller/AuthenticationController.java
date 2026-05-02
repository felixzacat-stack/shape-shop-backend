package com.shapeshop.controller;

import com.shapeshop.model.AuthenticationRequest;
import com.shapeshop.model.AuthenticationResponse;
import com.shapeshop.service.UserService;
import com.shapeshop.security.JwtUtil;
import com.shapeshop.security.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Login/Logout endpoints.
 */
@CrossOrigin
@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private UserService userDetailsService;

    @Autowired
    private PasswordUtils passwordValidationService;

    /**
     * Authenticate a user who is trying to log in.
     *
     * @param authenticationRequest authentication request when logging in
     * @return HTTP response entity
     */
//    @CrossOrigin
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> loginAndCreateAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {

        try {

            String pswd = authenticationRequest.getPassword();
            String uName = authenticationRequest.getUsername();

            // DON'T hash the password here - let Spring Security handle it with your custom encoder
            // String encryptedPswd = passwordValidationService.encryptPassword(pswd);

            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(uName, pswd)); // Use raw password

            String jwtToken = jwtTokenUtil.createToken(authentication);

            return ResponseEntity.ok(new AuthenticationResponse(jwtToken));

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error attempting to log in ", HttpStatus.UNAUTHORIZED);
        }
    }

    //TODO
//    @CrossOrigin
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseEntity<?> logout(@RequestBody AuthenticationRequest authenticationRequest)
            throws Exception {

//		Authentication authentication = null;
//
//		String pswd = authenticationRequest.getPassword();
//		String uName = authenticationRequest.getUsername();
//		String encryptedPswd = passwordValidationService.encryptPassword(pswd);
//
//		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
//
//		authentication = authenticationManager
//				.authenticate(new UsernamePasswordAuthenticationToken(uName, encryptedPswd, userDetails.getAuthorities()));
//
//		final String jwtToken = jwtTokenUtil.createToken(authentication);
//
        return ResponseEntity.ok(new AuthenticationResponse("TODO this isnt working correctly"));
    }
}
