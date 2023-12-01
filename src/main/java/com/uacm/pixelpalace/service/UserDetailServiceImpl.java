package com.uacm.pixelpalace.service;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.uacm.pixelpalace.model.Usuario;

@Service
public class UserDetailServiceImpl implements UserDetailsService{
	/*UserDetailsService se encarga de cargar los detalles del usuario durante e proceso
	de autentificacion*/

	@Autowired
	private IUsuarioService usuarioService;

	/*@Autowired
	private BCryptPasswordEncoder bCrypt;*/

	//Inyectamos la dependencia de la session HTTP para almaenar el id de usuario
	@Autowired
	HttpSession session;

	private Logger log = LoggerFactory.getLogger(UserDetailServiceImpl.class);

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("Este es el username");
		//Buscamos el usuario en la bd por su username (Seria el email)
		Optional<Usuario> optionalUser=usuarioService.findByEmail(username);
		if (optionalUser.isPresent()) {//Verificamos que encontramos el usuario
			log.info("Esto es el id del usuario: {}", optionalUser.get().getId());
			//Almacenamos el id en la session HTTP
			session.setAttribute("idusuario", optionalUser.get().getId());
			//Obtenemos el usuario
			Usuario usuario= optionalUser.get();
			
			return User.builder().//Creamos y devolvemos un objeto UserDetails basado en el usuario recuperado
					username(usuario.getNombre()).//Se define el nombre de usuario
					password(usuario.getPassword()).//Se define la contraseña(encriptada)
					roles(usuario.getTipo()).build();//Se define los roles del usuario
		}else {
			//Lanzamos una excepcion en caso de que no se encuentre el usuario
			throw new UsernameNotFoundException("Usuario no encontrado");
		}
	}



}
