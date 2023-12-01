package com.uacm.pixelpalace.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uacm.pixelpalace.model.DetalleVenta;
import com.uacm.pixelpalace.model.Producto;
import com.uacm.pixelpalace.repository.IDetalleVentaRepository;
import com.uacm.pixelpalace.repository.IProductoRepository;


@Service
@Transactional
public class ProductoServiceImpl implements ProductoService{

	@Autowired
	private IProductoRepository productoRepository;
	
	@Autowired
    private IDetalleVentaRepository detalleRepository;

	@Override
	public Producto save(Producto producto) {
		return productoRepository.save(producto);
	}

	@Override
	public Optional<Producto> get(Integer id) {
		return productoRepository.findById(id);
	}

	@Override
	public void update(Producto producto) {
		productoRepository.save(producto);
	}

	@Override
	public void delete(Integer id) {
		productoRepository.deleteById(id);
	}

	@Override
	public List<Producto> findAll() {
		return productoRepository.findAll();
	}

	@Override
	public void deleteDetallesByProductoId(Integer id) {
		 // Obtén el producto por su ID
        Optional<Producto> optionalProducto = productoRepository.findById(id);

        if (optionalProducto.isPresent()) {
            Producto producto = optionalProducto.get();

            // Obten todos los detalles asociados al producto
            List<DetalleVenta> detalles = producto.getDetalles();

            // Elimina cada detalle de la base de datos
            for (DetalleVenta detalleVenta : detalles) {
                detalleRepository.delete(detalleVenta);
            }
        }
	}

}
