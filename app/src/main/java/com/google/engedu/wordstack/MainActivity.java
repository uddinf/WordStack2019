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

import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private static final int WORD_LENGTH = 5;
    public static final int LIGHT_BLUE = Color.rgb(176, 200, 255);
    public static final int LIGHT_GREEN = Color.rgb(200, 255, 200);
    private ArrayList<String> words = new ArrayList<>();
    private Random random = new Random();
    private StackedLayout stackedLayout;
    private String word1, word2;
    private static final String TAG = "WordActivity";
    private ViewGroup word1LinearLayout;
    private ViewGroup word2LinearLayout;
    private Stack<LetterTile> placedTiles = new Stack<LetterTile>(); // a stack to keep track of the placed tiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = in.readLine()) != null) {
                String word = line.trim();
                if (word.length() == WORD_LENGTH) {
                    words.add(word);
                }

            }
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        LinearLayout verticalLayout = (LinearLayout) findViewById(R.id.vertical_layout);
        stackedLayout = new StackedLayout(this);
        verticalLayout.addView(stackedLayout, 3);

        word1LinearLayout = findViewById(R.id.word1);
        word1LinearLayout.setOnTouchListener(new TouchListener());
        //word1LinearLayout.setOnDragListener(new DragListener());
        word2LinearLayout = findViewById(R.id.word2);
        word2LinearLayout.setOnTouchListener(new TouchListener());
        //word2LinearLayout.setOnDragListener(new DragListener());
    }

    private class TouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && !stackedLayout.empty()) {
                LetterTile tile = (LetterTile) stackedLayout.peek();
                tile.moveToViewGroup((ViewGroup) v);
                if (stackedLayout.empty()) {
                    TextView messageBox = (TextView) findViewById(R.id.message_box);
                    messageBox.setText(word1 + " " + word2);
                }

                // push the touched tile onto placedTiles.
                placedTiles.push(tile);


                return true;
            }
            return false;
        }
    }

    private class DragListener implements View.OnDragListener {

        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(LIGHT_GREEN);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundColor(Color.WHITE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign Tile to the target Layout
                    LetterTile tile = (LetterTile) event.getLocalState();
                    tile.moveToViewGroup((ViewGroup) v);
                    if (stackedLayout.empty()) {
                        TextView messageBox = (TextView) findViewById(R.id.message_box);
                        messageBox.setText(word1 + " " + word2);
                    }
                    /**
                     **
                     **  YOUR CODE GOES HERE
                     **
                     **/
                    return true;
            }
            return false;
        }
    }

    public boolean onStartGame(View view) {

        TextView messageBox = (TextView) findViewById(R.id.message_box);
        messageBox.setText("Game started");

        // randomly choose 2 words
        Random rand = new Random();
        int index1 = rand.nextInt(words.size());
        int index2 = rand.nextInt(words.size());

        // make sure words are not the same
        while (index2 == index1) {
            index2 = rand.nextInt(words.size());
        }
        // words to be used in game
        word1 = words.get(index1);
        word2 = words.get(index2);

        int wordChoice = rand.nextInt(2) + 1;
        int word1Counter = 0;
        int word2Counter = 0;
        String scrambledLetters = "";

        // randomly choose word1 or word2 to get letters from to put into scrambled string
        while (word1Counter != word1.length() || word2Counter != word2.length()) {
            //randomly choose word
            wordChoice = rand.nextInt(2) + 1;

            if (wordChoice == 1 && word1Counter < word1.length()) {
                scrambledLetters += word1.charAt(word1Counter);
                word1Counter++;

            } else if (wordChoice == 2 && word2Counter < word2.length()) {
                scrambledLetters += word2.charAt(word2Counter);
                word2Counter++;
            }
        }

        char[] letters = scrambledLetters.toCharArray();

        // update on screen with scrambled words
        for (int i = letters.length - 1; i >= 0; i--) {

            // new tile for each letter
            LetterTile tile = new LetterTile(this, letters[i]);

            // push into stack layout
            stackedLayout.push(tile);
        }
        Log.i(TAG, "word1: " + word1);
        Log.i(TAG, "word2: " + word2);
        Log.i(TAG, "word " + scrambledLetters + " " + word1 + " " + word2);

        //removeAllViews(word1LinearLayout);
        word1LinearLayout.removeAllViews();
        word2LinearLayout.removeAllViews();

        stackedLayout.clear();

        return true;
    }


    public boolean onUndo(View view) {

        // pop the most recent tile from placedTiles and move it back to the stackedLayout
        if (!placedTiles.empty()) {
            LetterTile currTile = placedTiles.pop();
            currTile.moveToViewGroup(stackedLayout);
        }


        return true;
    }
}
