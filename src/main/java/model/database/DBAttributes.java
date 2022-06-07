package model.database;

public class DBAttributes {
//    public static final String VEHICLE_TABLE = "vehicle";
//    public static final String VEHICLE_VIN = "vin";
//    public static final String VEHICLE_YEAR = "year";
//    public static final String VEHICLE_MAKE = "make";
//    public static final String VEHICLE_MODEL = "model";
//    public static final String VEHICLE_LICENSE_PLATE = "license_plate";
//    public static final String VEHICLE_COLOR = "color";
//    public static final String VEHICLE_ENGINE = "engine";
//    public static final String VEHICLE_TRANSMISSION = "transmission";
//    public static final String VEHICLE_MILEAGE_IN = "mileage_in";
//    public static final String VEHICLE_MILEAGE_OUT = "mileage_out";
//    public static String VEHICLE_CUSTOMER_ID = "customer_id";

//    public static final String ITEM_TABLE = "item";
//    public static final String ITEM_NAME = "item_name";
//    public static final String ITEM_DESC = "desc";
//    public static final String ITEM_RETAIL_PRICE = "retail_price";
//    public static final String ITEM_LIST_PRICE = "list_price";
//    public static final String ITEM_TAXABLE = "taxable";
//    public static final String ITEM_QUANTITY = "quantity";

//    public static final String WORK_ORDER_TABLE = "work_order";
//    public static final String WORK_ORDER_ID = "work_order_id";
//    public static final String WORK_ORDER_DATE_CREATED = "date_created";
//    public static final String WORK_ORDER_DATE_COMPLETED = "date_completed";
//    public static final String WORK_ORDER_CUSTOMER_FIRST_NAME = "customer_first_name";
//    public static final String WORK_ORDER_CUSTOMER_LAST_NAME = "customer_last_name";
//    public static final String WORK_ORDER_CUSTOMER_PHONE = "customer_phone";
//    public static final String WORK_ORDER_CUSTOMER_EMAIL = "customer_email";
//    public static final String WORK_ORDER_CUSTOMER_COMPANY = "customer_company";
//    public static final String WORK_ORDER_CUSTOMER_STREET = "customer_street";
//    public static final String WORK_ORDER_CUSTOMER_CITY = "customer_city";
//    public static final String WORK_ORDER_CUSTOMER_STATE = "customer_state";
//    public static final String WORK_ORDER_CUSTOMER_ZIP = "customer_zip";
//    public static final String WORK_ORDER_VEHICLE_VIN = "vehicle_vin";
//    public static final String WORK_ORDER_VEHICLE_YEAR = "vehicle_year";
//    public static final String WORK_ORDER_VEHICLE_MAKE = "vehicle_make";
//    public static final String WORK_ORDER_VEHICLE_MODEL = "vehicle_model";
//    public static final String WORK_ORDER_VEHICLE_LICENSE_PLATE = "vehicle_license_plate";
//    public static final String WORK_ORDER_VEHICLE_COLOR = "vehicle_color";
//    public static final String WORK_ORDER_VEHICLE_ENGINE = "vehicle_engine";
//    public static final String WORK_ORDER_VEHICLE_TRANSMISSION = "vehicle_transmission";
//    public static final String WORK_ORDER_VEHICLE_MILEAGE_IN = "vehicle_mileage_in";
//    public static final String WORK_ORDER_VEHICLE_MILEAGE_OUT = "vehicle_mileage_out";

//    public static final String WORK_ORDER_ITEM_TABLE = "work_order_item";
//    public static final String WORK_ORDER_ITEM_ID = "work_order_item_id";
//    public static final String WORK_ORDER_ITEM_WORD_ORDER_ID = "work_order_id";
//    public static final String WORK_ORDER_ITEM_NAME = "item_name";
//    public static final String WORK_ORDER_ITEM_DESC = "item_desc";
//    public static final String WORK_ORDER_ITEM_RETAIL_PRICE = "item_retail_price";
//    public static final String WORK_ORDER_ITEM_LIST_PRICE = "item_list_price";
//    public static final String WORK_ORDER_ITEM_QUANTITY = "item_quantity";
//    public static final String WORK_ORDER_ITEM_TAXABLE = "item_taxable";
//
//    public static final String WORK_ORDER_LABOR_TABLE = "work_order_labor";
//    public static final String WORK_ORDER_LABOR_ID = "work_order_labor_id";
//    public static final String WORK_ORDER_LABOR_WORK_ORDER_ID = "work_order_id";
//    public static final String WORK_ORDER_LABOR_CODE = "labor_code";
//    public static final String WORK_ORDER_LABOR_DESC = "labor_desc";
//    public static final String WORK_ORDER_LABOR_BILLED_HRS = "Labor_billed_hrs";
//    public static final String WORK_ORDER_LABOR_RATE = "labor_rate";
//    public static final String WORK_ORDER_LABOR_TAXABLE = "labor_taxable";

//    public static final String
//            WORK_ORDER_PAYMENT_TABLE = "work_order_payment",
//            WORK_ORDER_PAYMENT_ID = "work_order_payment_id";
//    public static final String WORK_ORDER_PAYMENT_WORK_ORDER_ID = "work_order_id";
//    public static final String WORK_ORDER_PAYMENT_DATE = "date_of_payment";
//    public static final String WORK_ORDER_PAYMENT_AMOUNT = "amount";
//    public static final String WORK_ORDER_PAYMENT_TYPE = "type";

