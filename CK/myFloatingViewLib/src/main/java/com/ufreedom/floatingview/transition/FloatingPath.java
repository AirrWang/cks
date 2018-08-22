package com.ufreedom.floatingview.transition;

import android.graphics.Path;
import android.graphics.PathMeasure;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class FloatingPath {

    private Path mPath;
    private PathMeasure mPathMeasure;

    protected FloatingPath() {
        this.mPath = new Path();
    }
   
    protected FloatingPath(Path path) {
        this.mPath = path;
    }

    public static FloatingPath create(Path path, boolean forceClose) {
        FloatingPath floatingPath = new FloatingPath(path);
        floatingPath.mPathMeasure = new PathMeasure(path, forceClose);
        return floatingPath;
    }

    public Path getPath() {
        return mPath;
    }

    public PathMeasure getPathMeasure() {
        return mPathMeasure;
    }
}
