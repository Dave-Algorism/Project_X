package com.orinameh.googlemapdemo.helpers;

import com.google.android.gms.maps.model.LatLng;
import com.orinameh.googlemapdemo.helpers.Distance;
import com.orinameh.googlemapdemo.helpers.Duration;

import java.util.List;

/**
 * Created by davidevhade on 8/29/16.
 */
public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}
