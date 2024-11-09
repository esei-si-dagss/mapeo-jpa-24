package es.uvigo.dagss.mapeo.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Proyecto implements Serializable{
    private Long id;

    private String titulo;

    private String descripcion;

    private Date fechaInicio;

    private Date fechaFinalizacion;

    private EstadoActividad estado;

    private Empleado responsable;

    private Departamento departamento;

    @Builder.Default
    private List<Tarea> tareas = new ArrayList<>();
    
    public void anadirTarea(Tarea tarea) {
        tarea.setProyecto(this);
        this.tareas.add(tarea);
    }

    public void eliminarTarea(Tarea tarea) {
        tarea.setProyecto(null);
        this.tareas.remove(tarea);
    }
    

}
