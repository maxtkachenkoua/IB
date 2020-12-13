package sample.enum_class;

public enum ENM_OrderType {
    MARKET {
        // overriding toString() for SMALL
        public String toString() {
            return "MARKET";
        }
    },

    LIMIT {

        // overriding toString() for MEDIUM
        public String toString() {
            return "LIMIT";
        }
    },

    STOP_LOSS {

        // overriding toString() for MEDIUM
        public String toString() {
            return "STOP_LOSS";
        }
    },
}