    public static final CustomerTableAttributes CUSTOMER_TABLE = new CustomerTableAttributes();
    public static final VehicleTableAttributes VEHICLE_TABLE = new VehicleTableAttributes();
    public static final ItemTableAttributes ITEM_TABLE = new ItemTableAttributes();
    public static final WorkOrderTableAttributes WORK_ORDER_TABLE = new WorkOrderTableAttributes();
    public static final WorkOrderItemTableAttributes WORK_ORDER_ITEM_TABLE = new WorkOrderItemTableAttributes();
    public static final WorkOrderLaborTableAttributes WORK_ORDER_LABOR_TABLE = new WorkOrderLaborTableAttributes();
    public static final WorkOrderPaymentTableAttributes WORK_ORDER_PAYMENT_TABLE = new WorkOrderPaymentTableAttributes();

    public static class CustomerTableAttributes {
        final String CUSTOMER_ID = "customer_id";
        final String FIRST_NAME = "first_name";
        final String LAST_NAME = "last_name";
        final String PHONE = "phone";
        final String EMAIL = "email";
        final String COMPANY = "company";
        final String STREET = "street";
        final String CITY = "city";
        final String STATE = "state";
        final String ZIP = "zip";

        private CustomerTableAttributes() {}

        @Override
        public String toString() {
            return "customer";
        }
    }

    public static class VehicleTableAttributes {
        final String VIN = "vin";
        final String YEAR = "year";
        final String MAKE = "make";
        final String MODEL = "model";
        final String LICENSE_PLATE = "license_plate";
        final String COLOR = "color";
        final String ENGINE = "engine";
        final String TRANSMISSION = "transmission";
        final String MILEAGE_IN = "mileage_in";
        final String MILEAGE_OUT = "mileage_out";
        final String CUSTOMER_ID = "customer_id";

        private VehicleTableAttributes() {}

        @Override
        public String toString() { return "vehicle"; }
    }

    public static class ItemTableAttributes {
        final String ITEM_NAME = "item_name";
        final String ITEM_DESC = "desc";
        final String RETAIL_PRICE = "retail_price";
        final String LIST_PRICE = "list_price";
        final String TAXABLE = "taxable";
        final String QUANTITY = "quantity";

        private ItemTableAttributes() {}

        @Override
        public String toString() {
            return "item";
        }
    }

    public static class WorkOrderTableAttributes {
        final String WORK_ORDER_ID = "work_order_id";
        final String DATE_CREATED = "date_created";
        final String DATE_COMPLETED = "date_completed";
        final String CUSTOMER_FIRST_NAME = "customer_first_name";
        final String CUSTOMER_LAST_NAME = "customer_last_name";
        final String CUSTOMER_PHONE = "customer_phone";
        final String CUSTOMER_EMAIL = "customer_email";
        final String CUSTOMER_COMPANY = "customer_company";
        final String CUSTOMER_STREET = "customer_street";
        final String CUSTOMER_CITY = "customer_city";
        final String CUSTOMER_STATE = "customer_state";
        final String CUSTOMER_ZIP = "customer_zip";
        final String VEHICLE_VIN = "vehicle_vin";
        final String VEHICLE_YEAR = "vehicle_year";
        final String VEHICLE_MAKE = "vehicle_make";
        final String VEHICLE_MODEL = "vehicle_model";
        final String VEHICLE_LICENSE_PLATE = "vehicle_license_plate";
        final String VEHICLE_COLOR = "vehicle_color";
        final String VEHICLE_ENGINE = "vehicle_engine";
        final String VEHICLE_TRANSMISSION = "vehicle_transmission";
        final String VEHICLE_MILEAGE_IN = "vehicle_mileage_in";
        final String VEHICLE_MILEAGE_OUT = "vehicle_mileage_out";

        private WorkOrderTableAttributes() {}

        @Override
        public String toString() {
            return "work_order";
        }
    }

    public static class WorkOrderItemTableAttributes {
        final String ITEM_ID = "work_order_item_id";
        final String WORK_ORDER_ID = "work_order_id";
        final String ITEM_NAME = "item_name";
        final String ITEM_DESC = "item_desc";
        final String ITEM_RETAIL_PRICE = "item_retail_price";
        final String ITEM_LIST_PRICE = "item_list_price";
        final String ITEM_QUANTITY = "item_quantity";
        final String ITEM_TAXABLE = "item_taxable";

        private WorkOrderItemTableAttributes() {}

        @Override
        public String toString() {
            return "work_order_item";
        }
    }

    public static class WorkOrderLaborTableAttributes {
        final String LABOR_ID = "work_order_labor_id";
        final String WORK_ORDER_ID = "work_order_id";
        final String LABOR_CODE = "labor_code";
        final String LABOR_DESC = "labor_desc";
        final String LABOR_BILLED_HRS = "labor_billed_hrs";
        final String LABOR_RATE = "labor_rate";
        final String LABOR_TAXABLE = "labor_taxable";

        private WorkOrderLaborTableAttributes() {}

        @Override
        public String toString() {
            return "work_order_labor";
        }
    }

    public static class WorkOrderPaymentTableAttributes {
        final String PAYMENT_ID = "work_order_payment_id";
        final String WORK_ORDER_ID = "work_order_id";
        final String PAYMENT_DATE = "date_of_payment";
        final String PAYMENT_AMOUNT = "amount";
        final String PAYMENT_TYPE = "type";

        private WorkOrderPaymentTableAttributes() {}

        @Override
        public String toString() {
            return "work_order_payment";
        }
    }
}
