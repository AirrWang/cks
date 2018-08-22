/*
 *
 *  * Copyright (C) 2015 Eason.Lai (easonline7@gmail.com)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.pizidea.imagepicker;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class GlideImgLoader implements ImgLoader {
    @Override
    public void onPresentImage(ImageView imageView, String imageUri, int size) {
        Glide.with(imageView.getContext())
                .load(new File(imageUri))
                .centerCrop()
                .dontAnimate()
                .thumbnail(0.1f)
                .override(size / 4 * 3, size / 4 * 3)
                .placeholder(R.drawable.default_img)
                .error(R.drawable.default_img)
                .into(imageView);

    }

    @Override
    public void onPresentImageNoW(ImageView imageView, String imageUri) {
        Glide.with(imageView.getContext())
                .load(new File(imageUri))
                .centerCrop()
                .dontAnimate()
                .thumbnail(0.1f)
                .placeholder(R.drawable.default_img)
                .error(R.drawable.default_img)
                .into(imageView);
    }

    @Override
    public void onPresentImageNoWFit(ImageView imageView, String imageUri) {
        Glide.with(imageView.getContext())
                .load(Integer.parseInt(imageUri))
                .fitCenter()
                .dontAnimate()
                .thumbnail(0.1f)
                .into(imageView);
    }

    public void onPresentImageHttp(ImageView imageView, String imageUri) {
        Glide.with(imageView.getContext())
                .load(imageUri)
                .fitCenter()
                .dontAnimate()
                .thumbnail(0.1f)
                .into(imageView);
    }

}
