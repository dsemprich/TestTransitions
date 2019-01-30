/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.myapplication.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.myapplication.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

/**
 * A fragment for displaying an image.
 */
class ImageFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_image, container, false) as View

        @DrawableRes val imageRes = arguments?.getInt(KEY_IMAGE_RES)

        // Just like we do when binding views at the grid, we set the transition name to be the string
        // value of the image res.
        val imageView: ImageView = view.findViewById(R.id.image)
        ViewCompat.setTransitionName(imageView, imageRes.toString())
       // Load the image with Glide to prevent OOM error when the image drawables are very large.
        imageRes?.let {
            Picasso.get().load(imageRes)
                .resize(500,500)
                .centerCrop()
                .into(imageView, object : Callback{
                    override fun onSuccess() {
                        parentFragment?.startPostponedEnterTransition()
                    }

                    override fun onError(e: Exception?) {
                        parentFragment?.startPostponedEnterTransition()
                    }
                })
        }
        return view
    }

    companion object {

        private const val KEY_IMAGE_RES = "com.google.samples.gridtopager.key.imageRes"

        fun newInstance(@DrawableRes drawableRes: Int): ImageFragment {
            val fragment = ImageFragment()
            val argument = Bundle()
            argument.putInt(KEY_IMAGE_RES, drawableRes)
            fragment.arguments = argument
            return fragment
        }
    }
}
