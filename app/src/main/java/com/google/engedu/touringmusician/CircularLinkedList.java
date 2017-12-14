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


import android.graphics.Point;

import java.util.Iterator;

public class CircularLinkedList implements Iterable<Point> {

    private class Node {
        Point point;
        Node prev, next;

        public Node(Point point) {
            this.point = point;
            prev = next = null;
        }
    }

    Node head;

    public void insertBeginning(Point p) {
        Node beginningNode = new Node(p);

        if (head == null) {
            beginningNode.prev = beginningNode;
            beginningNode.next = beginningNode;
            head = beginningNode;
        } else {
            beginningNode.prev = head.prev; // inserted node points to tail
            beginningNode.next = head; // inserted node points to current head
            head.prev.next = beginningNode; // tail points to inserted node
            head.prev = beginningNode; // current head points to inserted node
            head = beginningNode; // inserted node becomes head
        }
    }

    private float distanceBetween(Point from, Point to) {
        return (float) Math.sqrt(Math.pow(from.y - to.y, 2) + Math.pow(from.x - to.x, 2));
    }

    public float totalDistance() {
        float total = 0;
        CircularLinkedListIterator circularLinkedListIterator = new CircularLinkedListIterator();
        Point headPoint = new Point();
        Point currentPoint = new Point();
        Point nextPoint = new Point();

        while (circularLinkedListIterator.hasNext()) {
            if (headPoint.equals(new Point())) {
                headPoint = currentPoint = circularLinkedListIterator.next();
            } else {
                nextPoint = circularLinkedListIterator.next();
                total += distanceBetween(currentPoint, nextPoint);
                currentPoint = nextPoint;
            }
        }
        total += distanceBetween(currentPoint, headPoint);

        return total;
    }

    public void insertNearest(Point p) {
        Node nearestNode = new Node(p);

        if (head == null) {
            nearestNode.prev = nearestNode;
            nearestNode.next = nearestNode;
            head = nearestNode;
        } else {
            Node currentNode = head.next;
            Node closestNode = head;
            float closestDistance = distanceBetween(nearestNode.point, closestNode.point);

            while (currentNode != head) {
                if (closestDistance > distanceBetween(nearestNode.point, currentNode.point)) {
                    closestDistance = distanceBetween(nearestNode.point, currentNode.point);
                    closestNode = currentNode;
                }
                currentNode = currentNode.next;
            }

            nearestNode.prev = closestNode; // inserted node points to nearest node
            nearestNode.next = closestNode.next; // inserted node points to what nearest node points to next
            closestNode.next.prev = nearestNode; // the node after nearest node points to inserted node
            closestNode.next = nearestNode; // nearest node points to inserted node
        }
    }

    public void insertSmallest(Point p) {
        Node smallestNode = new Node(p);

        if (head == null) {
            smallestNode.prev = smallestNode;
            smallestNode.next = smallestNode;
            head = smallestNode;
        } else {
            float leastDistance = Float.MAX_VALUE;
            Node currentNode = head.next;
            Node leastNode = head;

            if (currentNode.next != head) {
                while (currentNode != head) {
                    float distance = distanceBetween(currentNode.prev.point, smallestNode.point) +
                            distanceBetween(smallestNode.point, currentNode.point) -
                            distanceBetween(currentNode.prev.point, currentNode.point);
                    if (leastDistance > distance) {
                        leastDistance = distance;
                        leastNode = currentNode.prev;
                    }
                    currentNode = currentNode.next;
                }
            }

            float tailToHeadDistance = distanceBetween(currentNode.prev.point, smallestNode.point) +
                    distanceBetween(smallestNode.point, currentNode.point) -
                    distanceBetween(currentNode.prev.point, currentNode.point);
            if (leastDistance > tailToHeadDistance) {
                leastDistance = tailToHeadDistance;
                leastNode = currentNode.prev;
            }

            smallestNode.prev = leastNode;
            smallestNode.next = leastNode.next;
            leastNode.next.prev = smallestNode;
            leastNode.next = smallestNode;
        }
    }

    public void reset() {
        head = null;
    }

    private class CircularLinkedListIterator implements Iterator<Point> {

        Node current;

        public CircularLinkedListIterator() {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return (current != null);
        }

        @Override
        public Point next() {
            Point toReturn = current.point;
            current = current.next;
            if (current == head) {
                current = null;
            }
            return toReturn;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<Point> iterator() {
        return new CircularLinkedListIterator();
    }


}
