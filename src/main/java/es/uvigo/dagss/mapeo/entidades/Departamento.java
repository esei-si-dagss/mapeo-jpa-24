package es.uvigo.dagss.mapeo.entidades;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Departamento implements Serializable{
    private Long id;

    private String nombre;

    private String descripcion;

    private String ubicacion;

    @ToString.Exclude   // Evita bucle en toString() generado por Loombook
    private Empleado jefeDepartamento;
}
