package fudan.se.myWardrobe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.BAD_REQUEST)
public class BaseException extends RuntimeException{
    public BaseException(String message){
        super(message);
    }
}
