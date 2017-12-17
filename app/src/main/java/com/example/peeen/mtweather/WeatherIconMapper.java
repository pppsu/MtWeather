package com.example.peeen.mtweather;

/**
 * Created by peeen on 27-Nov-17.
 */

public class WeatherIconMapper {
    public static int getWeatherResource(String id, int wId) {

        if (wId == 500)
            return R.drawable.w500d1;

        if (wId == 501)
            return R.drawable.w501d1;

        if (wId == 212)
            return R.drawable.w212d1;

        if (id.equals("01d"))
            return R.drawable.w01d1;
        else if (id.equals("01n"))
            return R.drawable.w01n1;
        else if (id.equals("02d") || id.equals("02n"))
            return R.drawable.w02d1;
        else if (id.equals("03d") || id.equals("03n"))
            return R.drawable.w03d1;
        else if (id.equals("03d") || id.equals("03n"))
            return R.drawable.w03d1;
        else if (id.equals("04d") || id.equals("04n"))
            return R.drawable.w04d1;
        else if (id.equals("09d") || id.equals("09n"))
            return R.drawable.w500d1;
        else if (id.equals("10d") || id.equals("10n"))
            return R.drawable.w501d1;
        else if (id.equals("11d") || id.equals("11n"))
            return R.drawable.w212d1;
        else if (id.equals("13d") || id.equals("13n"))
            return R.drawable.w13d1;
        else if (id.equals("50d") || id.equals("50n"))
            return R.drawable.w50d1;


        return R.drawable.w01d1;

    }
}
