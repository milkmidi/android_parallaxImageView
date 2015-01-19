package com.milkmidi.parallax;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.milkmidi.parallax.view.ParallaxFitImageView;


public class MainActivity extends ActionBarActivity implements AbsListView.OnScrollListener {

    private ListView mListView;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        mListView = (ListView) this.findViewById( R.id.list_ivew );
        mListView.setOnScrollListener( this );

        mListView.setAdapter( new ParallaxAdpater() );
    }


    @Override   // AbsListView.OnScrollListener
    public void onScrollStateChanged( AbsListView view, int scrollState ) {

    }

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
    private static float getFraction(float i, float selected, float tot) {
        // Based on the current position, returns the F (-1 to 1) of the current item (i) which is its distance to the selected item
        float f;
        f = i - selected;
        f /= tot / 2;
        while (f < -1) f += 2;
        while (f > 1)  f -= 2;
        return f;
    }

    class ParallaxAdpater extends BaseAdapter{
        int[] imageArr;

        public ParallaxAdpater(){
            this.imageArr = new int[]{
                    R.drawable.image0,
                    R.drawable.image1,
                    R.drawable.image2,
                    R.drawable.image3,
                    R.drawable.image4,
                    R.drawable.image5
            };
        }

        @Override
        public int getCount() {
            return 50;
        }

        @Override
        public Object getItem( int position ) {
            return null;
        }

        @Override
        public long getItemId( int position ) {
            return 0;
        }

        @Override
        public View getView( int position, View convertView, ViewGroup parent ) {
            View view = convertView;
            final Holder holder;
            if ( view == null ){
                view = LayoutInflater.from( MainActivity.this  ).inflate( R.layout.item, null );
                holder = new Holder();
                holder.image = (ParallaxFitImageView) view.findViewById( R.id.image );
                holder.text = (TextView) view.findViewById( R.id.text );
                view.setTag( holder );
            }else{
                holder = (Holder) view.getTag();
            }

            holder.image.reset();
            holder.image.setImageBitmap( null );
            holder.text.setText( position +"" );


            //
            LoadBitmapAsyncTask t = new LoadBitmapAsyncTask(){
                @Override
                protected void onPostExecute( Bitmap bitmap ) {
                    super.onPostExecute( bitmap );
                    holder.image.setImageBitmap( bitmap );
                }
            };
            t.execute( imageArr[position % 6] );


            return view;
        }


    }
    class Holder{
        ParallaxFitImageView image;
        TextView text;
    }
    class LoadBitmapAsyncTask extends AsyncTask<Integer , Void , Bitmap>{

        @Override
        protected Bitmap doInBackground( Integer... params ) {
            return BitmapFactory.decodeResource( getResources() , params[0] );
        }
    }

}

