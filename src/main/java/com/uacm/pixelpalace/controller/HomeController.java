package com.uacm.pixelpalace.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lowagie.text.DocumentException;
import com.uacm.pixelpalace.model.DetalleVenta;
import com.uacm.pixelpalace.model.FormaDePago;
import com.uacm.pixelpalace.model.Producto;
import com.uacm.pixelpalace.model.Usuario;
import com.uacm.pixelpalace.model.Venta;
import com.uacm.pixelpalace.service.IDetalleVentaService;
import com.uacm.pixelpalace.service.IFormaPagoService;
import com.uacm.pixelpalace.service.IUsuarioService;
import com.uacm.pixelpalace.service.IVentaService;
import com.uacm.pixelpalace.service.ProductoService;


@Controller
@RequestMapping("/")
public class HomeController {

	private final Logger log = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	
	@Autowired
	private IVentaService ventaService;
	
	@Autowired
	private IDetalleVentaService detalleVentaService;
	@Autowired
	private IFormaPagoService formapago;

	// para almacenar los detalles de la orden
	List<DetalleVenta> detalles = new ArrayList<DetalleVenta>();

	// datos de la orden
	Venta venta = new Venta();

	@GetMapping("")
	public String home(Model model, HttpSession session) {
		
		Integer idUsuario = (Integer) session.getAttribute("idusuario");

	    if (idUsuario != null) {
	        // Si el ID de usuario está presente en la sesión, puedes obtener el nombre del usuario
	        Usuario usuario = usuarioService.findById(idUsuario).orElse(null);
	        
	        if (usuario != null) {
	            // Almacenar el nombre del usuario en la sesión
	            session.setAttribute("nombreUsuario", usuario.getNombre());
	        }
	    }
		
		model.addAttribute("productos", productoService.findAll());
		
		//session
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		if(idUsuario==null) {
			return "home/home";
		}

		return "usuario/home";
	}

	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {
		log.info("Id producto enviado como parámetro {}", id);
		Producto producto = new Producto();
		Optional<Producto> productoOptional = productoService.get(id);
		producto = productoOptional.get();

		model.addAttribute("producto", producto);

		return "usuario/productohome";
	}
	
	
	@GetMapping("verproducto/{id}")
	public String verProducto(@PathVariable Integer id, Model model) {
		log.info("Id producto enviado como parámetro {}", id);
		Producto producto = new Producto();
		Optional<Producto> productoOptional = productoService.get(id);
		producto = productoOptional.get();

		model.addAttribute("producto", producto);

		return "home/ver_producto";
	}

	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {
		DetalleVenta detalleVenta = new DetalleVenta();
		Producto producto = new Producto();
		double sumaTotal = 0;

		Optional<Producto> optionalProducto = productoService.get(id);
		log.info("Producto añadido: {}", optionalProducto.get());
		log.info("Cantidad: {}", cantidad);
		producto = optionalProducto.get();

		detalleVenta.setCantidad(cantidad);
		detalleVenta.setPrecio(producto.getPrecio());
		detalleVenta.setNombre(producto.getNombre());
		detalleVenta.setTotal(producto.getPrecio() * cantidad);
		detalleVenta.setProducto(producto);
		
		//validar que le producto no se añada 2 veces
		Integer idProducto=producto.getId();
		boolean ingresado=detalles.stream().anyMatch(p -> p.getProducto().getId()==idProducto);
		
		if (!ingresado) {
			detalles.add(detalleVenta);
		}
		
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

		venta.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("venta", venta);

		return "usuario/carrito";
		
	}

	@GetMapping("/deleteProducto/{id}")
	public String deleteProducto(@PathVariable Integer id) {
	    // Antes de eliminar el producto, elimina manualmente los detalles asociados
	    productoService.deleteDetallesByProductoId(id);
	    // Ahora puedes eliminar el producto
	    productoService.delete(id);
	    return "redirect:/administrador/productos";
	}

	
	// quitar un producto del carrito
/*	@GetMapping("/delete/cart/{id}")
	public String deleteProductoCart(@PathVariable Integer id, Model model) {

		// lista nueva de prodcutos
		List<DetalleVenta> ventasNueva = new ArrayList<DetalleVenta>();

		for (DetalleVenta detalleVenta : detalles) {
			if (detalleVenta.getProducto().getId() != id) {
				ventasNueva.add(detalleVenta);
			}
		}

		// poner la nueva lista con los productos restantes
		detalles = ventasNueva;

		double sumaTotal = 0;
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

		venta.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("venta", venta);

		return "usuario/carrito";
	} */
	
