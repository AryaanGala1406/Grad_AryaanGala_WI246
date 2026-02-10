import java.time.OffsetDateTime;

enum ReqType {
    DETAILS, SITE;

    public static ReqType fromDb(String s) {
        if (s == null)
            throw new IllegalArgumentException("req_type cannot be null");
        return switch (s.trim().toUpperCase()) {
            case "DETAILS" -> DETAILS;
            case "SITE" -> SITE;
            default -> throw new IllegalArgumentException("Unknown req_type: " + s);
        };
    }
}

enum RequestStatus {
    PENDING, APPROVED, REJECTED;

    public static RequestStatus fromDb(String s) {
        if (s == null)
            return PENDING; // default in DB
        return switch (s.trim().toUpperCase()) {
            case "PENDING" -> PENDING;
            case "APPROVED" -> APPROVED;
            case "REJECTED" -> REJECTED;
            default -> throw new IllegalArgumentException("Unknown status: " + s);
        };
    }
}

public class Request {

    protected int reqId;
    protected ReqType reqType;
    protected int userId;
    protected Integer siteId;
    protected RequestStatus status;
    protected OffsetDateTime requestedOn;

    public int getReqId() { return reqId; }
    public ReqType getReqType() { return reqType; }
    public int getUserId() { return userId; }
    public Integer getSiteId() { return siteId; }
    public RequestStatus getStatus() { return status; }
    public OffsetDateTime getRequestedOn() { return requestedOn; }
}


final class DetailsRequest extends Request {

    private String newName;

    public DetailsRequest(int reqId, int userId, String newName,
            RequestStatus status, OffsetDateTime requestedOn) {
        this.reqId = reqId;
        this.reqType = ReqType.DETAILS;
        this.userId = userId;
        this.siteId = null;
        this.newName = newName;
        this.status = status;
        this.requestedOn = requestedOn;
    }

    public String getNewName() {
        return newName;
    }

}

final class SiteRequest extends Request {

    private String newOwnerName;
    private String newSiteType;

    public SiteRequest(int reqId, int userId, Integer siteId,
            String newOwnerName, String newSiteType,
            RequestStatus status, OffsetDateTime requestedOn) {
        this.reqId = reqId;
        this.reqType = ReqType.SITE;
        this.userId = userId;
        this.siteId = siteId;
        this.newOwnerName = newOwnerName;
        this.newSiteType = newSiteType;
        this.status = status;
        this.requestedOn = requestedOn;
    }

    public String getNewOwnerName() {
        return newOwnerName;
    }

    public String getNewSiteType() {
        return newSiteType;
    }

}
