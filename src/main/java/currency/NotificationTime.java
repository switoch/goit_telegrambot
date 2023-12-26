package currency;

public enum NotificationTime {
    TIME1("09-00"),
    TIME2("10-00"),
    TIME3("11-00"),
    TIME4("12-00"),
    TIME5("13-00"),
    TIME6("14-00"),
    TIME7("15-00"),
    TIME8("16-00"),
    TIME9("17-00"),
    TIME10("18-00"),
    NO_TIME("Вимкнути повідомлення");

    private String value;
    public String get() {
        return value;
    }

    public static NotificationTime getEnum(String value) {
        for (NotificationTime nt: NotificationTime.values()) {
            if (nt.value.equals(value)) {
                return nt;
            }
        }
        return null;
    }

    NotificationTime(String value) {
        this.value = value;
    }
}
