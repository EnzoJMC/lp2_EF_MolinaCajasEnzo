package com.example.demo.entity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tb_usuario")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEntity {
	@Id
	@Column(name = "correo")
	private String correo;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "nombres")
	private String nombres;
	
	@Column(name = "apellidos")
	private String apellidos;

	@Column(name = "fecha_nacimiento")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate fechNac;
	
	@Column(name = "urlImagen")
	private String urlImagen;
}
