import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class RequestFactory {

    public static Request create(int reqId,
                                 ReqType reqType,
                                 int userId,
                                 Integer siteId,
                                 String newName,
                                 String newOwnerName,
                                 String newSiteType,
                                 RequestStatus status,
                                 OffsetDateTime requestedOn) {

        if (reqType == null)
            throw new IllegalArgumentException("reqType cannot be null");

        if (status == null)
            status = RequestStatus.PENDING;

        if (requestedOn == null)
            requestedOn = OffsetDateTime.now(ZoneOffset.UTC);

        return switch (reqType) {

            case DETAILS -> {
                if (isBlank(newName))
                    throw new IllegalArgumentException("DETAILS request requires new name");

                yield new DetailsRequest(
                        reqId,
                        userId,
                        newName.trim(),
                        status,
                        requestedOn
                );
            }

            case SITE -> {
                if (siteId == null)
                    throw new IllegalArgumentException("SITE request requires siteId");

                boolean hasOwnerChange = !isBlank(newOwnerName);
                boolean hasTypeChange  = !isBlank(newSiteType);

                if (!hasOwnerChange && !hasTypeChange)
                    throw new IllegalArgumentException(
                        "SITE request requires at least one of: newOwnerName or newSiteType"
                    );

                // Normalize & validate using SiteType enum
                String normalizedType = null;
                if (hasTypeChange) {
                    normalizedType = SiteType.fromDb(newSiteType).name();
                }

                yield new SiteRequest(
                        reqId,
                        userId,
                        siteId,
                        hasOwnerChange ? newOwnerName.trim() : null,
                        normalizedType,
                        status,
                        requestedOn
                );
            }
        };
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
