package com.Dimes.Controllers;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.Dimes.Models.JwtRequest;
import com.Dimes.Models.JwtResponse;
import com.Dimes.Models.Lender;
import com.Dimes.Models.LoginViewModel;
import com.Dimes.Services.AuthService;
import com.Dimes.Services.UserDetailsService;
import com.Dimes.security.CurrentUser;
import com.Dimes.security.JwtTokenUtil;
import com.Dimes.security.MyUserPrincpal;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class AuthController {

    Gson gson = new Gson();

    private AuthService authService;

    private AuthenticationManager authenticationManager;

    private JwtTokenUtil jwtTokenUtil;

    private UserDetailsService jwtInMemoryUserDetailsService;

    @Autowired
    public AuthController(AuthService authService, AuthenticationManager authenticationManager,
            JwtTokenUtil jwtTokenUtil, UserDetailsService jwtInMemoryUserDetailsServiee) {

        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtInMemoryUserDetailsService = jwtInMemoryUserDetailsServiee;
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody JwtRequest authenticationRequest)
            throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = jwtInMemoryUserDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) throws Exception {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> saveLender(@RequestBody Lender lender) {
        if (!authService.isEmailValid(lender.getEmail())) {
            return new ResponseEntity<>(gson.toJson("Invalid email"), HttpStatus.BAD_REQUEST);
        }
        if (authService.checkIfLenderExists(lender.getUsername())) {
            return new ResponseEntity<>(gson.toJson("Username Exists"), HttpStatus.CONFLICT);
        }
        if (authService.RegisterLender(lender)) {
            return ResponseEntity.ok(gson.toJson("Registered successfully"));
        } else {
            return new ResponseEntity<>(gson.toJson("An error occurred while registering"),
                    HttpStatus.EXPECTATION_FAILED);
        }

    }

    @GetMapping("/getUserIdDetails")
    public LoginViewModel getUserIdDetails(HttpServletRequest request, Principal principal) {
        if (request.isUserInRole("Admin")) {
            return new LoginViewModel(authService.getUserFirstName(principal.getName()),
                    authService.getUserId(principal.getName()), "Admin");
        }

        // Future use
        // If app register user of with ROLE_User
        return new LoginViewModel(authService.getUserFirstName(principal.getName()),
                authService.getUserId(principal.getName()), "User");
    }

    @GetMapping("/getAllLenderDetails")
    public List<Lender> getAllLenderDetails() {
        return authService.getAllLenderDetails();
    }

    // lebon added this
    @GetMapping("/LenderInfo")
    public Lender getCurrentUser(@CurrentUser MyUserPrincpal currentUser) {
        String username = currentUser.getUsername();
        Lender lender = authService.GetLender(username);
        return lender;
    }

    // lebon added this
    @PostMapping("/updateTotalInterest")
    public ResponseEntity<String> updateInterest(@RequestBody Lender lender) {

        if (authService.updateLender(lender)) {
            return new ResponseEntity<>(gson.toJson("Lender updated successfully"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(gson.toJson("An error occurred while updating Lender"),
                    HttpStatus.EXPECTATION_FAILED);
        }
    }

}