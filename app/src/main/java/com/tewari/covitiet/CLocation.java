package com.tewari.covitiet;

import android.location.Location;

public class CLocation extends Location {
    private static final float feet = 3.28083989501312f;

    public CLocation(Location location) {
        super(location);
    }

    @Override
    public float distanceTo(Location dest) {
        float nDistance = super.distanceTo(dest);
        //convert meters to feet
        nDistance = nDistance * this.feet;

        return nDistance;
    }

    @Override
    public double getAltitude() {
        double nAltitude = super.getAltitude();
            //convert meters to feet
        nAltitude = nAltitude * (double) this.feet;

        return nAltitude;
    }


    @Override
    public float getSpeed() {
        //converting from metre/sec to feet/sec
        float nSpeed = super.getSpeed()*this.feet;
        return nSpeed;
    }

    @Override
    public float getAccuracy() {
        //convert meters to feet
        float nAccuracy = super.getAccuracy()* this.feet;
        return nAccuracy;
    }

    @Override
    public double getLatitude() {
        return super.getLatitude();
    }

    @Override
    public double getLongitude() {
        return super.getLongitude();
    }
}
