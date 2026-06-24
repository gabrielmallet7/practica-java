package org.example;

import org.example.dto.ProductoDTO;
import org.example.services.ProductoService;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ProductoService productoService = ProductoService.getInstance();

    public static void main(String[] args) {
        boolean continuar = true;

        while (continuar) {
            mostrarMenu();
            int opcion = obtenerOpcion();

            switch (opcion) {
                case 1 -> crearProducto();
                case 2 -> mostrarProductos();
                case 3 -> actualizarProducto();
                case 4 -> eliminarProducto();
                case 5 -> continuar = false;
                default -> System.out.println("Opción no válida");
            }
        }

        System.out.println("¡Hasta luego!");
    }

    private static void mostrarMenu() {
        System.out.println("\n=== Gestión de Productos ===");
        System.out.println("1. Crear producto");
        System.out.println("2. Mostrar productos");
        System.out.println("3. Actualizar producto");
        System.out.println("4. Eliminar producto");
        System.out.println("5. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static int obtenerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void crearProducto() {
        System.out.println("\n=== Crear Producto ===");

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Stock: ");
        int stock = Integer.parseInt(scanner.nextLine());

        System.out.print("¿Está activo? (true/false): ");
        boolean esActivo = Boolean.parseBoolean(scanner.nextLine());

        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setNombre(nombre);
        productoDTO.setStock(stock);
        productoDTO.setActivo(esActivo);

        try {
            productoDTO = productoService.crearProducto(productoDTO);
            System.out.println("Producto creado exitosamente con ID: " + productoDTO.getId());
        } catch (Exception e) {
            System.out.println("Error al crear el producto: " + e.getMessage());
        }
    }

    private static void mostrarProductos() {
        System.out.println("\n=== Lista de Productos ===");
        List<ProductoDTO> productos = productoService.obtenerTodosLosProductos();

        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }

        for (ProductoDTO producto : productos) {
            System.out.println("\nID: " + producto.getId());
            System.out.println("Nombre: " + producto.getNombre());
            System.out.println("Stock: " + producto.getStock());
            System.out.println("Activo: " + producto.isActivo());
        }
    }

    private static void actualizarProducto() {
        System.out.println("\n=== Actualizar Producto ===");

        System.out.print("ID del producto a actualizar: ");
        Integer id = Integer.parseInt(scanner.nextLine());

        ProductoDTO productoDTO = productoService.obtenerProducto(id);
        if (productoDTO == null) {
            System.out.println("Producto no encontrado.");
            return;
        }

        System.out.print("Nuevo nombre (actual: " + productoDTO.getNombre() + "): ");
        String nombre = scanner.nextLine();
        if (!nombre.isEmpty()) {
            productoDTO.setNombre(nombre);
        }

        System.out.print("Nuevo stock (actual: " + productoDTO.getStock() + "): ");
        String stockStr = scanner.nextLine();
        if (!stockStr.isEmpty()) {
            productoDTO.setStock(Integer.parseInt(stockStr));
        }

        System.out.print("¿Está activo? (actual: " + productoDTO.isActivo() + ", true/false): ");
        String activoStr = scanner.nextLine();
        if (!activoStr.isEmpty()) {
            productoDTO.setActivo(Boolean.parseBoolean(activoStr));
        }

        try {
            productoDTO = productoService.actualizarProducto(productoDTO);
            System.out.println("Producto actualizado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al actualizar el producto: " + e.getMessage());
        }
    }

    private static void eliminarProducto() {
        System.out.println("\n=== Eliminar Producto ===");

        System.out.print("ID del producto a eliminar: ");
        Integer id = Integer.parseInt(scanner.nextLine());

        try {
            productoService.eliminarProducto(id);
            System.out.println("Producto eliminado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al eliminar el producto: " + e.getMessage());
        }
    }
}
