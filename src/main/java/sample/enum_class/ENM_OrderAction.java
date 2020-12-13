package sample.enum_class;

public enum ENM_OrderAction {
    BUY {
        // overriding toString() for SMALL
        public String toString() {
            return "BUY";
        }
    },

    SELL {

        // overriding toString() for MEDIUM
        public String toString() {
            return "SELL";
        }
    },

}
