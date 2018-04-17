package com.sincerly.fightcontentview.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.sincerly.fightcontentview.R;


/**
 * Created by Administrator on 2018/4/17 0017.
 */

public class TDialog extends Dialog {

	private Handler handler = new Handler();
	private TextView iv;
	private Context context;

	public TDialog(Context context) {
		super(context, R.style.EDialog);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_view);
		iv = (TextView) findViewById(R.id.imageView);

		changeWindow(this);
	}

	@Override
	public void show() {
		super.show();

		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Animation rotate = AnimationUtils.loadAnimation(context, R.anim.anim_rotation);
				rotate.setRepeatCount(-1);
				rotate.setRepeatMode(Animation.RESTART);
				iv.setAnimation(rotate);
			}
		}, 50);
	}

	void changeWindow(Dialog d) {
		Window window =getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.alpha = (float) 0.5;
		window.setAttributes(params);
		d.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				WindowManager.LayoutParams params =getWindow().getAttributes();
				params.alpha = (float) 1.0;
				getWindow().setAttributes(params);
			}
		});
	}

	@Override
	public void dismiss() {
		super.dismiss();
		iv.clearAnimation();
	}
}
