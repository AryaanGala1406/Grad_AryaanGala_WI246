enum SiteType {
    VILLA, APARTMENT, INDEPENDENT_HOUSE, OPEN_SITE;

    public static SiteType fromDb(String dbValue) {
        if (dbValue == null)
            throw new IllegalArgumentException("Site type is null");
        String normalized = dbValue.trim()
                .toUpperCase()
                .replace(' ', '_');
        return SiteType.valueOf(normalized);
    }
}

public abstract class Site {

    protected int siteId;
    protected int length;
    protected int width;
    protected SiteType type;
    protected int ownerId;
    protected String ownerName;

    protected Site(int length, int width, SiteType type) {
        this.length = length;
        this.width = width;
        this.type = type;
    }

    public int getArea() {
        return length * width;
    }
    
    public int getSiteId() {
        return siteId;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public SiteType getType() {
        return type;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String name) {
        this.ownerName = name;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
}

class Villa extends Site {
    Villa(int l, int w) {
        super(l, w, SiteType.VILLA);
    }
}

class Apartment extends Site {
    Apartment(int l, int w) {
        super(l, w, SiteType.APARTMENT);
    }
}

class IndependentHouse extends Site {
    IndependentHouse(int l, int w) {
        super(l, w, SiteType.INDEPENDENT_HOUSE);
    }
}

class OpenSite extends Site {
    OpenSite(int l, int w) {
        super(l, w, SiteType.OPEN_SITE);
    }
}
