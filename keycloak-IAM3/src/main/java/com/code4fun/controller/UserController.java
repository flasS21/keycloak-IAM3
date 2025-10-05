package com.code4fun.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@GetMapping("/debug-auth")
	public Object debugAuth(Authentication auth) {
		return Map.of(
				"name", auth.getName(), 
				"authorities", auth.getAuthorities(), 
				"principal", auth.getPrincipal());
	}

	// TODO :: ONLY ADMIN CAN ACCESS ANY /users ENDPOINT
	@GetMapping("/users")
	public List<String> getAllUsers() {
		return Arrays.asList("Maverick (ADMIN)", "Kent (USER)");
	}

	@PostMapping("/users")
	public String createUser(@RequestBody String name) {
		return "User created: " + name;
	}

	@PutMapping("/users/{id}")
	public String updateUser(@PathVariable String id, @RequestBody String name) {
		return "User updated: ID=" + id + ", Name=" + name;
	}

	@DeleteMapping("/users/{id}")
	public String deleteUser(@PathVariable String id) {
		return "User deleted: ID=" + id;
	}

	// TODO :: Public endpoint (outside /users)
	@GetMapping("/public")
	public String publicEndpoint() {
		return "Public access — no auth needed";
	}

	// TODO :: Authenticated-only (non-admin) endpoint
	@GetMapping("/profile")
	public String getProfile() {
		return "Hello, authenticated user!";
	}

}