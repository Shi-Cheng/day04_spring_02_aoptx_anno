package com.dx.dao.impl;

import com.dx.dao.IAccountDao;
import com.dx.domain.Account;
import com.dx.utils.ConnectionUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository("accountDao")
public class AccountDaoImpl implements IAccountDao {

    @Autowired
    private QueryRunner runner;
    @Autowired
    private ConnectionUtils connectionUtils;

    public List<Account> findAllAccount() {
        try{
            return runner.query(connectionUtils.getThreadConnection(),"select * from user",new BeanListHandler<Account>(Account.class));
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Account findAccountById(Integer accountId) {
        try{
            return runner.query(connectionUtils.getThreadConnection(),"select * from user where id=?",new BeanHandler<Account>(Account.class),accountId);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void saveAccount(Account account) {
        try{
             runner.update(connectionUtils.getThreadConnection(),"insert into user(name,money) values(?,?)",account.getName(),account.getMoney());
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void updateAccount(Account account) {
        try{
            runner.update(connectionUtils.getThreadConnection(),"update user set name=?,money=? where id=?",account.getName(),account.getMoney(),account.getId());
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void deleteAccount(Integer accountId) {
        try{
            runner.update(connectionUtils.getThreadConnection(),"delete user where id=?",accountId);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Account findAccountByName(String accountName) {
        try{
            List<Account> accounts = runner.query(connectionUtils.getThreadConnection(),"select * from user where name=?",new BeanListHandler<Account>(Account.class),accountName);
            if (accounts == null || accounts.size() ==0 ){
                return null;
            }
            if (accounts.size() > 1){
                throw new RuntimeException("结果不唯一，数据有问题");
            }
            return accounts.get(0);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
