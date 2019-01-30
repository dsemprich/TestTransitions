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

import android.os.Bundle
import android.support.transition.ChangeBounds
import android.support.transition.ChangeClipBounds
import android.support.transition.ChangeTransform
import android.support.transition.TransitionSet
import android.support.v4.app.Fragment
import android.support.v4.app.SharedElementCallback
import android.support.v4.view.ViewPager
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.adapter.ImagePagerAdapter

/**
 * A fragment for displaying a pager of images.
 */
class ImagePagerFragment : Fragment() {

    private var viewPager: ViewPager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
       savedInstanceState: Bundle?
    ): View {
        viewPager = inflater.inflate(R.layout.fragment_pager, container, false) as ViewPager
        viewPager?.adapter = ImagePagerAdapter(this)
        // Set the current position and add a listener that will update the selection coordinator when
        // paging the images.
        viewPager?.currentItem = MainActivity.currentPosition
        viewPager?.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                MainActivity.currentPosition = position
            }
        })

        prepareSharedElementTransition()

        // Avoid a postponeEnterTransition on orientation change, and postpone only of first creation.
        if (savedInstanceState == null) {
            postponeEnterTransition()
        }

        return viewPager as ViewPager
    }

    /**
     * Prepares the shared element transition from and back to the grid fragment.
     */
    private fun prepareSharedElementTransition() {

        val transitionSet = TransitionSet()
        transitionSet.duration = 375
        transitionSet.interpolator = FastOutSlowInInterpolator()
        transitionSet.ordering = TransitionSet.ORDERING_TOGETHER
        transitionSet.addTransition(ChangeClipBounds())
        transitionSet.addTransition(ChangeTransform())
        transitionSet.addTransition(ChangeBounds())

        sharedElementEnterTransition = transitionSet

        // A similar mapping is set at the GridFragment with a setExitSharedElementCallback.
        setEnterSharedElementCallback(
            object : SharedElementCallback() {
                override fun onMapSharedElements(names: List<String>, sharedElements: MutableMap<String, View>) {
                    // Locate the image view at the primary fragment (the ImageFragment that is currently
                    // visible). To locate the fragment, call instantiateItem with the selection position.
                    // At this stage, the method will simply return the fragment at the position and will
                    // not create a new one.
                    val currentFragment = viewPager?.let {
                        viewPager?.adapter?.instantiateItem(it, MainActivity.currentPosition)
                    } as Fragment
                    val view = currentFragment.getView() ?: return

                    // Map the first shared element name to the child ImageView.
                    sharedElements[names[0]] = view.findViewById(R.id.image)
                }
            })
    }
}
