package sample.enum_class;

public enum ENM_OrderQuantityType {

    SHARE {
        // overriding toString() for SMALL
        public String toString() {
            return "SHARE";
        }
    },

    USD {
        // overriding toString() for MEDIUM
        public String toString() {
            return "USD";
        }
    },

}
