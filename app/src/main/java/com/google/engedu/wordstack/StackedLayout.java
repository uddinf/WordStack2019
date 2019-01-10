/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.wordstack;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Stack;

public class StackedLayout extends LinearLayout {

    private Stack<View> tiles = new Stack();
    private static final String TAG = "StackedLayout";

    public StackedLayout(Context context) {
        super(context);
    }

    public void push(View tile) {

        Log.i(TAG, "error test");

        //call removeView with the tile on top of the stack (if there is one) to hide that tile
        if (!tiles.empty()) {
            removeView(tiles.peek());
        }

        // push specified tile into stack
        tiles.push(tile);

        // call add view with the tile that has just been pushed
        addView(tiles.peek());

        Log.i(TAG, "end of push error test");

    }

    public View pop() {
        View popped = null;

        // pop tile from tiles
        popped = tiles.pop();

        // remove view of popped
        if (popped != null) {

            removeView(popped);

            // add view of new top tile
            addView(tiles.peek());
        }

        return popped;
    }

    public View peek() {
        return tiles.peek();
    }

    public boolean empty() {
        return tiles.empty();
    }

    public void clear() {
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
    }
}
