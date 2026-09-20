package com.uvmove.poc.service;

import com.uvmove.poc.entity.Vehiculo;
import com.uvmove.poc.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventarioService {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Transactional
    public void validarYActualizarEstado(Long idVehiculo) {
        Vehiculo vehiculo = vehiculoRepository.findById(idVehiculo)
            .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado con ID: " + idVehiculo));

        // RN6: Un vehículo con nivel de batería < 10% no puede iniciar un viaje.
        if (vehiculo.getNivelBateria() < 10) {
            throw new IllegalStateException("RN6: El vehículo " + vehiculo.getCodigo() + " tiene batería insuficiente (" + vehiculo.getNivelBateria() + "%). No puede iniciar viaje.");
        }

        // RN7: Un vehículo sólo puede iniciar viaje si su estado actual es exactamente 'Disponible'.
        if (!"Disponible".equals(vehiculo.getEstado())) {
            throw new IllegalStateException("RN7: El vehículo " + vehiculo.getCodigo() + " no se encuentra disponible (Estado actual: " + vehiculo.getEstado() + ").");
        }

        // Actualizar disponibilidad
        vehiculo.setEstado("En_Uso");
        vehiculoRepository.save(vehiculo);
    }
}
