package fudan.se.myWardrobe.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponse {
    String message;
    String timestamp;

    public SuccessResponse(String message) {
        this.message = message;
        this.timestamp = getTimestamp();
    }

    public String getTimestamp(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//定义格式
        return df.format(new Date());
    }
}
