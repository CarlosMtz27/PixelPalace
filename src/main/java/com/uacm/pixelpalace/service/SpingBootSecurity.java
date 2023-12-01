package com.uacm.pixelpalace.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SpingBootSecurity extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserDetailsService userDetailService;


	//Este metodo nos sirve para configurar el AuthenticationManagerBuilder
	/*El AuthenticationManagerBuilder es una clase proporcionada por spring security
		para configurar el como se autentican los usuarios
	*/
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService).//Cargamos los detalles del usuario
		passwordEncoder(getEnecoder());//Se utiliza para cifrar y verificar las contraseñas
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {//Este metodo restringe cosas al usuario
		http.csrf().disable().authorizeRequests()//Nos ayuda a que no se inyecte codigo malicioso a la aplicacion
		.antMatchers("/administrador/**").hasRole("ADMIN")//da acceso a las vistas dependiendo del rol en el que ha ingresado
		.antMatchers("/productos/**").hasRole("ADMIN")//Se le da permiso al directorio(vistas) productos
		.and().formLogin().loginPage("/usuario/login")//elegimos el formulario donde se va a logear el usuario
		.permitAll().defaultSuccessUrl("/usuario/acceder");//El resto de vistas, se le permite al usuario normal y se redirige a acceder
	}

	//Este bean nos sirve para codificar y decodificar contraseñas de usuarios de manera segura.
	@Bean
	public BCryptPasswordEncoder getEnecoder() {
		return new BCryptPasswordEncoder();
	}


}
