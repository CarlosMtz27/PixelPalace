package com.uacm.pixelpalace.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "productos")
public class Producto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nombre;
	@Column(length = 7000)
	private String descripcion;
	private String imagen;
	private double precio;
	private int cantidad;
	private String genero;//AQUI UNA MODIFICACION
	//Modificaciones
	private String sistema_operativo;
	private String procesador;
	private String ram;
	private String tarjeta_grafica;
	private String almacenamiento;

	@ManyToOne
	private Usuario usuario;

	 @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
	 private List<DetalleVenta> detalles;
	
	public Producto() {

	}


	/*public Producto(Integer id, String nombre, String descripcion, String imagen, double precio, int cantidad,
			Usuario usuario) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.imagen = imagen;
		this.precio = precio;
		this.cantidad = cantidad;

		this.usuario = usuario;
	}*/



	

	public String getGenero() {
		return genero;
	}


	public Producto(Integer id, String nombre, String descripcion, String imagen, double precio, int cantidad,
			String genero, String sistema_operativo, String procesador, String ram, String tarjeta_grafica,
			String almacenamiento, Usuario usuario) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.imagen = imagen;
		this.precio = precio;
		this.cantidad = cantidad;
		this.genero = genero;
		this.sistema_operativo = sistema_operativo;
		this.procesador = procesador;
		this.ram = ram;
		this.tarjeta_grafica = tarjeta_grafica;
		this.almacenamiento = almacenamiento;
		this.usuario = usuario;
	}


	public void setGenero(String genero) {
		this.genero = genero;
	}


	public Producto(Integer id, String nombre, String descripcion, String imagen, double precio, int cantidad,
			String genero, Usuario usuario) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.imagen = imagen;
		this.precio = precio;
		this.cantidad = cantidad;
		this.genero = genero;
		this.usuario = usuario;
	}

	
	

	public String getSistemaOperativo() {
		return sistema_operativo;
	}


	public void setSistemaOperativo(String sistema_operativo) {
		this.sistema_operativo = sistema_operativo;
	}


	public String getProcesador() {
		return procesador;
	}


	public void setProcesador(String procesador) {
		this.procesador = procesador;
	}


	public String getRam() {
		return ram;
	}


	public void setRam(String ram) {
		this.ram = ram;
	}


	public String getTarjeta_grafica() {
		return tarjeta_grafica;
	}


	public void setTarjeta_grafica(String tarjeta_grafica) {
		this.tarjeta_grafica = tarjeta_grafica;
	}


	public String getAlmacenamiento() {
		return almacenamiento;
	}


	public void setAlmacenamiento(String almacenamiento) {
		this.almacenamiento = almacenamiento;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}


	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}



	@Override
	public String toString() {
		return "Producto [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", imagen=" + imagen
				+ ", precio=" + precio + ", cantidad=" + cantidad + ", genero=" + genero 
				+ ", usuario=" + usuario + "]";
	}

	@OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<DetalleVenta> getDetalles() {
		// TODO Auto-generated method stub
		return null;
	}


	



}
