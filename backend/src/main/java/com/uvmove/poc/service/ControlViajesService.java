package com.uvmove.poc.service;

import com.uvmove.poc.entity.Viaje;
import com.uvmove.poc.repository.ViajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ControlViajesService {

    @Autowired
    private InventarioService inventarioService;

    @Autowired
    private ViajeRepository viajeRepository;

    @Transactional
    public Viaje iniciarViaje(Long idVehiculo, String usuarioId) {
        // Contrato entre módulos: solicitar validación y actualización de estado
        // Si hay algún problema (batería, estado no disponible), lanzará excepción y hará rollback
        inventarioService.validarYActualizarEstado(idVehiculo);
        
        // Si la validación es exitosa, se procede a guardar el viaje
        Viaje nuevoViaje = new Viaje();
        nuevoViaje.setIdVehiculo(idVehiculo);
        nuevoViaje.setUsuarioId(usuarioId);
        nuevoViaje.setFechaInicio(LocalDateTime.now());
        nuevoViaje.setEstadoViaje("En_Curso");
        
        return viajeRepository.save(nuevoViaje);
    }
}
