package com.uacm.pixelpalace.controller;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uacm.pixelpalace.model.FormaDePago;
import com.uacm.pixelpalace.model.Producto;
import com.uacm.pixelpalace.model.Usuario;
import com.uacm.pixelpalace.model.Venta;
import com.uacm.pixelpalace.service.IFormaPagoService;
import com.uacm.pixelpalace.service.IUsuarioService;
import com.uacm.pixelpalace.service.IVentaService;
import com.uacm.pixelpalace.service.ProductoService;
import com.uacm.pixelpalace.service.SendMailService;


@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	
	private final Logger logger= LoggerFactory.getLogger(UsuarioController.class);
	
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IVentaService ventaService;
	
	@Autowired
	private IFormaPagoService formaPagoService;
	
	@Autowired
	private SendMailService sendMailService;
	
	private String codigoVerificacion;
	
	//Creamos el objeto para encriptar la contraseña del usuario
	BCryptPasswordEncoder passEncode= new BCryptPasswordEncoder();
	
	
	// /usuario/registro
	@GetMapping("/registro")
	public String create() {
		return "usuario/registro";
	}
	
	@PostMapping("/save")
	public String save(Usuario usuario) {
		logger.info("Usuario registro: {}", usuario);
		usuario.setTipo("USER");
		usuario.setPassword( passEncode.encode(usuario.getPassword()));//encriptamos la contraseña con el objeto que creamos
		usuarioService.save(usuario);		
		return "redirect:/";
	}
	
	
	@GetMapping("/login")
	public String login() {
		return "usuario/login";
	}
	
	@GetMapping("/acceder")
	public String acceder(Usuario usuario, HttpSession session) {
		logger.info("Accesos : {}", usuario);
		
		Optional<Usuario> user=usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString()));
		
		if (user.isPresent()) {
			session.setAttribute("idusuario", user.get().getId());
			
			if (user.get().getTipo().equals("ADMIN")) {
				return "redirect:/administrador";
			}else {
				return "redirect:/";
			}
		}else {
			logger.info("Usuario no existe");
		}
		
		return "redirect:/";
	}
	
	@GetMapping("/recuperarContraseña")
	public String recuperarContraseña() {
		return "usuario/recuperar-contraseña";
	}
	
	@PostMapping("/enviarCodigoRecuperacion")
	public String codigoRecuperacion(@RequestParam String email ,Model model, HttpSession session) {
		codigoVerificacion = generarCodigo();
		
		
		sendMailService.sendStyledMail("pixelpalaceuacm@gmail.com", email, "Codigo de verifcacion",codigoVerificacion);
        
		model.addAttribute("correoUsuario",email);
	    return "usuario/codigo-verificacion";
	}
	
	public static String generarCodigo() {
        int longitud = 6;

        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        Random random = new Random();
        StringBuilder codigo = new StringBuilder(longitud);

        for (int i = 0; i < longitud; i++) {
            int indice = random.nextInt(caracteres.length());

            codigo.append(caracteres.charAt(indice));
        }

        return codigo.toString();
    }
	
	@PostMapping("/verificarCodigoRecuperacion")
	public String verificarcodigoRecuperacion(@RequestParam String codigo,@RequestParam String email ,Model model, HttpSession session) {
		
		if(codigoVerificacion.equals(codigo)) {
			model.addAttribute("correoUsuario", email);
			return "usuario/cambiar-contraseña";
		}else {
		
		return "usuario/error-codigo-recuperacion";
		}
	}
	
	@PostMapping("/actualizarContraseña")
	public String actualizarContaseña(@RequestParam String email, @RequestParam String newPassword,@RequestParam String confirmPassword,Model model, HttpSession session) {
		Optional<Usuario> usuario;
		usuario = usuarioService.findByEmail(email);
		
		if(newPassword.equals(confirmPassword)) {
			usuario.get().setPassword(passEncode.encode(newPassword));
			usuarioService.update(usuario.get());
		}
		
		return "redirect:/";
	}
	
	
	@GetMapping("/compras")
	public String obtenerCompras(Model model, HttpSession session) {
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		
		Usuario usuario= usuarioService.findById(  Integer.parseInt(session.getAttribute("idusuario").toString()) ).get();
		List<Venta> ventas= ventaService.findByUsuario(usuario);
		//logger.info("ordenes {}", ordenes);
		
		model.addAttribute("ventas", ventas);
		
		return "usuario/compras";
	}
	
	@GetMapping("/detalle/{id}")
	public String detalleCompra(@PathVariable Integer id, HttpSession session, Model model) {
		logger.info("Id de la orden: {}", id);
		Optional<Venta> venta=ventaService.findById(id);
		
		model.addAttribute("detalles", venta.get().getDetalle());
		
		
		//session
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		return "usuario/detallecompra";
	}
	
	@GetMapping("/cerrar")
	public String cerrarSesion( HttpSession session ) {
		session.removeAttribute("idusuario");
		return "redirect:/";
	}
	@GetMapping("/juegos")
	public String Listajuegos(Model model) {
	    List<Producto> juegos = productoService.findAll();
	    model.addAttribute("juegos", juegos);
	    return "usuario/juegos";
	}
	
	@PostMapping("/agregar-tarjeta")
	public String agregarTarjeta(Model model, HttpSession session, @ModelAttribute("formaDePago") FormaDePago formaDePago) {
	    // Obtener el ID del usuario de la sesión
	    Integer userId = (Integer) session.getAttribute("idusuario");
	    Optional<Usuario> usuario = usuarioService.findById(userId);
        formaDePago.setUsuario(usuario.get());
        FormaDePago forma = new FormaDePago();
        String url="";
        // Imprimir los datos antes de guardarlos
        System.out.println("Datos a agregar a la base de datos:");
        System.out.println("Número de tarjeta: " + formaDePago.getNumero());
        System.out.println("Nombre del titular: " + formaDePago.getNombreTitular());
        System.out.println("Fecha de expiración: " + formaDePago.getFechaExpiracion());
        System.out.println("Código de seguridad: " + formaDePago.getCodigoSeguridad());
	    List<FormaDePago> formas = formaPagoService.findAllByUsuarioId(userId);
	    Boolean resultado=false;
	    int indice=0;
	    for(int i=0; i<formas.size();i++) {
	    	System.out.println(formas.get(i).getNumero());
	    	//System.out.println("numero recibido");
	    	System.out.println(formaDePago.getNumero());
	    	if(!(formas.get(i).getNumero().equals(formaDePago.getNumero()))) {
	    		//formaPagoService.save(formaDePago);
	    		//System.out.print("Hola mundo\n");
	    		//forma=formaPagoService.findByNumeroAndUsuarioId(formas.get(i).getNumero(), formas.get(i).getUsuario().getId());
	    		//System.out.print(forma);
	    		//url="redirect:/order?formaDePagoId=" + forma.getId()
	    		//indice = i;
	    		resultado=false;
	    		System.out.println(resultado);
	    	}else {
	    		
	    		resultado=true;
	    		indice=i;
	    		break;
	    	}
	    }
	    //System.out.print(forma);
	    if(resultado == false) {
	    	System.out.println(formaDePago);
	    	formaPagoService.save(formaDePago);
	    	forma=formaPagoService.findByNumeroAndUsuarioId(formaDePago.getNumero(),formaDePago.getUsuario().getId());
	    	url="redirect:/order?formaDePagoId=" + forma.getId();
	    	System.out.println(forma);
	    }else {
	    	forma=formaPagoService.findByNumeroAndUsuarioId(formas.get(indice).getNumero(), formas.get(indice).getUsuario().getId());
	    	url="redirect:/order?formaDePagoId=" + forma.getId();
	    	System.out.println("Si esta en la bd");
	    }
	    System.out.println("Los datos son: \n\n");
	    System.out.print(forma);
	    
	    model.addAttribute("formaDePago", forma);
	    
	    return url;
	    
	    
	    //Integer idUsuario = Integer.parseInt(session.getAttribute("idusuario").toString());
	    //List<FormaDePago> formas = formaPagoService.findAllByUsuarioId(idUsuario);
	    //if (userId != null) {
	        // Asignar el usuario al objeto FormaDePago
	        //Optional<Usuario> usuario = usuarioService.findById(userId);
	        //formaDePago.setUsuario(usuario.get());
	        //formaDePago.setCodigoSeguridad(forma)

	       

	        // Guardar el objeto FormaDePago en la base de datos
	        //formaPagoService.save(formaDePago);
	    //}
	    //model.addAttribute("formaDePago", formaDePago);
	    // Redirigir al controlador que muestra las tarjetas
	    
	}


}
