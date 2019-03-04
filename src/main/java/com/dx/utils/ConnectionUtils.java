package com.dx.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
@Component("connectionUtils")
public class ConnectionUtils {
    private ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();
    @Autowired
    private DataSource dataSource;
    public Connection getThreadConnection(){
        try {
            Connection conn = threadLocal.get();
            if (conn == null){
                conn = dataSource.getConnection();
                threadLocal.set(conn);
            }
            return  conn;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public void removeConnection(){
        threadLocal.remove();
    }
}
