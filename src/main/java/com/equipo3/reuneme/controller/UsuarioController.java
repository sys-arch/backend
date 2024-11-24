package com.equipo3.reuneme.controller;

import java.util.Map;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.ResponseEntity;


import com.equipo3.reuneme.model.Empleado;
import com.equipo3.reuneme.model.RegistroEmp;
import com.equipo3.reuneme.service.EmailService;
import com.equipo3.reuneme.service.PasswordService;
import com.equipo3.reuneme.service.UsuarioService;
import com.equipo3.reuneme.security.JwtTokenProvider;
import java.util.HashMap;


@RestController
@RequestMapping("/users")
public class UsuarioController {

    @Autowired
    UsuarioService userservice;

    @Autowired
    EmailService emailservice;
    
    @Autowired
    PasswordService pwdservice;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    /*
     * 
     */
    
    /////////////////////////////
    //REGISTRO EMPLEADOS
    ////////////////////////////
    @PostMapping("/register")
    public void register(@RequestBody RegistroEmp re) {
        // Comprobamos que ambas contraseñas son iguales
    	if(!this.pwdservice.isSamePwd(re.getPwd1(), re.getPwd2())) {
    		throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Las contraseñas no son iguales");
    	}
    	
    	// Comprobamos que la contraseña cumple requisitos de seguridad
    	if(!this.pwdservice.isValid(re.getPwd1())) {
    		throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, 
    				"Las contraseña no cumple con los requisitos de seguridad: "
    				+ "Entre 8 y 24 caracteres, Debe contener una maysucula, una minuscula, un digito, "
    				+ "un caracter especial y no debe contener espacios");
    	}
        

        // Comprobamos que el email tiene un formato válido
        if (!emailservice.validarEmail(re.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email insertado no tiene un formato válido: "
            		+ "usuario@dominio.com");
        }

        // Si pasa los controles, se registra en BD
        Empleado emp = new Empleado();
        emp.setEmail(re.getEmail().toLowerCase());
        emp.setPwd(re.getPwd1()); // Asignar la contraseña validada
        emp.setNombre(re.getNombre());
        emp.setApellido1(re.getApellido1());
        emp.setApellido2(re.getApellido2());
        emp.setCentro(re.getCentro());
        emp.setDepartamento(re.getDepartamento());
        emp.setPerfil(re.getPerfil());
        emp.setFechaalta(re.getFechaalta());
        emp.setBloqueado(re.isBloqueado());
        emp.setVerificado(re.isVerificado());

        this.userservice.registrar(emp);
    }
    
    /////////////////////////////
    //LOGIN UNICO
    ////////////////////////////
    @PutMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> info) {
        String email = info.get("email").toString().toLowerCase();
        String pwd = org.apache.commons.codec.digest.DigestUtils.sha512Hex(info.get("pwd").toString());

        if (loginAttemptService.isBlocked(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Algo ha pasado, inténtelo más tarde.")); // Mensaje para el front
        }

        try {
            boolean loginResult = userservice.login(email, pwd);

            if (loginResult) {
                String token = "fake-jwt-token"; // Sustituir por la lógica de generación de tokens
                return ResponseEntity.ok(Map.of("token", token));
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas.");
            }
        } catch (ResponseStatusException e) {
            loginAttemptService.loginFailed(email); // Incrementa los intentos
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("error", e.getReason()));
        }
    }

	/////////////////////////////
	//GENERA CLAVE DOBLE FACTOR DE AUTHENTICACIÓN
	////////////////////////////
	@PutMapping("/activar-2fa")
	public String activar2FA(@RequestBody Map<String, String> info) {
	    String email = info.get("email").toLowerCase();
	    return userservice.activar2FA(email);
	}
	
	/////////////////////////////
	//VERIFICACIÓN DOBLE FACTOR DE AUTHENTICACIÓN
	////////////////////////////
	@PutMapping("/verify-2fa")
	public ResponseEntity<Map<String, String>> verificarTwoFactorAuth(@RequestBody Map<String, Object> info) {
	    String email = info.get("email").toString().toLowerCase();
	    Integer authCode = (Integer) info.get("authCode");

	    Map<String, String> response = new HashMap<>();
	    try {
	        boolean isValidCode = userservice.verificarTwoFactorAuthCode(email, authCode);
	        if (isValidCode) {
	            response.put("message", "Autenticación de doble factor exitosa");
	            return ResponseEntity.ok(response);
	        } else {
	            response.put("message", "Código de autenticación incorrecto");
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	        }
	    } catch (ResponseStatusException ex) {
	        response.put("message", ex.getReason());
	        return ResponseEntity.status(ex.getStatusCode()).body(response);
	    }
	}

	
	/////////////////////////////
	//GENERACIÓN CÓDIGO QR
	////////////////////////////
	@GetMapping("/generate-qr-code")
	public String getQRCodeUrl(String secretKey, String account) {
	    String issuer = "ReuneMeApp";
	    String qrCodeData = String.format(
	        "otpauth://totp/%s:%s?secret=%s&issuer=%s",
	        URLEncoder.encode(issuer, StandardCharsets.UTF_8),
	        URLEncoder.encode(account, StandardCharsets.UTF_8),
	        URLEncoder.encode(secretKey, StandardCharsets.UTF_8),
	        URLEncoder.encode(issuer, StandardCharsets.UTF_8)
	    );

	    return "https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=" + URLEncoder.encode(qrCodeData, StandardCharsets.UTF_8);
	}
	
	/////////////////////////////
	//GENERACIÓN CÓDIGO QR
	////////////////////////////
	@PutMapping("/desactivar-2fa")
    public void desactivarTwoFA(@RequestBody Map<String, Object> updateData) {
        String email = (String) updateData.get("email");
        String clavesecreta = (String) updateData.get("clavesecreta");
        boolean twoFA = (boolean) updateData.get("twoFA");

        userservice.desactivar2FA(email, clavesecreta, twoFA);
    }

}

