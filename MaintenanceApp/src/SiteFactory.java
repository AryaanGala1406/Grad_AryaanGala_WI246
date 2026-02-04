public class SiteFactory {

    public static Site create(int id, int l, int w, String type, int ownerId) {

        Site s;
        switch (type) {
            case "Villa" -> s = new Villa(l, w);
            case "Apartment" -> s = new Apartment(l, w);
            case "Independent House" -> s = new IndependentHouse(l, w);
            case "Open Site" -> s = new OpenSite(l, w);
            default -> throw new IllegalArgumentException();
        }

        s.siteId = id;
        s.ownerId = ownerId;
        return s;
    }
}
