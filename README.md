

## android ParallaxImageView
Android Parallax Effect on ListView / ScrollView

![alt tag](https://raw.githubusercontent.com/milkmidi/android_parallaxImageView/master/preview.gif)


## Code Example

<pre>
mListView = (ListView) this.findViewById( R.id.list_ivew );
mListView.setOnScrollListener( this );
@Override   //AbsListView.OnScrollListener
public void onScroll( AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount ) {
    int childCount = view.getChildCount();
    int listHeight = view.getHeight();
    for ( int i = 0; i < childCount; i++ ) {
        View child = view.getChildAt( i );
        Holder holder = (Holder) child.getTag();
        float top = child.getTop();
        int childHeight = child.getHeight();

        float t = top+childHeight;
        float tot = listHeight+childHeight;

        float fraction = getFraction(t , tot/2 , tot);
        holder.image.setFraction( fraction );
    }
}
</pre>

## Installation

## API Reference

## Contributors

## License

A short snippet describing the license (MIT, Apache, etc.)