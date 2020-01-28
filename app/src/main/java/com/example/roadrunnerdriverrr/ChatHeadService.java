package com.example.roadrunnerdriverrr;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;



import java.util.Random;

public class ChatHeadService extends Service {
    ChatHeadService me = this;
    private NotificationManager mNM;
    private String notificationChannelId= "mynotification";
    private String notificationChannelName = "mynotification";


    MyDataManager myDataManager = new MyDataManager();
    private WindowManager mWindowManager;
    private View.OnTouchListener touchListener;
    private View mChatHeadView;
    LinearLayout ibMenu,ibMenu2;
    ImageView closeButton;
    LinearLayout chat_head_root;
    LinearLayout chat_head_root1;
    LinearLayout chat_head_root2;

    boolean closeVisMarker = false;
    int LAYOUT_FLAG;
    WindowManager.LayoutParams params;
    ImageButton ibA,ibD,ibO,ibX;
    LinearLayout addPostcodeLayout;
    boolean addPostcodeSwitch = false;
    EditText etPostcode;



    public ChatHeadService() {

    }




    @Override
    public void onCreate() {
        super.onCreate();
        this.me = this;
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = new NotificationChannel(notificationChannelId, notificationChannelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNM.createNotificationChannel(notificationChannel);
        }
        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();
        mChatHeadView = LayoutInflater.from(this).inflate(R.layout.layout_chat_head, null);





        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        //Add the view to the window.
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 100;

        final WindowManager.LayoutParams paramsClose = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        paramsClose.gravity = Gravity.TOP | Gravity.END;
        paramsClose.x = 0;
        paramsClose.y = 100;

        final WindowManager.LayoutParams paramsOpen = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        paramsOpen.gravity = Gravity.TOP | Gravity.END;
        paramsOpen.x = 50;
        paramsOpen.y =  100;

        final WindowManager.LayoutParams paramsOpenMenu = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        paramsOpenMenu.gravity = Gravity.TOP | Gravity.END;

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mChatHeadView, params);

        chat_head_root = (LinearLayout) mChatHeadView.findViewById(R.id.chat_head_root);

        chat_head_root1 = chat_head_root.findViewById(R.id.chat_head_root1);
        chat_head_root2 = chat_head_root.findViewById(R.id.chat_head_root2);


