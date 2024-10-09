package com.equipo3.reuneme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.equipo3.reuneme.services.TokenService;

@RestController
@RequestMapping("tokens")
@CrossOrigin("*")
public class TokenController {
	
	@Autowired
	private TokenService tokenService;
	
	
	
	@GetMapping("/validarToken")  //?idToken=
	public void validarToken(@RequestParam String idToken) {
		this.tokenService.validarToken(idToken);
	}

}
