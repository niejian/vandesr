package cn.com.vandesr.backend.config.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author: nj
 * @date: 2019/1/28:上午10:06
 */
public class AccountNotFountException extends Exception{


    public AccountNotFountException(String message) {
        super(message);
    }

    public AccountNotFountException(String message, Throwable cause) {
        super(message, cause);
    }




}
