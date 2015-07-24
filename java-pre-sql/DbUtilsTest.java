package me.topit.site;

import junit.framework.TestCase;
import me.topit.site.util.DBUtils;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DbUtilsTest extends TestCase{
    @Test
    public static void testInsert(){
        String sql = "insert into comment (fid,uid,cont) values (?,?,?)";
        DBUtils.insert(sql, 100, 50, "你好");
    }

    @Test
    public static void testSelect(){
        String sql = "select id,fid,tid,score,create_ts,modify_ts from exchange_wechat where id=?";
        int id = 1;

        DBUtils.select(sql, new DBUtils.Listener() {
            @Override
            public void callback(ResultSet resultSet) {
                try {
                    if (resultSet.next()) {
                        System.out.println(resultSet.getInt(1));
                        System.out.println(resultSet.getInt(2));
                        System.out.println(resultSet.getInt(3));
                        System.out.println(resultSet.getInt(4));
                        System.out.println(resultSet.getInt(5));
                        System.out.println(resultSet.getInt(6));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }, id);
    }

    @Test
    public static void testUpdate1(){
        String sql = "update user set name=? where id=?";
        DBUtils.update(sql, "大家好才是真的好", 1);
    }

    @Test
    public static void testUpdate2(){

        HashMap<String,String> fieldMap = new HashMap<String,String>(){{
            put("wechat","zhoumengkang");
            put("icon","1433389099D337C7D1F9");
            put("profession","程序员");
            put("location","北京市西城区");
            put("birthday","1437634718");
        }};
        String sql = "update user set";
        String[] param = new String[fieldMap.size()];

        Iterator it = fieldMap.entrySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            sql += " `" +key + "`=?,";
            String val = (String) entry.getValue();
            param[i] = val;
            i++;
        }
        sql = sql.substring(0,sql.length() -1);
        sql += " where id=1";

        DBUtils.update(sql, param);
    }

    @Test
    public static void testReplace(){
        String sql ="replace into evaluate(fid,tid,score,cont,stat,ts) values (?,?,?,?,1,?)";
        DBUtils.update(sql, 1, 2, 100, "不错", 1437634718);
    }

    @Test
    public static void testDelete(){
        String deleteSql = "delete from comment where id=?";
        DBUtils.delete(deleteSql,42);
    }
}
