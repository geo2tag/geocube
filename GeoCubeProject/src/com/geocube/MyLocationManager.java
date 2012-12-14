package com.geocube;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.view.Display;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Мару
 * Date: 14.12.12
 * Time: 11:59
 * To change this template use File | Settings | File Templates.
 */

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
