package com.uacm.pixelpalace.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uacm.pixelpalace.model.Usuario;
import com.uacm.pixelpalace.model.Venta;
import com.uacm.pixelpalace.repository.IVentaRepository;

@Service
public class VentaServiceImpl implements IVentaService {

	@Autowired
	private IVentaRepository ventaRepository;

	@Override
	public Venta save(Venta venta) {
		return ventaRepository.save(venta);
	}

	@Override
	public List<Venta> findAll() {
		return ventaRepository.findAll();
	}
	// 0000010
	@Override
	public String generarNumeroVenta() {
		int numero=0;
		String numeroConcatenado="";

		List<Venta> ordenes = findAll();

		List<Integer> numeros= new ArrayList<>();

		ordenes.stream().forEach(o -> numeros.add( Integer.parseInt( o.getNumero())));

		if (ordenes.isEmpty()) {
			numero=1;
		}else {
			numero= numeros.stream().max(Integer::compare).get();
			numero++;
		}

		if (numero<10) { //0000001000
			numeroConcatenado="000000000"+String.valueOf(numero);
		}else if(numero<100) {
			numeroConcatenado="00000000"+String.valueOf(numero);
		}else if(numero<1000) {
			numeroConcatenado="0000000"+String.valueOf(numero);
		}else if(numero<10000) {
			numeroConcatenado="0000000"+String.valueOf(numero);
		}

		return numeroConcatenado;
	}

	@Override
	public Optional<Venta> findById(Integer id) {
		// TODO Auto-generated method stub
		return ventaRepository.findById(id);
	}

	@Override
	public List<Venta> findByUsuario(Usuario usuario) {
		// TODO Auto-generated method stub
		return ventaRepository.findByUsuario(usuario);
	}


}
