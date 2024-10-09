package com.equipo3.reuneme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.equipo3.reuneme.service.TokenService;

@RestController
@RequestMapping("tokens")
@CrossOrigin("*")
public class TokenController {
	
	private TokenService tokenService;
	
	@Autowired
	@GetMapping("/validarToken")  //?idToken=
	public void validarToken(@RequestParam String idToken) {
		this.tokenService.validarToken(idToken);
	}

}