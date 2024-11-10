package store.model.domain;

public enum PromotionStatus {
    EXPIRED("만료"),
    GIVE_UP("포기"),
    ADDITIONAL("추가"),
    NONE("");

    private final String statusMessage;

    PromotionStatus(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
