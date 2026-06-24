/*package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import org.example.dto.CreditosNoFinalizadosDTO;
import org.example.dto.CreditosPagadosDTO;
import org.example.services.CreditService;
import org.example.models.CreditType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final CreditService logica = CreditService.getInstance();
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        boolean continuar = true;

        while (continuar) {
            mostrarMenu();
            int opcion = obtenerOpcion();

            switch (opcion) {
                case 1 -> registrarPago();
                case 2 -> buscarCreditos();
                case 3 -> resumenCreditosPagados();
                case 4 -> listarCreditosNoFinalizados();
                case 5 -> continuar = false;
                default -> System.out.println("Opcion no valida");
            }
        }

        System.out.println("Hasta luego!");
    }

    private static void mostrarMenu() {
        System.out.println("\n=== Gestion de Creditos ===");
        System.out.println("1. Registrar pago y actualizar estado");
        System.out.println("2. Buscar creditos con filtros");
        System.out.println("3. Resumen de creditos pagados (DTO)");
        System.out.println("4. Listado de creditos no finalizados (DTO)");
        System.out.println("5. Salir");
        System.out.print("Seleccione una opcion: ");
    }

    private static int obtenerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void registrarPago() {
        System.out.println("\n=== Registrar Pago ===");
        System.out.print("ID del credito: ");
        Long idCredito = Long.parseLong(scanner.nextLine());

        System.out.print("Monto del pago: ");
        double monto = Double.parseDouble(scanner.nextLine());

        try {
            logica.pagarCredito(idCredito, monto);
            System.out.println("Pago registrado y estado actualizado correctamente.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void buscarCreditos() {
        System.out.println("\n=== Buscar Creditos ===");
        System.out.print("Nombre del cliente: ");
        String cliente = scanner.nextLine();

        System.out.print("Tipo de credito (PERSONAL/BUSINESS o ENTER para omitir): ");
        String tipo = scanner.nextLine().trim();
        CreditType tipoFinal = tipo.isEmpty() ? null : CreditType.valueOf(tipo.toUpperCase());

        System.out.print("Fecha desde (yyyy-MM-dd o ENTER): ");
        String desdeStr = scanner.nextLine().trim();
        LocalDate desde = desdeStr.isEmpty() ? null : LocalDate.parse(desdeStr, dateFormatter);

        System.out.print("Fecha hasta (yyyy-MM-dd o ENTER): ");
        String hastaStr = scanner.nextLine().trim();
        LocalDate hasta = hastaStr.isEmpty() ? null : LocalDate.parse(hastaStr, dateFormatter);

        System.out.print("Monto minimo o ENTER: ");
        String minStr = scanner.nextLine().trim();
        Double montoMin = minStr.isEmpty() ? null : Double.parseDouble(minStr);

        System.out.print("Monto maximo o ENTER: ");
        String maxStr = scanner.nextLine().trim();
        Double montoMax = maxStr.isEmpty() ? null : Double.parseDouble(maxStr);

        logica.obtenerCreditos(cliente, tipoFinal, desde, hasta, montoMin, montoMax).forEach(System.out::println);
    }

    private static void resumenCreditosPagados() {
        System.out.println("\n=== Resumen de Creditos Pagados ===");
        System.out.print("Fecha desde (yyyy-MM-dd): ");
        LocalDate desde = LocalDate.parse(scanner.nextLine(), dateFormatter);

        System.out.print("Fecha hasta (yyyy-MM-dd): ");
        LocalDate hasta = LocalDate.parse(scanner.nextLine(), dateFormatter);

        List<CreditosPagadosDTO> resumen = logica.obtenerCreditosPagados(desde, hasta);
        resumen.forEach(System.out::println);
    }

    private static void listarCreditosNoFinalizados() {
        System.out.println("\n=== Creditos No Finalizados ===");
        List<CreditosNoFinalizadosDTO> lista = logica.obtenerCreditosNoFinalizados();
        lista.forEach(System.out::println);
    }
}*/
