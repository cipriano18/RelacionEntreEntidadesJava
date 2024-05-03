package proyect.main;

import java.io.Serializable;

public class Employee implements Serializable{
    private String name;
    private int salary;
    private Department department;
   private static final long serialVersionUID = 6641107279781048157L;  // Para consistencia
    public Employee(String name, int salary, Department department) {
        this.name = name;
        this.salary = salary;
        this.department = department;
    }

    @Override
    public String toString() {
        return "Nombre=" + name + ", salario =" + salary + "Departamento " + department ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
    
}
