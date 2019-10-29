package com.css.ssm.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExampleJob2 {

    public void execute(){  
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "执行ExampleJob2");
    }
    
}
