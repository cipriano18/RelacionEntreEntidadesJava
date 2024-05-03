package proyect.main;
import java.io.Serializable;

public class Department implements Serializable{
    private int ID;
    private String name;
   private static final long serialVersionUID = 5378882624469897474L;  // Para consistencia
    public Department(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ID =" + ID + " name=" + name ;
    }
}
