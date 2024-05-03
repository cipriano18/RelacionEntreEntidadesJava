package proyect.main;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataBase {
    private final File employeesFile = new File("employees.txt");
    private final File departmentsFile = new File("departments.txt");
    private List<Employee> employeeList;  // Lista de empleados
    private List<Department> departmentList;  // Lista de departamentos
    private final Scanner scanner = new Scanner(System.in);

    // Cargar empleados desde el archivo
    private List<Employee> loadEmployees() {
        List<Employee> employees = new ArrayList<>();
        if (employeesFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(employeesFile))) {
                while (true) {
                    Employee employee = (Employee) ois.readObject();  // Deserializar Employee

                    // Verificar si el departamento asociado ya está en la lista
                    if (departmentList.stream().noneMatch(d -> d.getID() == employee.getDepartment().getID())) {
                        // Si el departamento no está, agregarlo a la lista de departamentos
                        departmentList.add(employee.getDepartment());
                    }

                    employees.add(employee);  // Agregar el empleado a la lista
                }
            } catch (EOFException eof) {
                // Fin del archivo
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al cargar empleados: " + e.getMessage());
            }
        }
        return employees;
    }

    // Cargar departamentos desde el archivo
    private List<Department> loadDepartments() {
        List<Department> departments = new ArrayList<>();
        if (departmentsFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(departmentsFile))) {
                while (true) {
                    departments.add((Department) ois.readObject());
                }
            } catch (EOFException eof) {
                // Fin del archivo
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al cargar departamentos: " + e.getMessage());
            }
        }
        return departments;
    }

    // Guardar empleados en el archivo
    private void saveEmployees() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(employeesFile))) {
            for (Employee employee : employeeList) {
                oos.writeObject(employee);
            }
        } catch (IOException e) {
            System.err.println("Error al guardar empleados: " + e.getMessage());
        }
    }

    // Guardar departamentos en el archivo
    private void saveDepartments() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(departmentsFile))) {
            for (Department department : departmentList) {
                oos.writeObject(department);
            }
        } catch (IOException e) {
            System.err.println("Error al guardar departamentos: " + e.getMessage());
        }
    }

    public DataBase() {
        // Cargar departamentos y empleados desde el archivo
        this.departmentList = loadDepartments();
        this.employeeList = loadEmployees();
    }

    public void showEmployees() {
        if (employeeList.isEmpty()) {
            System.out.println("La lista de empleados está vacía.");
        } else {
            System.out.println("Lista de empleados:");
            for (Employee employee : employeeList) {
                System.out.println(employee.toString());
            }
        }
    }

    private void findEmployee(String name) {
        boolean found = false;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(employeesFile))) {
            while (true) {
                Employee employee = (Employee) ois.readObject();
                if (employee.getName().equalsIgnoreCase(name)) {
                    System.out.println("Empleado encontrado: " + employee.toString());
                    found = true;
                    break;
                }
            }
        } catch (EOFException e) {
            if (!found) {
                System.out.println("Empleado no encontrado.");
            }
        } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    public void addEmployee() {
         boolean validId = false;
        System.out.println("Ingrese el nombre del empleado:");
        String name = scanner.nextLine();

        System.out.println("Ingrese el salario:");
        int salary = scanner.nextInt();
        scanner.nextLine();  // Limpiar el buffer

        System.out.println("Ingrese el nombre del departamento:");
        String departmentName = scanner.nextLine();

        // Buscar el departamento por nombre
        Department department = departmentList.stream()
            .filter(d -> d.getName().equalsIgnoreCase(departmentName))
            .findFirst()
            .orElse(null);

        if (department == null) {
            System.out.println("Departamento no encontrado. ¿Desea crear uno nuevo? (s/n)");
            String response = scanner.nextLine();

            if (response.equalsIgnoreCase("s")) {
               
                int newDeptId = -1;

                while (!validId) {  // Validar que el ID del nuevo departamento sea único
                    System.out.println("Ingrese el ID del nuevo departamento:");
                    newDeptId = scanner.nextInt();
                    scanner.nextLine();  
                    int validar= newDeptId;
                    boolean idEnUso = departmentList.stream()
                        .anyMatch(d -> d.getID() == validar);  // Verificar si el ID está en uso

                    if (idEnUso) {
                        System.out.println("El ID ingresado ya está en uso. Por favor, ingrese un ID diferente.");
                    } else {
                        validId = true;  // El ID es válido
                    }
                }

                department = new Department(newDeptId, departmentName);  // Crear el nuevo departamento
                departmentList.add(department);  // Agregar el nuevo departamento
                saveDepartments();  // Guardar departamentos
            } else {
                System.out.println("No se puede agregar el empleado sin un departamento.");
                return;  // Salir si el usuario no quiere crear un nuevo departamento
            }
        } else {
            // El departamento ya existe, continuar con el proceso de agregar el empleado
        }
        if (validId) {
             // Crear el nuevo empleado y agregarlo a la lista
        Employee newEmployee = new Employee(name, salary, department);
        employeeList.add(newEmployee);  // Agregar el nuevo empleado
        saveEmployees();  // Guardar empleados

        System.out.println("Empleado agregado con éxito.");
        }
     
    }

    public void menu() {
        int option;
        boolean exit = false;
        while (!exit) {
            System.out.println("MENÚ DE OPCIONES");
            System.out.println("Ingrese 1 para ingresar un empleado");
            System.out.println("Ingrese 2 para mostrar todos los empleados");
            System.out.println("Ingrese 3 para buscar un empleado");
            System.out.println("Ingrese 4 para eliminar un empleado");
            System.out.println("Ingrese 0 para salir");

            option = scanner.nextInt();
            scanner.nextLine();  // Limpiar el buffer

            switch (option) {
                case 1:
                    addEmployee();  // Agregar un empleado
                    break;

                case 2:
                    showEmployees();  // Mostrar empleados
                    break;

                case 3:
                    System.out.println("Ingrese el nombre del empleado para buscar:");
                    String name = scanner.nextLine();
                    findEmployee(name);  // Buscar un empleado por nombre
                    break;

                case 4:
                    System.out.println("Ingrese el nombre del empleado para eliminar:");
                   // String name = scanner.nextLine();
                   
                    break;

                case 0:
                    exit = true;  // Salir del menú
                    break;

                default:
                    System.out.println("Opción no válida.");
                    break;
            }
        }
    }
}