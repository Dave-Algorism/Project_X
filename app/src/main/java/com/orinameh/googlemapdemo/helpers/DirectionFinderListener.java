package com.orinameh.googlemapdemo.helpers;

import java.util.List;

/**
 * Created by davidevhade on 8/29/16.
 */
public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