	@GetMapping("/getCart")
	public String getCart(Model model, HttpSession session) {
		
		model.addAttribute("cart", detalles);
		model.addAttribute("venta", venta);
		
		//sesion
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		return "/usuario/carrito";
	}
	@GetMapping("/order")
	public String order(Model model, HttpSession session, @RequestParam("formaDePagoId") Integer formaDePagoId) {
	    // Recuperar el objeto formaDePago utilizando el ID
	    FormaDePago formaDePago = formapago.get(formaDePagoId).orElse(null);
	    //System.out.print("hahahhshshshshshshshs\n\n");
	    System.out.print(formaDePago);
	    //System.out.print("hahahhshshshshshshshs\n\n");
	    if (formaDePago != null) {
	        // ... Realiza las operaciones con el objeto formaDePago

	        // Agregar el objeto formaDePago al modelo de la vista
	        model.addAttribute("formaDePago", formaDePago);

	        Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
	        model.addAttribute("cart", detalles);
	        model.addAttribute("venta", venta);
	        model.addAttribute("usuario", usuario);
	        //model.addAttribute("formaDePago", formaDePago);

	        return "usuario/resumenventa";
	    } else {
	        // Manejar el caso en el que el objeto formaDePago no se encontró
	        return "redirect:/"; // Redirigir a una página de error o a donde sea necesario
	    }
	}

	
	
	@GetMapping("/tarjetas")
	public String tarjetasUsuario(Model model, HttpSession session) {
	    Integer idUsuario = Integer.parseInt(session.getAttribute("idusuario").toString());
	    List<FormaDePago> formas = formapago.findAllByUsuarioId(idUsuario);
	    model.addAttribute("tarjetasUsuario", formas);
	    return "usuario/tarjetas";
	}

	
	@GetMapping("/agregar-tarjeta")
	public String agregarTarjeta(Model model, HttpSession session) {
		return "usuario/agregar-tarjeta";
	}
	
	@GetMapping("/tarjeta")
	public String agregarTarjeta(HttpSession session, Model model) {
		
		
		return "usuario/agregar-tarjeta";
	}
	
	@PostMapping("/saveVenta")
	public String saveVenta(@ModelAttribute("formaDePago") FormaDePago formaDePago, Model model, HttpSession session) throws DocumentException {
	    Date fechaCreacion = new Date();
	    venta.setFechaCreacion(fechaCreacion);
	    venta.setNumero(ventaService.generarNumeroVenta());
	    System.out.println();
	    System.out.println();
	    System.out.println();
	    System.out.println(formaDePago.getNombreTitular());
	    System.out.println(formaDePago.getTipo());
	    System.out.println(formaDePago.getNumero());
	    System.out.println();
	    System.out.println();
	    System.out.println();
	    // Usuario
	    Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
	    
	    venta.setUsuario(usuario);
	    ventaService.save(venta);
	    
	    System.out.println(venta);
	    // Guardar detalles
	    for (DetalleVenta dt : detalles) {
	        dt.setVenta(venta);
	        detalleVentaService.save(dt);
	        System.out.println(dt);
	    }
	    
	    model.addAttribute("venta", venta);
	    model.addAttribute("detalles", detalles);
	    model.addAttribute("usuario",usuario);
	    model.addAttribute("formadepago", formaDePago);
	    
	    return "usuario/ticketCompraView"; // Nombre de la vista del ticket de compra
	}

	@GetMapping("/juegos")
	public String showAllGames(Model model) {
	    List<Producto> productos = productoService.findAll();
	    model.addAttribute("productos", productos);
	    return "usuario/juegos";
	}

	
	@PostMapping("/search")
	public String searchProduct(@RequestParam String nombre, Model model, HttpSession session) {
		log.info("Nombre del producto: {}", nombre);
		String nombres =  nombre.toLowerCase();
		//List<Producto> productos= productoService.findAll().stream().filter( p -> p.getNombre().contains(nombre)).collect(Collectors.toList());
		 List<Producto> productos = productoService.findAll().stream()
		            .filter(p -> p.getNombre().toLowerCase().contains(nombres))
		            .collect(Collectors.toList());
		model.addAttribute("productos", productos);		
		
		Integer idUsuario = (Integer) session.getAttribute("idusuario");

		
		if(idUsuario == null) {
			return "home/home";
		}else {
			return "usuario/home";
		}
		//return "usuario/home";
	}
	
