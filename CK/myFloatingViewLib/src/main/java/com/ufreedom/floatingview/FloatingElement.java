package com.ufreedom.floatingview;

import android.view.View;

import com.ufreedom.floatingview.transition.FloatingTransition;


/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class FloatingElement {
    
    public FloatingTransition floatingTransition;
    
    public int offsetX;
    
    public int offsetY;
    
    public View targetView;
    
    public View anchorView;
    
    public int targetViewLayoutResId;
}
