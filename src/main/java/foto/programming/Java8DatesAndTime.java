package foto.programming;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Java8DatesAndTime {



    public static void main(String[] args){

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        LocalDateTime fromtimeStamp = LocalDateTime.parse("2018-05-18 12:06:51.043234", dateTimeFormatter);
        System.out.println(fromtimeStamp);


    }
}
