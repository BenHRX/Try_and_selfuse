package ben.home.cn.tryforgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import ben.home.cn.share.AnimateSpirit;
import ben.home.cn.share.BGSpirit;
import ben.home.cn.share.ScoreNumber;
import ben.home.cn.share.Spirit;

//import java.util.function.Predicate;

/**
 * Created by benhuang on 17-8-25.
 */

public class Scene extends SurfaceView implements SurfaceHolder.Callback {

    private final SurfaceHolder _holder;
    private BGSpirit _backGround;
    SceneThread _sceneThread;
    private Bitmap _cardBack;
    private ArrayList<Spirit> spirits = new ArrayList<>();
//    private ArrayList<Integer> resIndex;
    private ArrayList<RscPool> resIndex;
    // private int gameLogic = 0;          // Better use a class to descript   // ***
    private SceneControl gameLogic = SceneControl.START;
    private long gameTimeTick = 0;                                          // ***
    private ArrayList<AnimateSpirit> animateListStart = new ArrayList<>();
    private ArrayList<AnimateSpirit> animateListEnd = new ArrayList<>();
    // private AnimateSpirit animate;
    private ScoreNumber score;
    private int record;
    Bitmap tmpEndBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.score16);
    // private Spirit _backGround;
    //Spirit.Coordinates coord;
    // Bitmap gbImage;
    private String TAG = "Scene Tag for Test >>";
    final static int RESOURCE_COUNT = 2;

    public Scene(Context context) {
        super(context);
        // String resString;
        // 创建一个surfaceHolder
        _holder = getHolder();
        _holder.addCallback(this);     // 注册回调函数在自身



        // setFocusableInTouchMode(true);
        /* Setup the gamee background */
        _backGround = new BGSpirit(BitmapFactory.decodeResource(getResources(), R.drawable.background_x3));
        Log.v(TAG, "The background w: " + _backGround.getImage().getWidth() + "height: " + _backGround.getImage().getHeight());
//        Log.v(TAG, "The background w: " + _backGround.getImage().getScaledHeight() + "height: " + _backGround.getImage().getHeight());
        // _backGround = new BGSpirit(BitmapFactory.decodeResource(getResources(), R.drawable.background_x6));
        // _backGround = new Spirit(BitmapFactory.decodeResource(getResources(), R.drawable.background_x6));
        // gbImage = BitmapFactory.decodeResource(getResources(), R.drawable.background_x6);
        // Log.v(TAG, "From X=" + _backGround.getCutclip().getFromX() + ", From Y=" + _backGround.getCutclip().getFromY());
        //coord = _backGround.getCoordinates();
        // 尝试自动获取资源的ID number
        /*resIndex = new ArrayList<Integer>();            // 一定要牢记，List Map什么的一定要初始化

        for(int i=1; i<6; i++){
            resString = "pig"+i+"_x6";                    // 资源名不需要后缀名
            Log.v(TAG, "The resource string >> " + resString);
            // resID.add((Integer)getResources().getIdentifier(resString, "drawable", getContext().getPackageName()));
            String packageName = getContext().getPackageName();
            Resources res = getResources();
            int id = res.getIdentifier(resString,"drawable", packageName);
            resIndex.add(id);

            *//* This work the 2nd solution------
            Field tmpFld;

            try {
                tmpFld = R.drawable.class.getField(resString);
                int id = tmpFld.getInt(new R.drawable());
                Log.v(TAG, "The ID is >> " + id);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }*//*

        }
        String tmpDspString = ">>";
        for (int i:resIndex) {
            tmpDspString = tmpDspString + " " + i + ", ";
        }
        Log.v(TAG, tmpDspString);*/
        /* Start to setup the welcome scene */

        ArrayList<Bitmap> tmpBitmapList = new ArrayList<>();
        tmpBitmapList.add(BitmapFactory.decodeResource(getResources(), R.drawable.welcome01));
        tmpBitmapList.add(BitmapFactory.decodeResource(getResources(), R.drawable.welcome02));
        tmpBitmapList.add(BitmapFactory.decodeResource(getResources(), R.drawable.welcome03));
        tmpBitmapList.add(BitmapFactory.decodeResource(getResources(), R.drawable.welcome04));
        AnimateSpirit tmpAnimate = new AnimateSpirit(tmpBitmapList, 300);
        tmpAnimate.getCoordinates().set_x(_backGround.getImage().getWidth()/2 - tmpAnimate.getImage().getWidth()/2);
        tmpAnimate.getCoordinates().set_y(_backGround.getImage().getHeight()/2 - tmpAnimate.getImage().getHeight()/2);
        animateListStart.add(tmpAnimate);

        Bitmap tmpBitmap;
        tmpBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.campfire);
        tmpAnimate = new AnimateSpirit(tmpBitmap, 5, 1);
        tmpAnimate.getCoordinates().set_x(20);
        tmpAnimate.getCoordinates().set_y(_backGround.getImage().getHeight()/2 - tmpAnimate.getImage().getHeight()/2);
        tmpAnimate.getSpeed().set_velx(2);
        tmpAnimate.getSpeed().set_velxDirection(1);
        animateListStart.add(tmpAnimate);

        // Start setup the end scene
        tmpBitmapList = new ArrayList<>();
        tmpBitmapList.add(BitmapFactory.decodeResource(getResources(), R.drawable.congratulation01));
        tmpBitmapList.add(BitmapFactory.decodeResource(getResources(), R.drawable.congratulation02));
        tmpBitmapList.add(BitmapFactory.decodeResource(getResources(), R.drawable.congratulation03));
        tmpBitmapList.add(BitmapFactory.decodeResource(getResources(), R.drawable.congratulation04));
        tmpAnimate = new AnimateSpirit(tmpBitmapList, 2);
        tmpAnimate.getCoordinates().set_x(_backGround.getImage().getWidth()/2 - tmpAnimate.getImage().getWidth()/2);
        tmpAnimate.getCoordinates().set_y(_backGround.getImage().getHeight()/2 - tmpAnimate.getImage().getHeight()/2);
        animateListEnd.add(tmpAnimate);

        /* Start to setup the game scene */
        resIndex = new ArrayList<>();
        for(int i=1; i<6; i++) {
            //String tmpString = "pig"+i+"_x6";
            String tmpString = "pig"+i+"_x3";
//            int tmpID = getResources().getIdentifier(tmpString, "drawable", getContext().getPackageName());
//            RscPool tmpRsc = new RscPool(getResources().getIdentifier(tmpString, "drawable", getContext().getPackageName()), RESOURCE_COUNT);
            resIndex.add(new RscPool(getResources().getIdentifier(tmpString, "drawable",
                    getContext().getPackageName()), RESOURCE_COUNT));
        }

        /* Start setup the score class */
        HashMap<Integer, Bitmap> scoreList = new HashMap<>();
        for (int i = 0; i <= 9 ; i++) {
            String tmpString = "num" + i;
            tmpBitmap = BitmapFactory.decodeResource(getResources(),
                    getResources().getIdentifier(tmpString, "drawable", getContext().getPackageName()));
            scoreList.put(i, tmpBitmap);
        }
        score = new ScoreNumber(this.getContext(), scoreList);

        /*for(RscPool tmpResPool : resIndex){
            Log.v(TAG, tmpResPool.toString());
        }
        Log.v(TAG, "Index 0 is " + resIndex.get(0).toString());
        Log.v(TAG, "Index 1 is " + resIndex.get(1).toString());
        Log.v(TAG, "Index 2 is " + resIndex.get(2).toString());
        Log.v(TAG, "Index 3 is " + resIndex.get(3).toString());
        Log.v(TAG, "Index 4 is " + resIndex.get(4).toString());*/
