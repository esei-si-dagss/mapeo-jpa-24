package es.uvigo.dagss.mapeo.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Tarea implements Serializable{
    private Long id;

    private String nombre;

    private String descripcion;

    private Date fechaInicio;

    private Date fechaFinalizacion;

    private EstadoActividad estado;

    @ToString.Exclude // Evita bucle en toString() generado por Loombook
    private Proyecto proyecto;

    private Empleado responsable;

    @Builder.Default
    private List<Empleado> empleados = new ArrayList<>();
    

    public void anadirEmpleado(Empleado empleado) {
        this.empleados.add(empleado);
    }

    public void eliminarEmpleado(Empleado empleado) {
        this.empleados.remove(empleado);
    }   

}
