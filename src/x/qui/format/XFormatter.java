package x.qui.format;

/**
 * Created by орда on 03.04.2017.
 */
public class XFormatter {
    public static final int NUM_MONTHS = 10;
    public static final int MONTH_PERIOD = 30;
    public static final int YEAR_PERIOD = MONTH_PERIOD*NUM_MONTHS;

    public XFormatter() {

    }

        public static String formatDate(int days) {
            // 355
            // 355/30 = 11-m
            // 11/10 = 1-y

            int months = days / MONTH_PERIOD;
            int years = months / NUM_MONTHS;
            return String.format("%s years %s months %s days", years, months%NUM_MONTHS, days%MONTH_PERIOD);
        }

        public static String formatDateShort(int days) {
            int months = days /MONTH_PERIOD;
            int years = months /NUM_MONTHS;
            return String.format("%04d.%02d.%02d", years, months % NUM_MONTHS, days % MONTH_PERIOD);
        }

        public static int formatYears(int days) {
        int months = days /MONTH_PERIOD;
        int years = months /NUM_MONTHS;
        return years;
    }

}
