package com.tewari.covitiet;

public class LocationHelper {
    public double longitute;
    public double latitude;
    public int count;
    public LocationHelper()
    {}
    public LocationHelper(double latitude, double longitute, int count)
    {
        this.latitude=latitude;
        this.longitute=longitute;
        this.count=count;
    }

    public double getLongitute() {
        return longitute;
    }

    public void setLongitute(double longitute) {
        this.longitute = longitute;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
