package com.example.demo.service;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.entity.ProductoEntity;
import com.example.demo.entity.UsuarioEntity;


public interface ProductoService {
	List<ProductoEntity> obtenerLosProductos();
	void registrarProducto(ProductoEntity productoEntity, Model model);
	void verProducto(Model model, Long id);
	void eliminarProducto(Long id);
}
