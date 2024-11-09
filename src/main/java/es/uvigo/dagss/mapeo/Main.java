package es.uvigo.dagss.mapeo;

import java.util.List;
import es.uvigo.dagss.mapeo.entidades.Departamento;
import es.uvigo.dagss.mapeo.entidades.Empleado;
import es.uvigo.dagss.mapeo.entidades.Proyecto;
import es.uvigo.dagss.mapeo.entidades.Tarea;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class Main {
    public static final void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("mapeo_PU");
        EntityManager em;

        //// Crear e insertar entidades (ya hecho)
        //em = emf.createEntityManager();
        //Auxiliar.crearEntidadesIniciales(em);
        //em.close();

        // Volcar datos de la BD
        em = emf.createEntityManager();
        pruebaConsultaEmpleados(emf.createEntityManager());
        pruebaConsultaProyectos(emf.createEntityManager());
        em.close();

        emf.close();
        System.exit(0);
    }

    private static void pruebaConsultaEmpleados(EntityManager em) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            TypedQuery<Departamento> queryDepartamentos = em.createQuery("SELECT d FROM Departamento AS d", Departamento.class);
            List<Departamento> departamentos = queryDepartamentos.getResultList();

            System.out.println("[PRUEBA JPA]: --------------------------------------");
            System.out.println("[PRUEBA JPA]: Listado de departamentos con empleados");
            System.out.println("[PRUEBA JPA]: --------------------------------------");
            for (Departamento d : departamentos) {
                System.out.printf("[PRUEBA JPA]: DEPARTAMENTO (%d) %s, Jefe: %s\n", d.getId(), d.getNombre(), d.getJefeDepartamento().getNombre());
                TypedQuery<Empleado> queryEmpleados = em.createQuery("SELECT e FROM Empleado AS e WHERE e.departamento.id = :idDepto", Empleado.class);
                queryEmpleados.setParameter("idDepto", d.getId());    
                List<Empleado> empleados = queryEmpleados.getResultList();
                for (Empleado e : empleados){
                    System.out.printf("[PRUEBA JPA]:   %s [%s]  (%s)\n", e.getNombre(), e.getCategoria(), e.getDni());
                }
                System.out.println("[PRUEBA JPA]:");
            }
            System.out.println("[PRUEBA JPA]: --------------------------------------\n");

            tx.commit();

        } catch (Exception e) {
            System.err.println("Error en pruebaConsultaEmpleados");
            e.printStackTrace(System.err);

            if ((tx != null) && tx.isActive()) {
                tx.rollback();
            }
        }
    }

    private static void pruebaConsultaProyectos(EntityManager em) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            TypedQuery<Proyecto> query = em.createQuery("SELECT p FROM Proyecto as p", Proyecto.class);
            List<Proyecto> proyectos = query.getResultList();

            System.out.println("[PRUEBA JPA]: --------------------------------------");
            System.out.println("[PRUEBA JPA]: Listado de proyectos con tareas");
            System.out.println("[PRUEBA JPA]: --------------------------------------");
            for (Proyecto p : proyectos) {
                System.out.printf("[PRUEBA JPA]: PROYECTO %s (%d) -> Estado: %s, Responsable: %s\n", p.getTitulo(), p.getId(), p.getEstado(), p.getResponsable().getNombre());
                for (Tarea t : p.getTareas()) {
                    System.out.printf("[PRUEBA JPA]:   Tarea: %s (%d) -> Estado: %s\n", t.getNombre(), t.getId(), t.getEstado());
                    for (Empleado e: t.getEmpleados()) {
                        System.out.printf("[PRUEBA JPA]:      - %s [%s]\n", e.getNombre(), e.getDepartamento().getNombre());
                    }
                }
                System.out.println("[PRUEBA JPA]:");
            }
            System.out.println("[PRUEBA JPA]: --------------------------------------\n");

            tx.commit();

        } catch (Exception e) {
            System.err.println("Error en pruebaConsultaProyectos");
            e.printStackTrace(System.err);

            if ((tx != null) && tx.isActive()) {
                tx.rollback();
            }
        }
    }

}
