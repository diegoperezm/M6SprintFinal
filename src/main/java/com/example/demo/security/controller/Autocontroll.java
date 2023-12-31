package com.example.demo.security.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.security.models.ERole;
import com.example.demo.security.models.Role;
import com.example.demo.security.models.User;
import com.example.demo.security.payload.request.LoginRequest;
import com.example.demo.security.payload.request.SignUpRequest;
import com.example.demo.security.payload.response.JwtResponse;
import com.example.demo.security.payload.response.MessageResponse;
import com.example.demo.security.repo.RoleRepository;
import com.example.demo.security.repo.UserRepository;
import com.example.demo.security.services.UserDetailsImpl;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class Autocontroll {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(
				new JwtResponse(jwt,
								userDetails.getId(),
								userDetails.getUsername(),
								userDetails.getEmail(),
								roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid SignUpRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_ADMINISTRATIVO)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "cliente":
					Role clienteRole = roleRepository.findByName(ERole.ROLE_CLIENTE)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(clienteRole);

					break;
				case "pro":
					Role proRole = roleRepository.findByName(ERole.ROLE_PROFESIONAL)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(proRole);

					break;
				case "admi":
					Role admiRole = roleRepository.findByName(ERole.ROLE_ADMINISTRATIVO)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(admiRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_CLIENTE)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
}
