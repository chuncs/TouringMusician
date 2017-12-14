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

package com.google.engedu.touringmusician;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class TourMap extends View {

    private Bitmap mapImage;
    private CircularLinkedList list = new CircularLinkedList();
    private String insertMode = "Add";
    private CircularLinkedList list1 = new CircularLinkedList();
    private CircularLinkedList list2 = new CircularLinkedList();
    private CircularLinkedList list3 = new CircularLinkedList();

    public TourMap(Context context) {
        super(context);
        mapImage = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.map);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mapImage, 0, 0, null);
        Paint pointPaint = new Paint();
        pointPaint.setColor(Color.RED);

        Paint linePaint = new Paint();
        linePaint.setStrokeWidth(3);
        Point headPoint = new Point();
        Point currentPoint = new Point();

        ArrayList<CircularLinkedList> circularLinkedLists = new ArrayList<>();
        circularLinkedLists.add(list1);
        circularLinkedLists.add(list2);
        circularLinkedLists.add(list3);

        ArrayList<Integer> integerArrayList = new ArrayList<>();
        integerArrayList.add(Color.argb(120, 0, 0, 255));
        integerArrayList.add(Color.argb(120, 255, 0, 0));
        integerArrayList.add(Color.argb(120, 0, 0, 0));

        if (insertMode.equals("Closest")) {
            linePaint.setColor(Color.BLUE);
        } else if (insertMode.equals("Smallest")) {
            linePaint.setColor(Color.MAGENTA);
        } else {
            linePaint.setColor(Color.BLACK);
        }

        if (insertMode.equals("3 in 1")) {
            for (int i = 0; i < 3; i++) {
                for (Point p : circularLinkedLists.get(i)) {
                    linePaint.setColor(integerArrayList.get(i));
                    if (headPoint.equals(new Point())) {
                        headPoint = currentPoint = p;
                    } else {
                        canvas.drawLine(p.x, p.y, currentPoint.x, currentPoint.y, linePaint);
                    }
                    currentPoint = p;
                    canvas.drawCircle(p.x, p.y, 20, pointPaint);
                }
                canvas.drawLine(currentPoint.x, currentPoint.y, headPoint.x, headPoint.y, linePaint);
            }
        }else {
            for (Point p : list) {
                if (headPoint.equals(new Point())) {
                    headPoint = currentPoint = p;
                } else {
                    canvas.drawLine(p.x, p.y, currentPoint.x, currentPoint.y, linePaint);
                }
                currentPoint = p;
                canvas.drawCircle(p.x, p.y, 20, pointPaint);
            }
            canvas.drawLine(currentPoint.x, currentPoint.y, headPoint.x, headPoint.y, linePaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Point p = new Point((int) event.getX(), (int)event.getY());
                if (insertMode.equals("Closest")) {
                    list.insertNearest(p);
                } else if (insertMode.equals("Smallest")) {
                    list.insertSmallest(p);
                } else {
                    list.insertBeginning(p);
                }
                list1.insertNearest(p);
                list2.insertSmallest(p);
                list3.insertBeginning(p);

                TextView message = (TextView) ((Activity) getContext()).findViewById(R.id.game_status);
                if (insertMode.equals("3 in 1")) {
                    if (message != null) {
                        message.setText(String.format("Tour length is now %.2f(closest) %.2f(smallest) %.2f(beginning)", list1.totalDistance(), list2.totalDistance(), list3.totalDistance()));
                    }
                } else {
                    if (message != null) {
                        message.setText(String.format("Tour length is now %.2f", list.totalDistance()));
                    }
                }
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void reset() {
        list.reset();
        list1.reset();
        list2.reset();
        list3.reset();
        invalidate();
    }

    public void setInsertMode(String mode) {
        insertMode = mode;
    }
}
