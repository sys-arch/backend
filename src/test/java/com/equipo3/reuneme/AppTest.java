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

import com.equipo3.reuneme.controller.AdminController;
import com.equipo3.reuneme.controller.UsuarioController;
import com.equipo3.reuneme.model.Administrador;
import com.equipo3.reuneme.model.Empleado;
import com.equipo3.reuneme.service.AdminService;
import com.equipo3.reuneme.service.UsuarioService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AppTest {

    @Autowired
    private UsuarioController ucontrol;
    
    @Autowired
    private AdminController adController;

    @MockBean
    private UsuarioService uservice;
    
    @MockBean
    private AdminService adService;

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
    
    /************ TESTS PARA MODIFICAR EMPLEADO ************/

    /*@Test
    public void testModificarEmpleadoExito() {
        Empleado empleadoActualizado = new Empleado();
        empleadoActualizado.setEmail("empleado@example.com");
        empleadoActualizado.setNombre("Nuevo Nombre");

        // Simulamos que el servicio actualiza correctamente
        when(adService.actualizarEmpleado(empleadoActualizado.getEmail(), empleadoActualizado)).thenReturn(empleadoActualizado);

        // No esperamos excepción en este caso exitoso
        adController.modificarEmpleado(empleadoActualizado);
    }

    @Test
    public void testModificarEmpleadoNoExistente() {
        Empleado empleadoActualizado = new Empleado();
        empleadoActualizado.setEmail("inexistente@example.com");

        // Simulamos que el servicio lanza excepción al no encontrar el empleado
        when(adService.actualizarEmpleado(empleadoActualizado.getEmail(), empleadoActualizado))
            .thenThrow(new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No existe el empleado seleccionado"));

        // Validamos que se lance la excepción esperada
        assertThrows(ResponseStatusException.class, () -> {
            adController.modificarEmpleado(empleadoActualizado);
        });
    }

    @Test
    public void testModificarEmpleadoFormatoIncorrecto() {
        Empleado empleadoActualizado = new Empleado();
        empleadoActualizado.setEmail("empleado_malformateado");

        // Simulamos que el servicio lanza excepción por email mal formado
        when(adService.actualizarEmpleado(empleadoActualizado.getEmail(), empleadoActualizado))
            .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email no tiene un formato válido"));

        // Validamos que se lance la excepción esperada
        assertThrows(ResponseStatusException.class, () -> {
            adController.modificarEmpleado(empleadoActualizado);
        });
    }*/

    /************ TESTS PARA MODIFICAR ADMINISTRADOR ************/

    /*@Test
    public void testModificarAdministradorExito() {
        Administrador administradorActualizado = new Administrador();
        administradorActualizado.setEmail("admin@example.com");
        administradorActualizado.setNombre("Nuevo Nombre Admin");

        // Simulamos que el servicio actualiza correctamente
        when(adService.actualizarAdministrador(administradorActualizado.getEmail(), administradorActualizado)).thenReturn(administradorActualizado);

        // No esperamos excepción en este caso exitoso
        adController.modificarAdministrador(administradorActualizado);
    }

    @Test
    public void testModificarAdministradorNoExistente() {
        Administrador administradorActualizado = new Administrador();
        administradorActualizado.setEmail("inexistente_admin@example.com");

        // Simulamos que el servicio lanza excepción al no encontrar el administrador
        when(adService.actualizarAdministrador(administradorActualizado.getEmail(), administradorActualizado))
            .thenThrow(new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No existe el administrador seleccionado"));

        // Validamos que se lance la excepción esperada
        assertThrows(ResponseStatusException.class, () -> {
            adController.modificarAdministrador(administradorActualizado);
        });
    }

    @Test
    public void testModificarAdministradorFormatoIncorrecto() {
        Administrador administradorActualizado = new Administrador();
        administradorActualizado.setEmail("admin_malformateado");

        // Simulamos que el servicio lanza excepción por email mal formado
        when(adService.actualizarAdministrador(administradorActualizado.getEmail(), administradorActualizado))
            .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email no tiene un formato válido"));

        // Validamos que se lance la excepción esperada
        assertThrows(ResponseStatusException.class, () -> {
            adController.modificarAdministrador(administradorActualizado);
        });
    }*/

}
