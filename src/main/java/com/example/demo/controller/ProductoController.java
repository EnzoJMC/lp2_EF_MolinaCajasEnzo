package com.example.demo.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.example.demo.service.impl.PdfService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProductoController {
	
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private ProductoService productoService;
	@Autowired
	private PdfService pdfService;
	
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
		
		//Reutilizo el codigo de detalle para obtener producto
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
	
	
	@GetMapping("/generar_pdf")
	public ResponseEntity<InputStreamResource>generarPdf(HttpSession session,Model model) throws IOException{
		
		String correo = session.getAttribute("usuario").toString();
		UsuarioEntity usuarioEntity = usuarioService.buscarUsuarioPorCorreo(correo);
		
		List<ProductoEntity> productos = productoService.obtenerLosProductos();
	
		
		Map<String, Object> datosPdf = new HashMap<String,Object>();
		datosPdf.put("productos", productos);
		datosPdf.put("nombres", usuarioEntity.getNombres());
		datosPdf.put("apellidos", usuarioEntity.getApellidos());
		
		ByteArrayInputStream pdfBytes = pdfService.generarPdfDeHtml("template_pdf", datosPdf);
		
		HttpHeaders httpheaders = new HttpHeaders();
		httpheaders.add("Content-Disposition","inline: filename= productos.pdf");
		
		return ResponseEntity.ok()
				.headers(httpheaders)
				.contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(pdfBytes));
	}
}
