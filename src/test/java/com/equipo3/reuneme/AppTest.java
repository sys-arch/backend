package com.equipo3.reuneme;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import com.equipo3.reuneme.controller.UsuarioController;
import com.equipo3.reuneme.service.UsuarioService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AppTest {

    @Autowired
    private UsuarioController ucontrol;

    @MockBean
    private UsuarioService uservice;

    /*@Test
    public void testLoginBien() {
        String email = "guille@gmail.com";
        String password = "Aa123456?";

        // Simulamos un token válido, ya sea "a-" o "e-" seguido de un UUID
        when(uservice.login(email, password)).thenReturn("a-" + UUID.randomUUID().toString());

        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("email", email);
        loginInfo.put("pwd", password);

        String response = ucontrol.login(loginInfo);

        // Validamos que el token empiece con "a-" o "e-" y que lo que sigue sea un UUID válido
        assertTrue(response.matches("^(a|e)-[0-9a-f]{8}-[0-9a-f]{4}-[4][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$"));
    }

    @Test
    public void testLoginNoExisteUsuario() {
        String email = "nonexistent@example.com";
        String password = "anyPassword";

        when(uservice.login(email, password)).thenThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "El usuario no existe o las credenciales son incorrectas."));

        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("email", email);
        loginInfo.put("pwd", password);

        assertThrows(ResponseStatusException.class, () -> {
            ucontrol.login(loginInfo);
        });
    }
    
    @Test
    public void testLoginPwdMal() {
        String email = "guille@gmail.com";
        String password = "1234";

        // Simulamos que el servicio lanza la excepción al recibir credenciales inválidas
        when(uservice.login(email, password)).thenThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciales inválidas."));

        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("email", email);
        loginInfo.put("pwd", password);

        // Capturamos la excepción esperada
        assertThrows(ResponseStatusException.class, () -> {
            ucontrol.login(loginInfo);
        });
    }*/

}
