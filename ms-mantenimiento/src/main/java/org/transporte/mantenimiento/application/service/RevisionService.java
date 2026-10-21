package org.transporte.mantenimiento.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.transporte.mantenimiento.application.port.in.RevisionUseCase;
import org.transporte.mantenimiento.application.port.out.OdometroOutService;
import org.transporte.mantenimiento.application.port.out.RevisionOutService;
import org.transporte.mantenimiento.domian.bean.Odometro;
import org.transporte.mantenimiento.domian.bean.Revision;
import org.transporte.mantenimiento.domian.constraints.RevisionConstraint;
import org.transporte.mantenimiento.domian.enums.EstadoRevision;
import org.transporte.mantenimiento.domian.model.RecursoNoEncontradoException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RevisionService implements RevisionUseCase {

    private final RevisionOutService revisionOutService;
    private final OdometroOutService odometroOutService;
    private final RevisionConstraint revisionConstraint;

    @Override
    @Transactional
    public Revision programar(Revision revision) {
        revisionConstraint.validarProgramacion(revision);
        revision.setId(null);
        revision.setEstado(EstadoRevision.PENDIENTE);

        Double kilometrajeActual = odometroOutService.buscarPorVehiculoId(revision.getVehiculoId())
                .map(Odometro::getKilometrajeAcumulado)
                .orElse(null);
        if (revisionConstraint.estaVencida(revision, kilometrajeActual)) {
            revision.setEstado(EstadoRevision.VENCIDA);
        }
        return revisionOutService.guardar(revision);
    }

    @Override
    @Transactional
    public Revision marcarRealizada(Long id) {
        Revision revision = revisionOutService.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la revision con id " + id));
        revisionConstraint.validarRealizable(revision);
        revision.setEstado(EstadoRevision.REALIZADA);
        return revisionOutService.guardar(revision);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Revision> listarPorVehiculo(Long vehiculoId) {
        return revisionOutService.listarPorVehiculo(vehiculoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Revision> listarVencidas() {
        return revisionOutService.listarPorEstado(EstadoRevision.VENCIDA);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        revisionOutService.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la revision con id " + id));
        revisionOutService.eliminar(id);
    }
}