	@PostMapping("/searchGames")
	public String searchGames(@RequestParam String nombre, Model model) {
		log.info("Nombre del producto: {}", nombre);
		String nombres =  nombre.toLowerCase();
	    List<Producto> productos = productoService.findAll().stream()
	            .filter(p -> p.getNombre().toLowerCase().contains(nombres))
	            .collect(Collectors.toList());
	model.addAttribute("productos", productos);
	    return "usuario/juegos";  // Ajusta el nombre de la vista según la estructura de tu proyecto
	}
	
	@GetMapping("/filterByGenre")
	public String filterByGenre(@RequestParam String genre, Model model) {
	    log.info("Filtrar por género: {}", genre);

	    if ("todos".equalsIgnoreCase(genre)) {
	        List<Producto> productos = productoService.findAll();
	        model.addAttribute("productos", productos);
	    } else {
	        List<Producto> productos = productoService.findAll().stream()
	                .filter(p -> p.getGenero().equalsIgnoreCase(genre))
	                .collect(Collectors.toList());
	        model.addAttribute("productos", productos);
	        model.addAttribute("generoFiltrado", genre); // Agrega el género al modelo
	    }

	    return "usuario/juegos";
	}	
	
	@GetMapping("/filterByLetter")
	public String filterByLetter(@RequestParam String letter, Model model) {
	    log.info("Filtrar por letra: {}", letter);

	    if ("todos".equalsIgnoreCase(letter)) {
	        List<Producto> productos = productoService.findAll();
	        model.addAttribute("productos", productos);
	    } else {
	        List<Producto> productos = productoService.findAll().stream()
	                .filter(p -> p.getNombre().toLowerCase().startsWith(letter.toLowerCase()))
	                .collect(Collectors.toList());
	        model.addAttribute("productos", productos);
	        model.addAttribute("letraFiltrada", letter); // Agrega la letra al modelo
	    }

	    return "usuario/juegos";
	}
}
/*private byte[] generarPDF() throws DocumentException {
ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
Document document = new Document();

PdfWriter.getInstance(document, outputStream);
document.open();

// Agregar contenido al PDF
document.add(new Paragraph("Número de Venta: " + venta.getNumero()));
document.add(new Paragraph("Fecha de Creación: " + venta.getFechaCreacion()));
// Agregar más detalles según tus necesidades

// Agregar detalles de la venta
document.add(new Paragraph("Detalles de la Venta:"));
for (DetalleVenta detalle : detalles) {
    document.add(new Paragraph("Producto: " + detalle.getProducto().getNombre()));
    document.add(new Paragraph("Cantidad: " + detalle.getCantidad()));
    document.add(new Paragraph("Precio Unitario: " + detalle.getPrecio()));
    document.add(new Paragraph("Total: " + detalle.getTotal()));
    document.add(new Paragraph(" ")); // Espacio entre detalles
}

document.close();
return outputStream.toByteArray();
}*/
//guardar la orden
	/*@GetMapping("/saveVenta")
	public String saveVenta(HttpSession session ) {
		Date fechaCreacion = new Date();
		venta.setFechaCreacion(fechaCreacion);
		venta.setNumero(ventaService.generarNumeroVenta());
		
		//usuario
		Usuario usuario =usuarioService.findById( Integer.parseInt(session.getAttribute("idusuario").toString())  ).get();
		
		venta.setUsuario(usuario);
		ventaService.save(venta);
		
		//guardar detalles
		for (DetalleVenta dt:detalles) {
			dt.setVenta(venta);
			detalleVentaService.save(dt);
		}
		
		///limpiar lista y venta
		venta = new Venta();
		detalles.clear();
		
		return "redirect:/";
	}*/
/*@GetMapping("/order")
public String order(Model model, HttpSession session) {
	
	Usuario usuario =usuarioService.findById( Integer.parseInt(session.getAttribute("idusuario").toString())).get();
	FormaDePago formaDePago = (FormaDePago) model.getAttribute("formaDePago");
	
	//FormaDepago formaDePago = formapago.get()
	model.addAttribute("cart", detalles);
	model.addAttribute("venta", venta);
	model.addAttribute("usuario", usuario);
	System.out.println(formaDePago);

    // Agregar el objeto formaDePago al modelo de la vista
    model.addAttribute("formaDePago", formaDePago);
	return "usuario/resumenventa";
}*/
//log.info("Sesion del usuario: {}", session.getAttribute("idusuario"));