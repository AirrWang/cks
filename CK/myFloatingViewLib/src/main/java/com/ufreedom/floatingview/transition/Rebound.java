package com.ufreedom.floatingview.transition;

import com.facebook.rebound.Spring;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public interface Rebound {
    
    public Spring createSpringByBouncinessAndSpeed(double bounciness, double speed);

    public Spring createSpringByTensionAndFriction(double tension, double friction) ;

    public float transition(double progress, float startValue, float endValue);
    
}
