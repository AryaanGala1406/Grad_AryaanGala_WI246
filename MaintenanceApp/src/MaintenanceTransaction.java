import java.time.OffsetDateTime;

public class MaintenanceTransaction {

    private int txnId;
    private int siteId;
    private String ownerName;
    private int year;
    private int paidAmount;
    private OffsetDateTime paidOn;
    private int remainingBalance;

    public MaintenanceTransaction(int txnId, int siteId, String ownerName,
                                  int year, int paidAmount,
                                  OffsetDateTime paidOn,
                                  int remainingBalance) {
        this.txnId = txnId;
        this.siteId = siteId;
        this.ownerName = ownerName;
        this.year = year;
        this.paidAmount = paidAmount;
        this.paidOn = paidOn;
        this.remainingBalance = remainingBalance;
    }

    public int getTxnId() { return txnId; }
    public int getSiteId() { return siteId; }
    public String getOwnerName() { return ownerName; }
    public int getYear() { return year; }
    public int getPaidAmount() { return paidAmount; }
    public OffsetDateTime getPaidOn() { return paidOn; }
    public int getRemainingBalance() { return remainingBalance; }
}
