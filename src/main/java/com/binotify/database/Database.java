package com.binotify.database;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private String driver;
    private String host = "localhost";
    private String port = "3307";
    private String name = "binotify-soap";
    private String username = "root";
    private String password = "";
    private Connection conn = null;
    private Statement statement = null;

    public Database() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.name;
            this.conn = DriverManager.getConnection(url,this.username,this.password);
            this.statement = this.conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    public ResultSet executeQuery(String query) throws SQLException {
        return this.statement.executeQuery(query);
    }

    public static List<Map<String, Object>> getFormattedRes(ResultSet res) throws SQLException {
//  Format result set to List of records [{...colName}]
        List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
        if (res != null) {
            if (!res.next()) {
                return null;
            } else {
                ResultSetMetaData meta = res.getMetaData();
                int columns = meta.getColumnCount();
                do {
                    Map<String, Object> row = new HashMap<String, Object>();
                    for (int i = 1; i <= columns; ++i) {
                        String colName = meta.getColumnName(i);
                        Object val = res.getObject(i);
                        row.put(colName, val);
                    }
                    resList.add(row);
                } while (res.next());
            }
        }
        return resList;
    }

//    public static void main(String[] args) {
//        try {
//            Database db = new Database();
//            ResultSet res = db.executeQuery("select * from subscription");
//            List<Map<String, Object>> data = db.getFormattedRes(res);
//            System.out.println(data);
//            ResultSet res2 = db.executeQuery("select * from logging");
//            List<Map<String, Object>> data2 = db.getFormattedRes(res2);
//            System.out.println(data2);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println(e);
//        }
//    }
}
