package com.equipo3.reuneme;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import com.equipo3.reuneme.controller.AdminController;
import com.equipo3.reuneme.controller.UsuarioController;
import com.equipo3.reuneme.model.Administrador;
import com.equipo3.reuneme.model.Empleado;
import com.equipo3.reuneme.service.AdminService;
import com.equipo3.reuneme.service.UsuarioService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc // Opcional si usas MockMvc
public class AppTest {
	  @Autowired
	    private UsuarioController ucontrol;

	    @Autowired
	    private AdminController adController;

	    @MockBean
	    private UsuarioService uservice;

	    @MockBean
	    private AdminService adService;

	    // --------------------------
	    // Pruebas de Login
	    // --------------------------

	    @Test
	    public void testLoginBien() {
	        String email = "guille@gmail.com";
	        String password = "Aa123456?";
	        String hashedPassword = org.apache.commons.codec.digest.DigestUtils.sha512Hex(password);

	        when(uservice.login(email, hashedPassword)).thenReturn(true);
	        when(uservice.getRoleByEmail(email)).thenReturn("ROLE_USER");

	        Map<String, Object> loginInfo = new HashMap<>();
	        loginInfo.put("email", email);
	        loginInfo.put("pwd", password);

	        ResponseEntity<?> response = ucontrol.login(loginInfo);

	        assertTrue(response.getStatusCode().is2xxSuccessful());
	        Map<String, String> body = (Map<String, String>) response.getBody();
	        assertTrue(body.containsKey("token"));
	        assertTrue(body.get("token").startsWith("eyJ")); // Verifica que parece un JWT
	    }
	    @Test
	    public void testLoginUsuarioNoExiste() {
	        String email = "nonexistent@example.com";
	        String password = "anyPassword";
	        String hashedPassword = org.apache.commons.codec.digest.DigestUtils.sha512Hex(password);

	        // Mock: configurar para lanzar la excepción esperada
	        when(uservice.login(eq(email), eq(hashedPassword)))
	            .thenThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "El usuario no existe o las credenciales son incorrectas."));

	        // Entrada simulada
	        Map<String, Object> loginInfo = new HashMap<>();
	        loginInfo.put("email", email);
	        loginInfo.put("pwd", password);

	        // Ejecutar y verificar el comportamiento
	        try {
	            ucontrol.login(loginInfo);
	            fail("Se esperaba una ResponseStatusException pero no se lanzó ninguna excepción."); // Solo se ejecuta si falla
	        } catch (ResponseStatusException e) {
	            // Validar la excepción
	            assertEquals(HttpStatus.FORBIDDEN, e.getStatusCode());
	            assertEquals("El usuario no existe o las credenciales son incorrectas.", e.getReason());
	        }
	    }

	    // --------------------------
	    // Pruebas de 2FA
	    // --------------------------

	    @Test
	    public void testActivar2FASuccess() {
	        String email = "user@example.com";
	        String secretKey = "SAMPLE_SECRET_KEY";

	        when(uservice.activar2FA(email)).thenReturn(secretKey);

	        Map<String, String> info = new HashMap<>();
	        info.put("email", email);

	        String result = ucontrol.activar2FA(info);

	        assertEquals(secretKey, result);
	        verify(uservice, times(1)).activar2FA(email);
	    }

	    @Test
	    public void testVerificar2FACorrecto() {
	        String email = "user@example.com";
	        int code = 123456;

	        when(uservice.verificarTwoFactorAuthCode(email, code)).thenReturn(true);

	        Map<String, Object> info = new HashMap<>();
	        info.put("email", email);
	        info.put("authCode", code);

	        ResponseEntity<Map<String, String>> response = ucontrol.verificarTwoFactorAuth(info);

	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertEquals("Autenticación de doble factor exitosa", response.getBody().get("message"));
	    }
	    @Test
	    public void testVerificar2FAFallido() {
	        String email = "test@example.com";
	        Integer authCode = 123456;

	        // Mock: configurar para lanzar la excepción esperada
	        when(uservice.verificarTwoFactorAuthCode(eq(email), eq(authCode)))
	            .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Código de autenticación incorrecto."));

	        // Entrada simulada
	        Map<String, Object> requestBody = new HashMap<>();
	        requestBody.put("email", email);
	        requestBody.put("authCode", authCode);

	        // Ejecutar y verificar el comportamiento
	        try {
	            ucontrol.verificarTwoFactorAuth(requestBody);
	            fail("Se esperaba una ResponseStatusException pero no se lanzó ninguna excepción."); // Solo se ejecuta si falla
	        } catch (ResponseStatusException e) {
	            // Validar la excepción
	            assertEquals(HttpStatus.UNAUTHORIZED, e.getStatusCode());
	            assertEquals("Código de autenticación incorrecto.", e.getReason());
	        }
	    }

	    @Test
	    public void testDesactivar2FA() {
	        String email = "user@example.com";
	        String secretKey = "SAMPLE_SECRET_KEY";
	        boolean twoFA = false;

	        doNothing().when(uservice).desactivar2FA(email, secretKey, twoFA);

	        Map<String, Object> updateData = new HashMap<>();
	        updateData.put("email", email);
	        updateData.put("clavesecreta", secretKey);
	        updateData.put("twoFA", twoFA);

	        assertDoesNotThrow(() -> ucontrol.desactivarTwoFA(updateData));
	        verify(uservice, times(1)).desactivar2FA(email, secretKey, twoFA);
	    }

	    // --------------------------
	    // Pruebas de Modificar Administrador
	    // --------------------------

	    @Test
	    @WithMockUser(username = "admin", roles = {"ADMIN"})
	    public void testModificarAdministradorExito() {
	        Administrador admin = new Administrador();
	        admin.setEmail("admin@example.com");
	        admin.setNombre("Nuevo Nombre Admin");

	        doNothing().when(adService).actualizarAdministrador(admin.getEmail(), admin);

	        assertDoesNotThrow(() -> adController.modificarAdministrador(admin));
	    }

	    @Test
	    @WithMockUser(username = "admin", roles = {"ADMIN"})
	    public void testModificarAdministradorNoExistente() {
	        Administrador admin = new Administrador();
	        admin.setEmail("inexistente_admin@example.com");

	        doThrow(new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No existe el administrador seleccionado"))
	                .when(adService).actualizarAdministrador(admin.getEmail(), admin);

	        assertThrows(ResponseStatusException.class, () -> adController.modificarAdministrador(admin));
	    }
	}
