package com.zaruc.backend.controllers;

import com.zaruc.backend.domain.user.User;
import com.zaruc.backend.domain.user.UserRequestDTO;
import com.zaruc.backend.domain.user.UserResponseDTO;
import com.zaruc.backend.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.zaruc.backend.infra.security.TokenService;
import com.zaruc.backend.domain.LoginResponseDTO;

import java.util.List;


@RestController
@RequestMapping("/api")
public class UserController {

	final
	UserRepository repository;
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenService tokenService;

	@Autowired
	public UserController(UserRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/users")
	public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
		List<UserResponseDTO> userList = repository.findAll().stream().map(UserResponseDTO::new).toList();

		return ResponseEntity.ok(userList);
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<?> getUserByID(@PathVariable("id") String id) {
		if (id == null || id.trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Id inválido.");
		}
		UserResponseDTO user = repository.findById(id).stream().map(UserResponseDTO::new).findFirst().get();
		return ResponseEntity.ok(user);
	}

	@PutMapping("/users/{id}")
	public ResponseEntity<?> putUser(@PathVariable("id") String id, @RequestBody @Valid UserRequestDTO body) {
		if (!repository.existsById(id)) {
			return ResponseEntity.status(404).body("Erro: Usuário não encontrado.");
		}

		User user = new User(body);
		if ((user.getLogin() == null || user.getLogin().trim().isEmpty()) &&
				(user.getName() == null || user.getName().trim().isEmpty())) {
			return ResponseEntity.badRequest().body("Login ou nome deve ser informado");
		}
		User existingUser = repository.findById(id).orElseThrow();

		if (user.getLogin() != null) {
			existingUser.setLogin(user.getLogin());
		}
		if (user.getName() != null) {
			existingUser.setName(user.getName());
		}

		repository.save(existingUser);
		var authentication = new UsernamePasswordAuthenticationToken(existingUser, null, existingUser.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		var token = tokenService.generateToken(existingUser);
		return ResponseEntity.ok(new LoginResponseDTO(token));
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable("id") String id) {
		if (id == null || id.trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Id deve ser informado.");
		}

		repository.deleteById(id);
		return ResponseEntity.ok("Usuário deletado com sucesso.");
	}
}