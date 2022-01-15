package model.database;

import model.work_order.AutoPart;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class AutoPartStore {
    private Connection c;
    private Statement stmt;

    public AutoPartStore(@NotNull Connection c) throws SQLException {
        this.c = c;
        this.stmt = c.createStatement();
    }

    public void add(@NotNull AutoPart part) {
        try {
            PreparedStatement prepStmt = c.prepareStatement(
                    "insert into item " +
                            "(item_name,desc,retail_price,list_price,taxable,quantity) " +
                            "values (?,?,?,?,?,?)");
            prepStmt.setString(1, part.getName());
            prepStmt.setString(2, part.getDesc());
            prepStmt.setDouble(3, part.getRetailPrice());
            prepStmt.setDouble(4, part.getListPrice());
            prepStmt.setBoolean(5, part.isTaxable());
            prepStmt.setInt(6, part.getQuantity());
            prepStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<AutoPart> getFilteredAutoParts(String s1, String s2) {
        List<AutoPart> list = new LinkedList<>();
        try {
            ResultSet rs = stmt.executeQuery(
                    "select * from item where " +
                            "item_name like \"" + s1 + "%\" " +
                            "and desc like \"%" + s2 + "%\"");
            while (rs.next()) {
                String partNumber = rs.getString("item_name");
                String desc = rs.getString("desc");
                double retailPrice = rs.getDouble("retail_price");
                double listPrice = rs.getDouble("list_price");
                int quantity = rs.getInt(("quantity"));
                boolean taxable = rs.getBoolean("taxable");
                list.add(new AutoPart(partNumber, desc, retailPrice, listPrice, quantity, taxable));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
