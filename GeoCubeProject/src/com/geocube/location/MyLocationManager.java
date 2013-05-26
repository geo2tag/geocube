package com.geocube.location;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.view.Display;

import java.util.List;

public class MyLocationManager {
    public static Location getLocation(Context ctx) {
        LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers;
        providers = lm.getProviders(true);
        Location l = null;

        for (int i = providers.size() - 1; i >= 0; i--) {
            l = lm.getLastKnownLocation(providers.get(i));
            if (l != null)
                break;
        }
        return l;
    }

}
