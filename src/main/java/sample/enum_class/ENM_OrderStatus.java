package sample.enum_class;

public enum ENM_OrderStatus {
    PENDING {
        // overriding toString() for SMALL
        public String toString() {
            return "PENDING";
        }
    },

    PRE_SUBMITTED {

        // overriding toString() for MEDIUM
        public String toString() {
            return "PreSubmitted";
        }
    },

}
