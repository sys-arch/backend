//package com.equipo3.reuneme.model;
//
//import java.util.Date;
//
//public class Ausencia {
//    private Date fechaInicio;
//    private java.time.LocaDate fechaFin;
//    private String motivo;
//
//    public Ausencia(Date fechaInicio, Date fechaFin, String motivo) {
//        // Validamos las fechas al crear la instancia
//        validarFechas(fechaInicio, fechaFin);
//        this.fechaInicio = fechaInicio;
//        this.fechaFin = fechaFin;
//        this.motivo = motivo;
//    }
//
//    // Getters y setters
//    public Date getFechaInicio() {
//        return fechaInicio;
//    }
//
//    public void setFechaInicio(Date fechaInicio) {
//        // Validamos la fecha al modificarla
//        validarFechas(fechaInicio, fechaFin);
//        this.fechaInicio = fechaInicio;
//    }
//
//    public Date getFechaFin() {
//        return fechaFin;
//    }
//
//    public void setFechaFin(Date fechaFin) {
//        // Validamos la fecha al modificarla
//        validarFechas(fechaInicio, fechaFin);
//        this.fechaFin = fechaFin;
//    }
//
//    public String getMotivo() {
//        return motivo;
//    }
//
//    public void setMotivo(String motivo) {
//        this.motivo = motivo;
//    }
//
//    // Método privado para validar las fechas
//    private void validarFechas(Date fechaInicio, Date fechaFin) {
//        Date fechaActual = new Date();
//        if (fechaInicio.before(fechaActual)) {
//            throw new IllegalArgumentException("La fecha de inicio no puede ser anterior a la fecha actual.");
//        }
//        if (fechaFin.before(fechaInicio) || fechaFin.before(fechaActual)) {
//            throw new IllegalArgumentException("La fecha de fin debe ser igual o posterior a la fecha de inicio y a la fecha actual.");
//        }
//    }
//}
