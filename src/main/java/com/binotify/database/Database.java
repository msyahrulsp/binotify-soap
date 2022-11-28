package com.binotify.database;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
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

    public int executeUpdate(String query) throws SQLException {
        return this.statement.executeUpdate(query);
    }

    /**
     * return true if api key exist in SOAP database, else return false
     * **/
    public boolean verifyAPIKey(WebServiceContext wsContext, String key) {
        boolean status = false;
        try {
            MessageContext msgContext = wsContext.getMessageContext();
            HttpExchange exchange = (HttpExchange)msgContext.get("com.sun.xml.ws.http.exchange");
            Headers reqHeaders = exchange.getRequestHeaders();

            String reqAuth = reqHeaders.getFirst("Authorization");

            String query = "SELECT COUNT(1) as status FROM api_key WHERE api_key = " + "'" + key + "'";
            ResultSet res = executeQuery(query);
            List<Map<String, Object>> data = getFormattedRes(res);

            if (((Long) data.get(0).get("status")).intValue() == 1) {
                status = true;
                System.out.println("API Verified");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return status;
    }

    /**
     * insert log to SOAP database
     * @param wsContext
     * @param description
     * @param endpoint
     */
    public void insertLog(WebServiceContext wsContext, String description, String endpoint) {
        try {
            MessageContext msgContext = wsContext.getMessageContext();
            HttpExchange exchange = (HttpExchange)msgContext.get("com.sun.xml.ws.http.exchange");
            Headers reqHeaders = exchange.getRequestHeaders();

            String reqIP = reqHeaders.getFirst("X-Forwarded-For");
            String reqDate = reqHeaders.getFirst("Date");

//        System.out.println("headers" + reqHeaders.getFirst("X-Forwarded-For"));
//        System.out.println("date" + reqHeaders.getFirst("Date"));

            String query = "INSERT INTO `logging` (`id`, `description`, `ip`, `endpoint`, `requested_at`) VALUES (NULL, '" + description + "', '" + reqIP + "', '" + endpoint + "', '" + reqDate + "')";

            int res = executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
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
//            System.out.println("status: " + db.verifyAPIKey("LcS0-SmJjjUoooMAKOANu_JdFij7AOb1kaFkNXuGVWY"));
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
