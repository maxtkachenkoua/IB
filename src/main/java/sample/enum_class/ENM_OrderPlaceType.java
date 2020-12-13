package sample.enum_class;

public enum ENM_OrderPlaceType {
    NORMAL {
        // overriding toString() for SMALL
        public String toString() {
            return "NORMAL";
        }
    },

    STOP_LOSS {

        // overriding toString() for MEDIUM
        public String toString() {
            return "STOP_LOSS";
        }
    },

    TAKE_PROFIT {

        // overriding toString() for MEDIUM
        public String toString() {
            return "TAKE_PROFIT";
        }
    };

}
