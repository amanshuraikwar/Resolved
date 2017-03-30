package com.sonu.resolved.ui.problem.adapter.visitables;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.sonu.resolved.data.network.model.Problem;
import com.sonu.resolved.ui.problem.adapter.TypeFactory;
import com.sonu.resolved.ui.problem.adapter.Visitable;

/**
 * Created by sonu on 7/3/17.
 */

public class ProblemVisitable extends Problem implements Visitable {
    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
