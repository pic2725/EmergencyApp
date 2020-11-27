package edu.utah.cs4530.emergency.ui.contacts;

public interface ItemTouchHelperListener {
    boolean onItemMove(int from_position, int to_position);
    void onItemSwipe(int position);
}