        final ImageView chatHeadImage = (ImageView) mChatHeadView.findViewById(R.id.chat_head_profile_iv);
        touchListener = new View.OnTouchListener() {
            private int lastAction;
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();

                        lastAction = event.getAction();
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (lastAction == MotionEvent.ACTION_DOWN) {
                            Log.v("lastAction","down");
                        }
                        lastAction = event.getAction();
                        float resX = initialX - params.x;
                        if(resX < 0){
                            resX = -resX;
                        }
                        float resY = initialY - params.y;
                        if(resY < 0){
                            resY = -resY;
                        }
                        if((resX < 15)&&(resY < 15)){
                            if(!closeVisMarker) {
                                //mWindowManager.addView(mChatHeadCloseView,paramsClose);
                                createOpenMenu();
                                chat_head_root1.addView(ibMenu2);
                                chat_head_root2.addView(ibMenu);


                                closeVisMarker = !closeVisMarker;

                            }else {
                                //mWindowManager.removeView(mChatHeadCloseView);
                                if(addPostcodeSwitch){
                                    hideAddPostcodeAlert();
                                    addPostcodeSwitch = !addPostcodeSwitch;
                                }
                                chat_head_root1.removeView(ibMenu2);
                                chat_head_root2.removeView(ibMenu);
                                closeVisMarker = !closeVisMarker;
                            }


                        }
                        if(myDataManager.getStickyEdges()) {
                            if (params.x < ((myDataManager.getScreenWidth() / 2) - 100)) {
                                //left
                                int beginValue = params.x;
                                int endValue = 50;

                                ValueAnimator animator = ValueAnimator.ofInt(beginValue, endValue);
                                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator animation) {

                                        params.x = (int) animation.getAnimatedValue();
                                        //params.y =  (int)animation.getAnimatedValue();
                                        mWindowManager.updateViewLayout(mChatHeadView, params);
                                    }
                                });
                                animator.start();
                            } else {
                                //right
                                int beginValue = params.x;
                                int endValue = myDataManager.getScreenWidth() - 50;

                                ValueAnimator animator = ValueAnimator.ofInt(beginValue, endValue);
                                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator animation) {

                                        params.x = (int) animation.getAnimatedValue();
                                        //params.y =  (int)animation.getAnimatedValue();
                                        mWindowManager.updateViewLayout(mChatHeadView, params);
                                    }
                                });
                                animator.start();
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mChatHeadView, params);
                        lastAction = event.getAction();
                        return true;
                }
                return false;
            }
        };
        chatHeadImage.setOnTouchListener(touchListener);
        //addHeadAnim();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_NOT_STICKY;
    }

    private void createOpenMenu(){
        ibMenu = new LinearLayout(chat_head_root.getContext());
        LinearLayout.LayoutParams paramsibmenu2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        paramsibmenu2.setMargins(50,0,10,0);
        LinearLayout.LayoutParams paramsibmenu1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        paramsibmenu1.setMargins(100,0,0,5);

        ibMenu.setOrientation(LinearLayout.VERTICAL);
        ibD = new ImageButton(ibMenu.getContext());
        ibD.setImageResource(R.drawable.bd);
        ibD.setBackgroundColor(Color.TRANSPARENT);
        ibD.setLayoutParams(paramsibmenu1);

        ibMenu.addView(ibD);
        ibA = new ImageButton(ibMenu.getContext());
        ibA.setImageResource(R.drawable.ba);
        ibA.setBackgroundColor(Color.TRANSPARENT);
        ibA.setLayoutParams(paramsibmenu1);

        ibMenu.addView(ibA);

        ibD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeliveryConfirmationAlert();
            }
        });
        ibA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(addPostcodeSwitch){
                    if(etPostcode.getText().toString().equals("")){

                    }else {
                        myDataManager.addPostcodeString(etPostcode.getText().toString());
                    }
                    hideAddPostcodeAlert();

                }else {
                    showAddPostcodeAlert();
                    //popUpEditText();
                }
                addPostcodeSwitch = !addPostcodeSwitch;*/
                //myDataManager.addPostcodeFromCurLoc();
                Toast.makeText(getApplicationContext(), "Current Postcode: " + myDataManager.getCurPostcode(), Toast.LENGTH_LONG).show();
                if(myDataManager.getAutoGetPostcodeFromLoc()){
                    myDataManager.addPostcodeString(myDataManager.getCurPostcode());
                }else {
                    Intent dialogIntent = new Intent(me, MainActivity.class);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                }
                updateNotification();

            }
        });

        ibMenu2 = new LinearLayout(chat_head_root.getContext());
        //ibMenu2.setLayoutParams(paramsibmenu1);
        ibMenu2.setOrientation(LinearLayout.HORIZONTAL);
        ibO = new ImageButton(ibMenu2.getContext());
        ibO.setImageResource(R.drawable.bo);
        ibO.setBackgroundColor(Color.TRANSPARENT);
        ibO.setLayoutParams(paramsibmenu2);
        ibMenu2.addView(ibO);
        ibX = new ImageButton(ibMenu2.getContext());
        ibX.setImageResource(R.drawable.bx);
        ibX.setBackgroundColor(Color.TRANSPARENT);
        ibX.setLayoutParams(paramsibmenu2);
        ibMenu2.addView(ibX);
        ibO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addPostcodeSwitch){
                    hideAddPostcodeAlert();
                    addPostcodeSwitch = !addPostcodeSwitch;
                }
                chat_head_root1.removeView(ibMenu2);
                chat_head_root2.removeView(ibMenu);
                closeVisMarker = !closeVisMarker;
                Intent dialogIntent = new Intent(me, MainActivity.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialogIntent);
            }
        });
        ibX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });
    }
    private void popUpEditText() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Comments");

        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // do something here on OK

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }
    private void showDeliveryConfirmationAlert(){
        int result = myDataManager.sendDeliveryConfirmationSMS();
        if(result == 0){
            Toast.makeText(getApplicationContext(), "No number selected!", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
        }

    }

    private void showAddPostcodeAlert(){
        final WindowManager.LayoutParams postcodeAlertParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        postcodeAlertParams.gravity = Gravity.TOP | Gravity.START;
        postcodeAlertParams.x = params.x + 400;
        postcodeAlertParams.y = params.y + 600;


        addPostcodeLayout = new LinearLayout(chat_head_root.getContext());
        addPostcodeLayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView tvLabel = new TextView(addPostcodeLayout.getContext());
        tvLabel.setText("GL");
        tvLabel.setBackgroundColor(Color.argb(50,211,211,211));
        tvLabel.setTextSize(28.0f);


        etPostcode = new EditText(addPostcodeLayout.getContext());
        etPostcode.setText("");
        etPostcode.setWidth(200);
        etPostcode.setTextSize(28.0f);
        etPostcode.setBackgroundColor(Color.argb(50,211,211,211));
        //etPostcode.requestFocus();

        etPostcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showKeyboard();
                etPostcode.requestFocus();
                etPostcode.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(etPostcode, InputMethodManager.SHOW_IMPLICIT);
                    }
                }, 1000);
            }
        });


        addPostcodeLayout.addView(tvLabel);
        addPostcodeLayout.addView(etPostcode);
        mWindowManager.addView(addPostcodeLayout, postcodeAlertParams);
    }

    private void hideAddPostcodeAlert(){
        Log.v("aaa","bb:" + etPostcode.getText().toString());
        try {
            mWindowManager.removeView(addPostcodeLayout);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showKeyboard(){

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        etPostcode.requestFocus();
    }

    public void closeKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private void addHeadAnim(){
        ImageView tmp = (ImageView) mChatHeadView.findViewById(R.id.chat_head_profile_iv);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0.85f, 1f, 0.85f, tmp.getWidth() / 2.0f, tmp.getHeight() / 2.0f);
        scaleAnimation.setDuration(750); // scale to 3 times as big in 3 seconds
        scaleAnimation.setInterpolator(this, android.R.interpolator.accelerate_decelerate);
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        scaleAnimation.setRepeatMode(ScaleAnimation.REVERSE);
        tmp.startAnimation(scaleAnimation);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
                Log.d("ScaleActivity", "Scale started...");
            }

            public void onAnimationEnd(Animation animation) {
                Log.d("ScaleActivity", "Scale ended...");
            }

            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }



    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = "Total: " + myDataManager.getTotal() + " ";

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.pizzachat)  // the status icon
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle("Widget is ON")  // the label of the entry
                .setContentText(text)  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .setChannelId(notificationChannelId)
                .build();

        // Send the notification.
        mNM.notify(1, notification);
    }

    private void updateNotification(){
        mNM.cancel(1);
        showNotification();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNM.cancel(1);
        if (mChatHeadView != null){
            mWindowManager.removeView(mChatHeadView);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public class LocalBinder extends Binder {
        ChatHeadService getService() {
            return ChatHeadService.this;
        }
    }
}