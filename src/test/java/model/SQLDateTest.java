package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.time.LocalDate;

public class SQLDateTest {
    @Test
    void test1() {
        LocalDate localDate = LocalDate.of(2016, 12, 5);
        Date sqlDate = Date.valueOf(localDate.toString());
        assertEquals("2016-12-05", sqlDate.toString());
    }
}
