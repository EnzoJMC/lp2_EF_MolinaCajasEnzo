package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.demo.entity.ProductoEntity;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.service.ProductoService;

@Service
public class ProductoServiceImpl implements ProductoService {
	
	@Autowired
	private ProductoRepository productoRepository;
	
	@Override
	public List<ProductoEntity> obtenerLosProductos() {
	
		return productoRepository.findAll();
	}

	@Override
	public void registrarProducto(ProductoEntity productoEntity, Model model) {
		// TODO Auto-generated method stub
		productoRepository.save(productoEntity);
	}

	@Override
	public void verProducto(Model model, Long id) {
		// TODO Auto-generated method stub
		ProductoEntity productoDetalle = productoRepository.findById(id).get();
		model.addAttribute("producto",productoDetalle);
	}

	@Override
	public void eliminarProducto(Long id) {
		// TODO Auto-generated method stub
		productoRepository.deleteById(id);
		
	}
	
	

}
