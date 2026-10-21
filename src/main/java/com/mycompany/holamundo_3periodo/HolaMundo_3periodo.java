package com.mycompany.holamundo_3periodo;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HolaMundo_3periodo {

    public static void main(String[] args) throws SQLException {
        System.out.println("=== Ejecutando operaciones en tabla Alumnos ===");

        Connection cn = ConexionDB.getConexion();

        if (cn == null) {
            System.out.println("No se puede establecer la conexión");
            return;
        }

        PreparedStatement psInsertar = null;

        try {
            System.out.println("\n1. Intentando insertar nuevo Alumno...");

            String psInsert =
                    "DECLARE @nombre NVARCHAR(255) = ?, @correo NVARCHAR(255) = ?; " +
                    "INSERT INTO Alumnos(nombre, correo) " +
                    "SELECT @nombre, @correo " +
                    "WHERE NOT EXISTS (SELECT 1 FROM Alumnos WHERE correo = @correo)";

            psInsertar = cn.prepareStatement(psInsert);
            psInsertar.setString(1, "Carlos Pineda");
            psInsertar.setString(2, "Carlos.Pineda@uth.hn");

            int filasAfectadas = psInsertar.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("¡Alumno insertado con éxito!");
            } else {
                System.out.println("Ya existe un alumno con ese correo");
            }

            System.out.println("\n2. Consultando alumnos...");

            String psSelect = "SELECT id, nombre, correo FROM Alumnos";

            try (PreparedStatement psConsultar = cn.prepareStatement(psSelect); ResultSet rs = psConsultar.executeQuery()) {

                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id")
                            + " | Nombre: " + rs.getString("nombre")
                            + " | Correo: " + rs.getString("correo"));
                }
            }

            System.out.println("\n3. Actualizando alumno...");

            String psUpdate = "UPDATE Alumnos SET nombre = ? WHERE correo = ?";

            try (PreparedStatement psActualizar = cn.prepareStatement(psUpdate)) {
                psActualizar.setString(1, "Fercho Jr Martinez Juan");
                psActualizar.setString(2, "Fercho.Martinez@uth.hn");

                filasAfectadas = psActualizar.executeUpdate();

                if (filasAfectadas > 0) {
                    System.out.println("¡Alumno actualizado con éxito!");
                } else {
                    System.out.println("No se encontró el alumno para actualizar");
                }
            }

            System.out.println("\n4. Eliminando alumno...");

            String psDelete = "DELETE FROM Alumnos WHERE correo = ?";

            try (PreparedStatement psEliminar = cn.prepareStatement(psDelete)) {
                psEliminar.setString(1, "Neymar.Cardona@uth.hn");

                filasAfectadas = psEliminar.executeUpdate();

                if (filasAfectadas > 0) {
                    System.out.println("¡Alumno eliminado con éxito!");
                } else {
                    System.out.println("No se encontró el alumno para eliminar");
                }
            }

        } catch (SQLException e) {
            System.err.println("Ocurrió un error con la base de datos: " + e.getMessage());
        } finally {
            try {
                if (psInsertar != null) {
                    psInsertar.close();
                }
            } finally {
                ConexionDB.cerrarConexion();
            }
        }
    }
}
