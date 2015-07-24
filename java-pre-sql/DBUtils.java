package me.topit.site.util;

import java.sql.*;
import java.util.Random;

/***
 * dbutils class, use static method to handle mysql
 */

public class DBUtils {
    
    private static final String writeHost = "127.0.0.1:3306";
    private static final String writeDatabase = "test";
    private static final String writeUser = "root";
    private static final String writePassword = "123456";

    private static final String readHost = "127.0.0.1:3307,127.0.0.1:3308,127.0.0.1:3309";
    private static final String readDatabase = "test";
    private static final String readUser = "read";
    private static final String readPassword = "123456";
 
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
        	e.printStackTrace();
        }
    }
 
    /**
     * 连接主库
     * 
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = null;
        try {
            String url = "jdbc:mysql://" + writeHost + "/" + writeDatabase + "?characterEncoding=utf-8&useUnicode=true";
            conn = DriverManager.getConnection(url, writeUser, writePassword);
        } catch (SQLException e) {
            System.out.println("connect to db failed!");
            throw e;
        }
        return conn;
    }
    
    /**
     * 连接从库
     * 
     * @return
     * @throws SQLException
     */
    public static Connection getReadConnection() throws SQLException {
        Connection conn = null;
        try {
            String[] host = readHost.split(",");
            Random random = new Random();
            String url = "jdbc:mysql://" + host[random.nextInt(host.length)] + "/" + readDatabase + "?characterEncoding=utf-8&useUnicode=true";
            conn = DriverManager.getConnection(url, readUser, readPassword);
        } catch (SQLException e) {
            System.out.println("connect to db failed!");
            throw e;
        }
        return conn;
    }
     
    
    /**
     * method to close ResultSet
     * @param rs
     */
    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
            	e.printStackTrace();
            }
        }
    }
     
    /**
     * method to close Statement
     * @param stmt
     */
    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            }       
            catch (Exception e) {
            	e.printStackTrace();
            }
        }
    }
     
    /**
     * method to close ResultSet、Statement
     * @param rs
     * @param stmt
     */
    public static void closeStatement(ResultSet rs, Statement stmt) {
        closeResultSet(rs);
        closeStatement(stmt);
    }
     
     
    /**
     * method to close ResultSet、Statement、Connection
     * @param rs
     * @param stmt
     * @param con
     */
    public static void closeConnection(ResultSet rs, Statement stmt, Connection con) {
        closeResultSet(rs);
        closeStatement(stmt);
        closeConnection(con);
    }
     
    /**
     * method to close Statement、Connection
     * @param stmt
     * @param con
     */
    public static void closeConnection(Statement stmt, Connection con) {
        closeStatement(stmt);
        closeConnection(con);
    }
     
    /**
     * method to close Connection
     * @param con
     */
    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
               con.close();
            }
            catch (Exception e) {
            	e.printStackTrace();
            }
        }
    }

    /**
     * 预处理参数设置
     * @param statement
     * @param params
     * @return
     * @throws SQLException
     */
    private static PreparedStatement paramsSet(PreparedStatement statement, Object... params) throws SQLException {

        int paramSize = params.length;

        for(int i=1; i<=paramSize; i++){
            Object param = params[i-1];
            if(param instanceof java.lang.Integer){
                statement.setInt(i,(Integer)param);
            }else if(param instanceof java.lang.String){
                statement.setString(i, (String) param);
            }else if(param instanceof java.lang.Float){
                statement.setFloat(i, (Float) param);
            }else if(param instanceof java.lang.Long){
                statement.setLong(i,(Long)param);
            }else if(param instanceof java.lang.Double){
                statement.setDouble(i,(Double)param);
            }else if(param instanceof java.sql.Date){
                statement.setDate(i, (Date)param);
            }else{
                statement.setObject(i,param);
            }
        }

        return statement;
    }

    /**
     * 插入操作
     * @param sql
     * @param params
     * @return
     */
    public static int insert(String sql, Object... params){

        int paramSize = sql.length() - sql.replaceAll("\\?","").length();
        if (paramSize != params.length) {
            return -1;
        }

        if (!sql.toLowerCase().startsWith("insert")) {
            return -1;
        }

        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = getConnection();
            statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            if(paramSize > 0){
                statement = paramsSet(statement,params);
            }

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()){
                return rs.getInt(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            closeConnection(statement, conn);
        }

        return 0;
    }

    /**
     * 删除操作
     * @param sql
     * @param params
     * @return
     */
    public static int delete(String sql, Object... params){
        return update(sql,params);
    }

    /**
     * 更新操作
     * @param sql
     * @param params
     * @return
     */
    public static int update(String sql, Object... params){
        int paramSize = sql.length() - sql.replaceAll("\\?","").length();
        if (paramSize != params.length) {
            return -1;
        }

        if (!sql.toLowerCase().startsWith("update") && !sql.toLowerCase().startsWith("delete") && !sql.toLowerCase().startsWith("replace")) {
            return -1;
        }

        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = getConnection();
            statement = conn.prepareStatement(sql);

            if(paramSize > 0){
                statement = paramsSet(statement,params);
            }

            return statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            closeConnection(statement, conn);
        }

        return 0;
    }


    public interface Listener{
        default void callback(ResultSet resultSet) {

        }
    }


    /**
     * 查询
     * @param sql
     * @param params
     * @return
     */
    public static ResultSet select(String sql,Listener listener, Object... params){
        int paramSize = sql.length() - sql.replaceAll("\\?","").length();
        if (paramSize != params.length) {
            return null;
        }

        if (!sql.toLowerCase().startsWith("select")) {
            return null;
        }

        if(listener == null ){
            return null;
        }

        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = getReadConnection();
            statement = conn.prepareStatement(sql);

            if(paramSize > 0){
                statement = paramsSet(statement,params);
            }

            ResultSet resultSet = statement.executeQuery();
            listener.callback(resultSet);
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            closeConnection(statement, conn);
        }

        return null;
    }

}
