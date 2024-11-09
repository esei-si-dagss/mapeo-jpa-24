package es.uvigo.dagss.mapeo.entidades;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Empleado implements Serializable{
    private String dni;

    private String nombre;

    private CategoriaLaboral categoria;

    private Departamento departamento;
}
