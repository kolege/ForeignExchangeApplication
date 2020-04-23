package com.kolege.assignment.util;

import java.text.DecimalFormat;

public class Extensions {
    public static double formatForResponse(Double value){
        DecimalFormat df = new DecimalFormat("#.####");
        return Double.valueOf(df.format(value));
    }
}
