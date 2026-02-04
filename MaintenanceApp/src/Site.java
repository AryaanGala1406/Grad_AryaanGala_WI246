abstract class Site {

    protected int siteId;
    protected int length;
    protected int width;
    protected boolean occupied;
    protected String type; // Villa, Apartment, etc
    protected int ownerId;

    protected String ownerName;

    Site(int length, int width, String type, boolean occupied) {
        this.length = length;
        this.width = width;
        this.type = type;
    }

    public int getArea() {
        return length * width;
    }
    
    // Utility display
    void displaySite() {
        System.out.println("Site ID        : " + this.siteId);
        System.out.println("Type           : " + this.type);
        System.out.println("Size           : " + this.length + " x " + this.width);
        System.out.println("Occupied       : " + this.occupied);
        System.out.println("Owner Name     : " + this.ownerName);
        System.out.println("Maintenance Rs : " + this.calculateMaintenance());
        System.out.println("----------------------------------");
    }
    
    public abstract int calculateMaintenance();

    // getters & setters

    public void setOwnerName(String name) {
        this.ownerName = name;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getType() {
        return type;
    }

}

class Villa extends Site {
    Villa(int l, int w) {
        super(l, w, "Villa", true);
    }

    public int calculateMaintenance() {
        return getArea() * 9;
    }
}

class Apartment extends Site {
    Apartment(int l, int w) {
        super(l, w, "Apartment", true);
    }

    public int calculateMaintenance() {
        return getArea() * 9;
    }
}

class IndependentHouse extends Site {
    IndependentHouse(int l, int w) {
        super(l, w, "Independent House", true);
    }

    public int calculateMaintenance() {
        return getArea() * 9;
    }
}

class OpenSite extends Site {
    OpenSite(int l, int w) {
        super(l, w, "Open Site", false);
    }

    public int calculateMaintenance() {
        return getArea() * 9;
    }
}

class UpdateRequest {
    int reqId;
    int siteId;
    String newName;
    String status;
}
