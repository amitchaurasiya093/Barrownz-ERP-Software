package com.erp.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LeaveCalculator {

    public static double calculateLeaveQuantity(LocalDate fromDate, LocalDate toDate, int fromSession, int toSession) {
        if (fromDate == null || toDate == null)
            return 0.0;

        long daysBetween = ChronoUnit.DAYS.between(fromDate, toDate);

        // Same day leave
        if (daysBetween == 0) {
            return (toSession - fromSession + 1) * 0.25;
        }

        // Multi-day leave
        double total = 0.0;

        // First day sessions (fromSession → 4)
        total += (4 - fromSession + 1) * 0.25;

        // Middle days (if any)
        if (daysBetween > 1) {
            total += (daysBetween - 1);
        }

        // Last day sessions (1 → toSession)
        total += (toSession) * 0.25;

        return total;
    }

}
