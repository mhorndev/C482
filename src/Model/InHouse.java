package Model;

public class InHouse extends Part {

    private int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }
    
    public InHouse() {
        super();
    }
        
    public void setMachineID(int machineId) {
        this.machineId = machineId;
    }    

    public int getMachineId() {
        return machineId;
    }

}