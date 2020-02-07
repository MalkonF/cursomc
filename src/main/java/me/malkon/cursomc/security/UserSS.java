package me.malkon.cursomc.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import me.malkon.cursomc.domain.enums.Perfil;

/*Essa classe precisa implementar interface UserDetails. É contrato q o spring security
 *  precisa p trabalhar c usuarios*/
public class UserSS implements UserDetails {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String email;
	private String senha;
	private Collection<? extends GrantedAuthority> authorities;// armazena perfis do usuario

	public UserSS() {
	}

	public UserSS(Integer id, String email, String senha, Set<Perfil> perfis) {
		super();
		this.id = id;
		this.email = email;
		this.senha = senha;
		this.authorities = perfis.stream().map(x -> new SimpleGrantedAuthority(x.getDescricao()))
				.collect(Collectors.toList());
		/*
		 * converte set de perfis para lista do tipo authorities. O Spring precisa saber
		 * se é ROLE_ADMIN ROLE_CLIENT
		 */
	}

	public Integer getId() {
		return id;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
		/*
		 * sim, a conta não está expirada. Aqui poderia ser implementado a lógica
		 * aContaNEstaExpirada? true, a conta não está expirada. Aqui poderia ser // que
		 * diz que o usuário pode estar com a conta expirada implementado a lógica que
		 * diz que o usuário pode estar com a conta expirada Ex: Se algo acontecer, a
		 * conta esta expirada, entao retorna false
		 */
	}
	/* aContaNEstaBloqueada? true, a conta n esta bloqueada */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {

		return true;
	}

	public boolean hasRole(Perfil perfil) {
		return getAuthorities().contains(new SimpleGrantedAuthority(perfil.getDescricao()));
	}/*
		 * getAuthorities retorna a lista GrantedAuthority e contains verifica se tá
		 * admin ou cliente
		 */
}