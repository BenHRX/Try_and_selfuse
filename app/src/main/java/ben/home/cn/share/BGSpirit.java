package ben.home.cn.share;

import android.graphics.Bitmap;

/**
 * Created by benhuang on 17-8-27.
 */

public class BGSpirit {

    private Bitmap _bitmap;
    private CutClip _cutclip;        /* Intend to show which area to show of the background */
    // private TileImage _tileImg;      /* To use the tile image */

    public BGSpirit(Bitmap _bitmap) {
        this._bitmap = _bitmap;
        _cutclip = new CutClip();         // 在这里卡了很久， inner class也是需要先new的, 否则pointer fail
        _cutclip.setFromX(0);
        _cutclip.setFromY(0);
    }

    public Bitmap getImage() {
        return _bitmap;
    }

    public CutClip getCutclip() {
        return _cutclip;
    }

    public class CutClip {
        private int _x = 0;
        private int _y = 0;

        public int getFromX() {                                 // Move the pic to minus coordinates, 移动到负坐标去令到显示的图片移动。
            return (0 - _x);
        }

        public void setFromX(int _x) {
            this._x = _x;
        }

        public int getFromY() {
            return (0 - _y);
        }

        public void setFromY(int _y) {
            this._y = _y;
        }
    }

    //public void setImage(Bitmap _bitmap) {
    //    this._bitmap = _bitmap;
    //}
}
