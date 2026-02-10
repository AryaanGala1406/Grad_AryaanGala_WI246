public class SiteFactory {

    public static Site create(int id, int l, int w, String type, int ownerId) {

        SiteType siteType = SiteType.fromDb(type);  // normalize + validate

        Site s = switch (siteType) {
            case VILLA -> new Villa(l, w);
            case APARTMENT -> new Apartment(l, w);
            case INDEPENDENT_HOUSE -> new IndependentHouse(l, w);
            case OPEN_SITE -> new OpenSite(l, w);
        };

        s.setSiteId(id);
        s.setOwnerId(ownerId);

        return s;
    }
}
