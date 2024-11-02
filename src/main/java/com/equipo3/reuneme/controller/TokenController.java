package com.equipo3.reuneme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.equipo3.reuneme.service.TokenService;

@RestController
@RequestMapping("tokens")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET})
public class TokenController {

    // Inyección correcta de dependencia con @Autowired
    @Autowired
    private TokenService tokenService;

    @GetMapping("/validarToken")  //?idToken=valor
    public void validarToken(@RequestParam(name = "idToken", required = true) String idToken) {
        this.tokenService.validarToken(idToken);
    }
    
    @GetMapping("/obtenerEmail")  // ?idToken=valor
    public String obtenerEmail(@RequestParam(name = "idToken", required = true) String idToken) {
        return this.tokenService.obtenerEmail(idToken);
    }
}
