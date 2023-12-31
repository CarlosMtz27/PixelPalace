package com.uacm.pixelpalace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uacm.pixelpalace.model.DetalleVenta;


@Repository
public interface IDetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {

}