//        gameInit();                                     // Initialize logic put to here
        gameLogic = SceneControl.START;
    }

    public void gameInit(){
        // 1st step, just 8 cards to check.
        // Future should has a parameter or method to get the hard level *******
        // Card NO 5 is card back;
        int cardCount = 8;
//        spirits = new ArrayList<Spirit>();
        if(!spirits.isEmpty()){
            spirits.clear();
        }
        for(RscPool tmpPool : resIndex){
            tmpPool.set_PoolCount(RESOURCE_COUNT);
        }
        Random rnd = new Random(System.currentTimeMillis());
        for(int c = 0; c < cardCount; c++){
            int cardFaceSequenece = Math.abs(rnd.nextInt()%(cardCount/2));
            if(resIndex.get(cardFaceSequenece).chk_PoolCount()){
                resIndex.get(cardFaceSequenece).consumePool();
                Bitmap tmpBitmap = BitmapFactory.decodeResource(getResources(), resIndex.get(cardFaceSequenece).get_id());
                Log.v(TAG, "The pic height : " + tmpBitmap.getHeight() + "width : " + tmpBitmap.getWidth());
                spirits.add(new Spirit(tmpBitmap));
                spirits.get(c).set_bitmapSequence(cardFaceSequenece);
                spirits.get(c).set_alive(true);
                spirits.get(c).getCoordinates().set_y((c/3) * tmpBitmap.getHeight() + 50);
                spirits.get(c).getCoordinates().set_x((c%3) * tmpBitmap.getWidth() + 50);
            }
            else{
                c = c - 1;
            }
        }
        _cardBack = BitmapFactory.decodeResource(getResources(),R.drawable.pig5_x3);
        record = 1800;
        // gameLogic = 0;
        gameLogic = SceneControl.REMEMBER;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 将绘画部分放于此处，便于代码整理
        canvas.drawBitmap(_backGround.getImage(), _backGround.getCutclip().getFromX(), _backGround.getCutclip().getFromY(), null);
        // 绘画其他精灵 - 静止
        // 绘画其他精灵 - 动态
//        if (gameLogic == 0) {
//        if(gameLogic == SceneControl.REMEMBER) {
//            for (Spirit sp : spirits) {
//                canvas.drawBitmap(sp.getImage(), sp.getCoordinates().get_x(), sp.getCoordinates().get_y(), null);
//            }
        switch(gameLogic){
            case START:
                /*canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.welcome),
                        300,400,null);*/
                for(AnimateSpirit tmpanimate : animateListStart){
                    canvas.drawBitmap(tmpanimate.getImage(),tmpanimate.getCoordinates().get_x(),
                            tmpanimate.getCoordinates().get_y(), null);
                }
                break;
            case REMEMBER:
                for (Spirit sp : spirits) {
                    canvas.drawBitmap(sp.getImage(), sp.getCoordinates().get_x(), sp.getCoordinates().get_y(), null);
                }
                canvas.drawBitmap(score.getScoreImage(record), 0, 0, null);
                break;
            case GUESS:
                for (Spirit sp : spirits) {
                    canvas.drawBitmap(_cardBack, sp.getCoordinates().get_x(), sp.getCoordinates().get_y(), null);
                }
                canvas.drawBitmap(score.getScoreImage(record), 0, 0, null);
                break;
            case CLICK1:
                for (Spirit sp : spirits) {
                    if(!sp.is_alive()) {
                        canvas.drawBitmap(sp.getImage(), sp.getCoordinates().get_x(), sp.getCoordinates().get_y(), null);
                    }
                    else{
                        canvas.drawBitmap(_cardBack, sp.getCoordinates().get_x(), sp.getCoordinates().get_y(), null);
                    }
                }
                canvas.drawBitmap(score.getScoreImage(record), 0, 0, null);
                break;
            case END:
                /*Paint paint = new Paint();
                paint.setColor(Color.RED);
                canvas.drawText("Congratulations!!!!!!", 100, 100, paint);*/
                for(AnimateSpirit tmpanimate : animateListEnd){
                    canvas.drawBitmap(tmpanimate.getImage(),tmpanimate.getCoordinates().get_x(),
                            tmpanimate.getCoordinates().get_y(), null);
                }
                canvas.drawBitmap(tmpEndBitmap, 250, 350, null);
                canvas.drawBitmap(score.getScoreImage(record), 260 + tmpEndBitmap.getWidth(), 350, null);
                break;
            default:
        }

//        }
//        if(gameLogic == 1){
/*        if(gameLogic == SceneControl.GUESS){
            for (Spirit sp : spirits) {
                canvas.drawBitmap(_cardBack, sp.getCoordinates().get_x(), sp.getCoordinates().get_y(), null);
            }
            break;
        }*/
//        if(gameLogic == 2){
        /*if(gameLogic == SceneControl.CLICK1){
            for (Spirit sp : spirits) {
                if(!sp.is_alive()) {
                    canvas.drawBitmap(sp.getImage(), sp.getCoordinates().get_x(), sp.getCoordinates().get_y(), null);
                }
                else{
                    canvas.drawBitmap(_cardBack, sp.getCoordinates().get_x(), sp.getCoordinates().get_y(), null);
                }
            }
        }*/
//        if(gameLogic == 4) {
        /*if(gameLogic == SceneControl.END){
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            canvas.drawText("Congratulations!!!!!!", 100, 100, paint);
        }*/
    }

    private void GameProcess() {
        switch(gameLogic){
//            case 0:                         // 显示牌底
            case START:
                break;
            case REMEMBER:
                gameTimeTick++;
                if(gameTimeTick > 1*60){
                    // gameLogic = 1;
                    gameLogic = SceneControl.GUESS;
                    gameTimeTick = 0;
                }
                break;
//            case 1:                         // 显示牌面允许猜
            case GUESS:
                break;
//            case 2:                         // 一张牌被点击开了
            case CLICK1:
                break;
//            case 3:                         // 清除已经被猜中的卡
            case CLICK2:
            /*    spirits.removeIf(new Predicate<Spirit>() {           // Java 1.8新特征
                    @Override
                    public boolean test(Spirit spirit) {
                        if(!spirit.is_alive()){
                            return true;
                        }
                        else{
                            return false;
                        }
                    }
                });*/
                for(int i=(spirits.size()-1); i>=0; i--){
                    if(!spirits.get(i).is_alive()){
                        spirits.remove(i);
                    }
                }
                /*Spirit delSp = null;
                boolean clearUp = false;
                while(!clearUp) {
                    *//*while(spirits.iterator().hasNext()){
                        Spirit sp3 = spirits.iterator().next();
                        if(!sp3.is_alive()){
                            spirits.iterator().remove();
                        }
                    }*//*
                    for (Spirit sp3 : spirits) {
                        if (!sp3.is_alive()) {
                            delSp = sp3;
                            clearUp = false;
                            break;
                        }
                        clearUp = true;
                    }
                    if(!clearUp) {
                        spirits.remove(delSp);
                    }
                    if(spirits.isEmpty()){
                        clearUp = true;
                    }
                }*/
                if(!spirits.isEmpty()){
//                    gameLogic = 1;
                    gameLogic = SceneControl.GUESS;
                }
                else{
//                    gameLogic = 4;
                    gameLogic = SceneControl.END;
                }
                break;
            default:
                Log.v(TAG, "The default logic hit.");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // return super.onTouchEvent(event);
        // 这个触摸监控是已经放入到View下面了吗？
//        if(event.getAction() != MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_MOVE){
        if(event.getAction() != MotionEvent.ACTION_DOWN){
            return(false);
        }

        synchronized (_holder){
            float x = event.getX();
            float y = event.getY();

            Log.v(TAG, "X=" + x + ", Y=" + y);
            // _backGround.getCutclip().setFromX((int)x);
            // _backGround.getCutclip().setFromY((int)y);
            switch(gameLogic) {
//                case 1:
                case START:
                    gameLogic = SceneControl.REMEMBER;
                    gameInit();
                    break;
                case REMEMBER:
                    break;
                case GUESS:
                    for (Spirit sp1 : spirits) {
                        if (sp1.is_click(x, y)) {
                            sp1.set_alive(false);
//                            gameLogic = 2;
                            gameLogic = SceneControl.CLICK1;
                        }
                    }
                    break;
//                case 2:
                case CLICK1:
                    Spirit tmpSp = null;
                    Spirit clickSp = null;
                    for (Spirit sp2 : spirits) {
                        if (sp2.is_click(x, y)) {
                            if(sp2.is_alive())
                                clickSp = sp2;
                            else
                                return(false);          // Click the already guess then leave.
                        }
                        if (!sp2.is_alive()) {
                            tmpSp = sp2;
                        }
                    }
                    if(clickSp == null || tmpSp == null){
                        return(false);                  // No spirit was clicked or previous choose
                    }
                    if (clickSp.get_bitmapSequence() == tmpSp.get_bitmapSequence()) {
                        clickSp.set_alive(false);
//                        gameLogic = 3;
                        gameLogic = SceneControl.CLICK2;
                    } else {
                        tmpSp.set_alive(true);
//                        gameLogic = 1;
                        gameLogic = SceneControl.GUESS;
                    }
                    break;
//                case 3:
                case CLICK2:
                    break;
                case END:
                    gameLogic = SceneControl.START;
                    break;
                default:
            }

            return(true);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder _holder) {
        // 启动线程
        _sceneThread = new SceneThread();
        _sceneThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder _holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder _holder) {
        // 停止线程
        _sceneThread.requestWaitAndExit();
        _sceneThread = null;
    }

    private class SceneThread extends Thread {

        private boolean _threadDone;

        SceneThread() {
            super();
            this._threadDone = false;
        }

        @Override
        public void run() {
            super.run();
            SurfaceHolder surfaceHolder = _holder;
            // Random rnd = new Random(System.currentTimeMillis());

            while(!this._threadDone){
                Canvas canvas = surfaceHolder.lockCanvas();     // 应该是为了线程同步。 先占据了Canvas桌面响应
                synchronized (surfaceHolder){                   // 大括号内是原子操作
                    // Draw the game
                 /*   int x = 0, y = 10;
                    int height = 100;
                    Paint paint = new Paint();
                    paint.setColor(Color.RED);
                    canvas.drawLine(x, y, x + canvas.getWidth() - 1, y, paint);
                    canvas.drawLine(x, y + height - 1, x + canvas.getWidth(), y + height - 1, paint);
                    paint.setColor(Color.WHITE);
                    canvas.drawPoint(x + 5, y + 5, paint); */
                    // _backGround = new BackGround(BitmapFactory.decodeResource(getResources(), R.drawable.background_x6));
                    // Log.v("From X=" + _backGround.getCutclip().getX() + ", From Y=" + _backGround.getCutclip().getY(), VIEW_LOG_TAG);
                    // 按照书本里面的方法，建议将绘画放于重写onDraw函数里面。
                    // canvas.drawBitmap(_backGround.getImage(), _backGround.getCutclip().getFromX(), _backGround.getCutclip().getFromY(), null);
                    GameProcess();
                    AnimateControl();
                    ScoreRecord();
                    onDraw(canvas);
                    // Bitmap gbImage = BitmapFactory.decodeResource(getResources(), R.drawable.background_x6);
                    // Bitmap gbImage = _backGround.getImage();
                    // canvas.drawBitmap(gbImage, 0, 0, null);
                }
                // 解锁Canvas桌面
                surfaceHolder.unlockCanvasAndPost(canvas);

                try {
                    Thread.sleep(1000/60);      // 1000ms分成份，每1/60秒醒来运行一次
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void requestWaitAndExit() {
            _threadDone = true;
            try {
                join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void ScoreRecord() {
        switch(gameLogic){
            case REMEMBER:
            case GUESS:
            case CLICK1:
            case CLICK2:
                record--;
                if(record < 0){
                    record = 0;
                }
                break;
            default:
                break;
        }
        /*if(gameLogic == SceneControl.REMEMBER || gameLogic == SceneControl.GUESS
                || gameLogic == SceneControl.CLICK1 || gameLogic == SceneControl.CLICK2)
        {
            record--;
            if(record < 0){
                record = 0;
            }
        }*/
    }

    private void AnimateControl() {
        switch(gameLogic){
            case START:
                for(AnimateSpirit tmpAnimate : animateListStart){
                    tmpAnimate.statusUpdate();
                    tmpAnimate.runBySequence(tmpAnimate.getFrameTime());
                }
                break;
            case END:
                for(AnimateSpirit tmpAnimate : animateListEnd){
                    tmpAnimate.statusUpdate();
                    tmpAnimate.runBySequence(tmpAnimate.getFrameTime());
                }
                break;
            default:
                break;
        }
    }

}
