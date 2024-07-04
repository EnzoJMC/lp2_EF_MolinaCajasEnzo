package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.ProductoEntity;
import com.example.demo.entity.UsuarioEntity;
import com.example.demo.service.ProductoService;
import com.example.demo.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProductoController {
	
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private ProductoService productoService;
	
	
	@GetMapping("/menu")
	public String showMenu(HttpSession session, Model model) {
		if(session.getAttribute("usuario") ==null) {
			return "redirect:/";
		}
		
		String correo = session.getAttribute("usuario").toString();
		UsuarioEntity usuarioEntity = usuarioService.buscarUsuarioPorCorreo(correo);
		model.addAttribute("foto",usuarioEntity.getUrlImagen());
		model.addAttribute("nombres",usuarioEntity.getNombres());
		
		List<ProductoEntity>productos = productoService.obtenerLosProductos();
		model.addAttribute("productos",productos);
		
		return "menu";
	}
	
	@GetMapping("/registrar_producto")
	public String mostrarRegistrarProducto(HttpSession session,Model model) {
		model.addAttribute("producto",new ProductoEntity());
		
		if(session.getAttribute("usuario") ==null) {
			return "redirect:/";
		}
		
		String correo = session.getAttribute("usuario").toString();
		UsuarioEntity usuarioEntity = usuarioService.buscarUsuarioPorCorreo(correo);
		model.addAttribute("foto",usuarioEntity.getUrlImagen());
		model.addAttribute("nombres",usuarioEntity.getNombres());
		
		return "registrar_producto";
	}
	
	@PostMapping("/registrar_producto")
	public String registrarProducto(HttpSession session,@ModelAttribute ProductoEntity producto, Model model) {
		productoService.registrarProducto(producto, model);

		String correo = session.getAttribute("usuario").toString();
		UsuarioEntity usuarioEntity = usuarioService.buscarUsuarioPorCorreo(correo);
		model.addAttribute("foto", usuarioEntity.getUrlImagen());
		model.addAttribute("nombres", usuarioEntity.getNombres());

		List<ProductoEntity> productos = productoService.obtenerLosProductos();
		model.addAttribute("productos", productos);

		return "menu";

	}
	

	@GetMapping("/actualizar_producto/{id}")
	public String mostrarActualizarProducto(Model model, @PathVariable("id") Long id, HttpSession session) {
		
		//Reutilizo el codigo de obtener producto
		productoService.verProducto(model, id);
		
		String correo = session.getAttribute("usuario").toString();
		UsuarioEntity usuarioEntity = usuarioService.buscarUsuarioPorCorreo(correo);
		model.addAttribute("foto", usuarioEntity.getUrlImagen());
		model.addAttribute("nombres", usuarioEntity.getNombres());

		return "actualizar_producto";
	}
	
	@GetMapping("/detalle_producto/{id}")
	public String verProducto(Model model, @PathVariable("id") Long id,HttpSession session) {

		String correo = session.getAttribute("usuario").toString();
		UsuarioEntity usuarioEntity = usuarioService.buscarUsuarioPorCorreo(correo);
		model.addAttribute("foto", usuarioEntity.getUrlImagen());
		model.addAttribute("nombres", usuarioEntity.getNombres());

		
		productoService.verProducto(model, id);
		
		return "detalle_producto";
	}
	
	@GetMapping("/delete/{id}")
	public String eliminarProducto(@PathVariable("id")Long id,HttpSession session,Model model) {
		
		productoService.eliminarProducto(id);
		
		String correo = session.getAttribute("usuario").toString();
		UsuarioEntity usuarioEntity = usuarioService.buscarUsuarioPorCorreo(correo);
		model.addAttribute("foto", usuarioEntity.getUrlImagen());
		model.addAttribute("nombres", usuarioEntity.getNombres());
		List<ProductoEntity> productos = productoService.obtenerLosProductos();
		model.addAttribute("productos", productos);
		return "menu";
	}
	
}
