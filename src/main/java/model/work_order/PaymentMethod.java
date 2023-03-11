package model.work_order;

import java.util.List;

public enum PaymentMethod {
    Cash, Check, Credit;

    public static List<PaymentMethod> list() {
        return List.of(PaymentMethod.values());
    }
}
