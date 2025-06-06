package com.powercast.usuario.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.powercast.shared.exception.ResourceNotFoundException;
import com.powercast.usuario.dto.UsuarioDto;
import com.powercast.usuario.model.Usuario;
import com.powercast.usuario.repository.UsuarioRepository;

import jakarta.annotation.PostConstruct;

@Service
@Transactional
public class UsuarioService implements UserDetailsService {
	
	private final UsuarioRepository repo;
	private final PasswordEncoder encoder;
	
	public UsuarioService(UsuarioRepository repo, PasswordEncoder encoder) {
		this.repo = repo;
		this.encoder = encoder;
	}
	
	@PostConstruct
	public void seedAdmin() {
	    if (repo.count() == 0) {
	    	createUser("admin", "admin123", "ROLE_ADMIN");
	        System.out.println("👉 Usuário admin/admin123 criado automaticamente");
	    }
	}
	
	public UsuarioDto createUser(String username, String rawPassword, String role) {
		Usuario entity = Usuario.builder()
				.username(username)
				.password(encoder.encode(rawPassword))
				.role(role)
				.build();
		
		entity = repo.save(entity);
		return toDto(entity);
	}
	
	 @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        Usuario u = repo.findByUsername(username)
	            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
	        return new User(
	            u.getUsername(),
	            u.getPassword(),
	            Collections.singletonList(new SimpleGrantedAuthority(u.getRole()))
	        );
	    }
	
	public List<UsuarioDto> findAll() {
		return repo.findAll().stream()
				.map(this::toDto)
				.collect(Collectors.toList());
	}
	
	public UsuarioDto findById(Long id) {
		Usuario entity = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID " + id));
		return toDto(entity);
	}
	
	public UsuarioDto updateUser(Long id, String newUsername, String newRawPassword, String newRole) {
		Usuario entity = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID " + id));
		entity.setUsername(newUsername);
		if (newRawPassword != null && !newRawPassword.isBlank()) {
			entity.setPassword(encoder.encode(newRawPassword));
		}
		
		entity.setRole(newRole);
		entity = repo.save(entity);
		return toDto(entity);
	}
	
	public void deleteById(Long id) {
		if (!repo.existsById(id)) {
			throw new ResourceNotFoundException("Usuário não encontrado com o ID " + id);
		}
		repo.deleteById(id);
	}
	
	public UsuarioDto findByUsername(String username) {
		Usuario entity = repo.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado com o username " + username));
		return toDto(entity);
	}
	
	private UsuarioDto toDto(Usuario user) {
		return UsuarioDto.builder()
				.id(user.getId())
				.username(user.getUsername())
				.role(user.getRole())
				.build();
	}
}
