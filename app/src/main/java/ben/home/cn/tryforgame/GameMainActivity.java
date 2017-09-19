package ben.home.cn.tryforgame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

public class GameMainActivity extends AppCompatActivity {

    private static final String TAG = "MainAct >";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main);
        // setContentView(new MyView(this));      // 用Canvas绘图
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Log.v(TAG, "h px= " + dm.heightPixels + ", w px= " + dm.widthPixels);
        // 1184 / 768
        Log.v(TAG, "xdpi= " + dm.xdpi + ", ydpi= " + dm.ydpi);
        Log.v(TAG, "Scale = " + dm.scaledDensity);
        Log.v(TAG, "Destiny = " + dm.density + " Destiny DPI = " + dm.density);
        setContentView(new Scene(this));
    }

    //private class MyView extends View{

    //    public MyView(Context context) {
    //      super(context);
    //    }

    //    public void onDraw(Canvas canvas){
    //        int x = 0, y = 10;
    //        int height = 100;
    //        Paint paint = new Paint();
    //        paint.setColor(Color.RED);
    //        canvas.drawLine(x, y, x+canvas.getWidth()-1, y, paint);
    //        canvas.drawLine(x, y+height-1, x+canvas.getWidth(), y+height-1, paint);
    //        paint.setColor(Color.WHITE);
    //       canvas.drawPoint(x+5, y+5, paint);
    //    }
    // }
}
