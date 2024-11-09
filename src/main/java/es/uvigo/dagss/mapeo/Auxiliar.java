package es.uvigo.dagss.mapeo;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.uvigo.dagss.mapeo.entidades.CategoriaLaboral;
import es.uvigo.dagss.mapeo.entidades.Departamento;
import es.uvigo.dagss.mapeo.entidades.Empleado;
import es.uvigo.dagss.mapeo.entidades.EstadoActividad;
import es.uvigo.dagss.mapeo.entidades.Proyecto;
import es.uvigo.dagss.mapeo.entidades.Tarea;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class Auxiliar {
    public static final void crearEntidadesIniciales(EntityManager em) {
        EntityTransaction tx = em.getTransaction();

        Map<String, List<String>> empleados_por_departamento = Map.of(
                "Ejercito de Dumbledore", List.of("Harry Potter", "Hermione Granger", "Ron Weasley", "Luna Lovegood"),
                "Claustro de Hogwarts", List.of("Albus Dumbledore", "Minerva McGonagall", "Rubius Hagrid"),
                "Mortífagos", List.of("Voldemort", "Bellatrix Lestrange", "Lucius Malfoy", "Barty Crouch Jr."));

        Map<String, String> proyectos_por_departamento = Map.of(
                "Defensa del mundo mágico", "Claustro de Hogwarts",
                "Conquista del mundo", "Mortífagos");

        Map<String, List<String>> tareas_por_proyecto = Map.of(
                "Defensa del mundo mágico",
                List.of("Búsqueda de horrocruxes", "Defensa del castillo", "Matar a Voldemort"),
                "Conquista del mundo",
                List.of("Reclutar adeptos", "Matar a Harry Potter", "Revivir a Voldemort", "Atacar el castillo"));

        Map<String, String> responsable_por_proyecto = Map.of(
                "Defensa del mundo mágico", "Albus Dumbledore",
                "Conquista del mundo", "Voldemort");

        Map<String, List<String>> empleados_por_tarea = Map.of(
                "Búsqueda de horrocruxes", List.of("Harry Potter", "Hermione Granger", "Ron Weasley"),
                "Defensa del castillo", List.of("Minerva McGonagall", "Rubius Hagrid", "Luna Lovegood"),
                "Matar a Voldemort", List.of("Harry Potter", "Albus Dumbledore"),
                "Reclutar adeptos", List.of("Bellatrix Lestrange", "Lucius Malfoy", "Barty Crouch Jr."),
                "Matar a Harry Potter", List.of("Voldemort"),
                "Revivir a Voldemort", List.of("Barty Crouch Jr."),
                "Atacar el castillo", List.of("Voldemort", "Bellatrix Lestrange", "Lucius Malfoy", "Barty Crouch Jr."));

        Map<String, Empleado> empleados = new HashMap<>();
        Map<String, Departamento> departamentos = new HashMap<>();
        Map<String, Proyecto> proyectos = new HashMap<>();
        Map<String, Tarea> tareas = new HashMap<>();

        try {
            tx.begin();

            for (String nombreDepartamento : empleados_por_departamento.keySet()) {
                Departamento departamento = Departamento.builder().nombre(nombreDepartamento)
                        .descripcion("Descripcion del departamento " + nombreDepartamento)
                        .ubicacion("Ubicacion del departamento " + nombreDepartamento)
                        .build();
                em.persist(departamento);
                departamentos.put(nombreDepartamento, departamento);

                String baseDNI = Long.toString(departamento.getId() % 10).repeat(8);
                int contador = 0;
                for (String nombreEmpleado : empleados_por_departamento.get(nombreDepartamento)) {
                    String dni = baseDNI + ((char) ('A' + contador));
                    Empleado empleado = em.find(Empleado.class, dni);
                    if (empleado == null) {
                        CategoriaLaboral categoria = (nombreDepartamento.equals("Ejercito de Dumbledore")) ? CategoriaLaboral.BECARIO : CategoriaLaboral.SENIOR;
                        if (contador == 0) { // Primer empleado => jefe del depto
                            categoria = CategoriaLaboral.JEFE_DEPARTAMENTO;
                        }
                        empleado = Empleado.builder().dni(dni)
                                .nombre(nombreEmpleado)
                                .categoria(categoria)
                                .departamento(departamento)
                                .build();
                        em.persist(empleado);
                        empleados.put(nombreEmpleado, empleado);

                        if (contador == 0) { // Vincular con jefe de departamentp una vez creado
                            departamento.setJefeDepartamento(empleado);
                            departamento = em.merge(departamento);
                            departamentos.put(nombreDepartamento, departamento);
                        }
                    }
                    contador++;
                }
            }

            for (String nombreProyecto : proyectos_por_departamento.keySet()) {
                Proyecto proyecto = Proyecto.builder().titulo(nombreProyecto)
                        .descripcion("Descripción de " + nombreProyecto)
                        .estado(EstadoActividad.EN_CURSO)
                        .fechaInicio(Date.from(Instant.now().minus(6 * 30, ChronoUnit.DAYS)))
                        .fechaFinalizacion(Date.from(Instant.now()))
                        .departamento(departamentos.get(proyectos_por_departamento.get(nombreProyecto)))
                        .responsable(empleados.get(responsable_por_proyecto.get(nombreProyecto)))
                        .build();
                em.persist(proyecto);
                proyectos.put(nombreProyecto, proyecto);

                // Crear y enlazar tareas
                for (String nombreTarea : tareas_por_proyecto.get(nombreProyecto)) {
                    Tarea tarea = Tarea.builder().nombre(nombreTarea)
                            .descripcion("Descripción de la tarea " + nombreTarea)
                            .proyecto(proyecto)
                            .estado(EstadoActividad.EN_CURSO)
                            .fechaInicio(Date.from(Instant.now().minus(6 * 30, ChronoUnit.DAYS)))
                            .fechaFinalizacion(Date.from(Instant.now()))
                            .build();
                    for (String nombreEmpleado : empleados_por_tarea.get(nombreTarea)) {
                        tarea.anadirEmpleado(empleados.get(nombreEmpleado));
                    }
                    // Primer empleado de la lista como responsable
                    tarea.setResponsable(empleados.get(empleados_por_tarea.get(nombreTarea).get(0)));

                    em.persist(tarea);
                    proyecto.anadirTarea(tarea);

                    tareas.put(nombreTarea, tarea);
                }
                // Atualizar enlace bidireccional de tareas
                proyecto = em.merge(proyecto);
                proyectos.put(nombreProyecto, proyecto);
            }

            tx.commit();  // Fuerza em.flush() implicito

        } catch (Exception e) {
            System.err.println("Error en pruebaCrearEntidades");
            e.printStackTrace(System.err);

            if ((tx != null) && tx.isActive()) {
                tx.rollback();
            }
        }
    }

}
