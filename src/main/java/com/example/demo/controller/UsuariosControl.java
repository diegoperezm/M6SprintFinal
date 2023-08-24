package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dao.UsuariosDAO;
import com.example.demo.model.Usuarios;

@Controller
@RequestMapping("/")
public class UsuariosControl {
	private final UsuariosDAO usuariosDAO;
  
    @Autowired	
	public UsuariosControl(UsuariosDAO usuariosDAO) {
		this.usuariosDAO = usuariosDAO;
	}

    @GetMapping("/crearUsuarios")
    public String crearUsuarios(Model model) {
    	model.addAttribute("usuario", new Usuarios());
    	return "crearusuario";
    }
    
    @PostMapping("/agregarUsuarios")
    public String agregarUsuarios(Usuarios usuarios) {
    	usuariosDAO.save(usuarios);
    	return "redirect:/listarUsuarios";
    }
    
	@GetMapping("/listarUsuarios")
	public String listarUsuarios(Model model) {
	  List<Usuarios> listarUsuarios =  usuariosDAO.findAll();
	  model.addAttribute("listarUsuarios", listarUsuarios);
      return "listarusuarios";
	}
  
   
}
