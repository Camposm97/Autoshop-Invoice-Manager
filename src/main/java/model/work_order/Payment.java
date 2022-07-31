package model.work_order;

import java.util.List;

public enum Payment {
    Cash, Check, Debit, Credit;

    public static List<Payment> list() {
        return List.of(Payment.values());
    }
}
