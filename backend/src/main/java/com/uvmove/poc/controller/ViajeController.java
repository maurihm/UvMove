package com.uvmove.poc.controller;

import com.uvmove.poc.entity.Viaje;
import com.uvmove.poc.service.ControlViajesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/viajes")
@CrossOrigin(origins = "*") // Permitir llamadas desde el frontend en desarrollo
public class ViajeController {

    @Autowired
    private ControlViajesService controlViajesService;

    @PostMapping("/iniciar")
    public ResponseEntity<?> iniciarViaje(@RequestParam Long idVehiculo, @RequestParam String usuarioId) {
        try {
            Viaje viaje = controlViajesService.iniciarViaje(idVehiculo, usuarioId);
            return ResponseEntity.ok("Viaje iniciado exitosamente con ID: " + viaje.getIdViaje());
        } catch (IllegalStateException e) {
            // Regla de Negocio violada (RN6 o RN7)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error (Regla de Negocio): " + e.getMessage());
        } catch (IllegalArgumentException e) {
            // Vehículo no encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (Exception e) {
            // Otro error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }
}
