package com.uacm.pixelpalace.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uacm.pixelpalace.model.Producto;
import com.uacm.pixelpalace.model.Venta;
import com.uacm.pixelpalace.service.IUsuarioService;
import com.uacm.pixelpalace.service.IVentaService;
import com.uacm.pixelpalace.service.ProductoService;

@Controller
@RequestMapping("/administrador")
public class AdministradorController {

	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IVentaService ventaService;
	
	private Logger logg= LoggerFactory.getLogger(AdministradorController.class);

	@GetMapping("")
	public String home(Model model) {

		List<Producto> productos = productoService.findAll();
		model.addAttribute("productos", productos);

		return "administrador/home";
	}
	
	@GetMapping("/usuarios")
	public String usuarios(Model model) {
		model.addAttribute("usuarios", usuarioService.findAll());
		return "administrador/usuarios";
	}
	
	@GetMapping("/ventas")
	public String ventas(Model model) {
		model.addAttribute("ventas", ventaService.findAll());
		return "administrador/ventas";
	}
	
	@GetMapping("/detalle/{id}")
	public String detalle(Model model, @PathVariable Integer id) {
		logg.info("Id de la venta {}",id);
		Venta venta= ventaService.findById(id).get();
		
		model.addAttribute("detalles", venta.getDetalle());
		
		return "administrador/detalleventa";
	}
}
