package org.FieldCongratulations.tydlk;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.util.*;
import java.io.*;

public class MainActivity extends Activity 
{
	protected indexInfo info;
	protected Context context = this;
	protected Dialog d = null,d1 = null;
	protected boolean isInit = false;
	protected ConnectionChecker connectionChecker;

	private void showDialog() {
		d = new Dialog(context);
		d.setContentView(R.layout.dialog);
		d.setTitle("拉取考试信息");
		d.show();
	}

	private void showDialog(String i) {
		d1 = new Dialog(context);
		d1.setContentView(R.layout.dialog);
		d1.setTitle("解析中");
		d1.show();
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		connectionChecker = new ConnectionChecker(this);
		((Button)findViewById(R.id.mainButtonSubmit)).setOnLongClickListener(new OnLongClickListener(){
				@Override
				public boolean onLongClick(View p1) {
					connectionChecker.refresh();
					if (!connectionChecker.isConnected) {
						Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
						return false;
					}
					showDialog();
					new Thread(new Runnable(){
							@Override
							public void run() {
								try {
									info = new indexInfo();
									runOnUiThread(new Runnable(){
											@Override
											public void run() {
												((Spinner)findViewById(R.id.mainSpinnerExam)).setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, info.indexExamResult()));
												Toast.makeText(context, "拉取完成,长按可重新拉取考试信息", Toast.LENGTH_SHORT).show();
											}
										});
									isInit = true;
								}  catch (final Exception e) {
									runOnUiThread(new Runnable(){
											@Override
											public void run() {
												Toast.makeText(context, "拉取考试信息失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
											}
										});
								}
								d.cancel();
							}
						}).start();
					return false;
				}
			});
		((Button)findViewById(R.id.mainButtonSubmit)).setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1) {
					//boolean tIsInit = isInit;
					connectionChecker.refresh();
					if (!connectionChecker.isConnected) {
						Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
						return;
					}
					showDialog("");
					if (!isInit) {
						d1.cancel();
						showDialog();
						new Thread(new Runnable(){
								@Override
								public void run() {
									try {
										info = new indexInfo();
										runOnUiThread(new Runnable(){
												@Override
												public void run() {
													((Spinner)findViewById(R.id.mainSpinnerExam)).setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, info.indexExamResult()));
													Toast.makeText(context, "拉取完成,长按可重新拉取考试信息", Toast.LENGTH_SHORT).show();
													d.cancel();
												}
											});
										isInit = true;
									}  catch (final Exception e) {
										runOnUiThread(new Runnable(){
												@Override
												public void run() {
													Toast.makeText(context, "拉取考试信息失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
													d.cancel();
												}
											});
									}
								}
							}).start();
						return;
					}
					if (((EditText)findViewById(R.id.mainEditTextNumber)).getText().toString().equals("")) {
						Toast.makeText(context, "未输入考生号", Toast.LENGTH_SHORT).show();
						return;
					}
					try {
						info.requestResult(((EditText)findViewById(R.id.mainEditTextNumber)).getText().toString(), 
										   info.serial[((Spinner)findViewById(R.id.mainSpinnerExam)).getSelectedItemPosition()]);
					} catch (final Exception e) {
						Toast.makeText(context, "查询错误: " + e.getMessage(), Toast.LENGTH_SHORT).show();
						d1.cancel();
						return;
					}
					((TextView)findViewById(R.id.mainTextViewUserInfo)).setText(info.indexStudentInfo());

					subjects s = info.indexSubjects();

					((TextView)findViewById(R.id.mainTextViewTOTSCORE)).setText(s.total.score);
					((TextView)findViewById(R.id.mainTextViewTOTSCORE0)).setText(s.total.score);
					((TextView)findViewById(R.id.mainTextViewTOTSCHOOLS)).setText(s.total.schoolSerial);
					((TextView)findViewById(R.id.mainTextViewTOTEXAMS)).setText(s.total.examSerial);
					((TextView)findViewById(R.id.mainTextViewTOTCLASSS)).setText(s.total.classSerial);

					((TextView)findViewById(R.id.mainTextViewCHNSCORE)).setText(s.chinese.score);
					((TextView)findViewById(R.id.mainTextViewCHNSCORE0)).setText(s.chinese.score);
					((TextView)findViewById(R.id.mainTextViewCHNSCHOOLS)).setText(s.chinese.schoolSerial);
					((TextView)findViewById(R.id.mainTextViewCHNEXAMS)).setText(s.chinese.examSerial);
					((TextView)findViewById(R.id.mainTextViewCHNCLASSS)).setText(s.chinese.classSerial);

					((TextView)findViewById(R.id.mainTextViewMATSCORE)).setText(s.math.score);
					((TextView)findViewById(R.id.mainTextViewMATSCORE0)).setText(s.math.score);
					((TextView)findViewById(R.id.mainTextViewMATSCHOOLS)).setText(s.math.schoolSerial);
					((TextView)findViewById(R.id.mainTextViewMATEXAMS)).setText(s.math.examSerial);
					((TextView)findViewById(R.id.mainTextViewMATCLASSS)).setText(s.math.classSerial);

					((TextView)findViewById(R.id.mainTextViewENGSCORE)).setText(s.english.score);
					((TextView)findViewById(R.id.mainTextViewENGSCORE0)).setText(s.english.score);
					((TextView)findViewById(R.id.mainTextViewENGSCHOOLS)).setText(s.english.schoolSerial);
					((TextView)findViewById(R.id.mainTextViewENGEXAMS)).setText(s.english.examSerial);
					((TextView)findViewById(R.id.mainTextViewENGCLASSS)).setText(s.english.classSerial);

					((TextView)findViewById(R.id.mainTextViewPHYSCORE)).setText(s.physics.score);
					((TextView)findViewById(R.id.mainTextViewPHYSCORE0)).setText(s.physics.score);
					((TextView)findViewById(R.id.mainTextViewPHYSCHOOLS)).setText(s.physics.schoolSerial);
					((TextView)findViewById(R.id.mainTextViewPHYEXAMS)).setText(s.physics.examSerial);
					((TextView)findViewById(R.id.mainTextViewPHYCLASSS)).setText(s.physics.classSerial);

					((TextView)findViewById(R.id.mainTextViewCHESCORE)).setText(s.chemistry.score);
					((TextView)findViewById(R.id.mainTextViewCHESCORE0)).setText(s.chemistry.score);
					((TextView)findViewById(R.id.mainTextViewCHESCHOOLS)).setText(s.chemistry.schoolSerial);
					((TextView)findViewById(R.id.mainTextViewCHEEXAMS)).setText(s.chemistry.examSerial);
					((TextView)findViewById(R.id.mainTextViewCHECLASSS)).setText(s.chemistry.classSerial);

					((TextView)findViewById(R.id.mainTextViewBIOSCORE)).setText(s.biology.score);
					((TextView)findViewById(R.id.mainTextViewBIOSCORE0)).setText(s.biology.score);
					((TextView)findViewById(R.id.mainTextViewBIOSCHOOLS)).setText(s.biology.schoolSerial);
					((TextView)findViewById(R.id.mainTextViewBIOEXAMS)).setText(s.biology.examSerial);
					((TextView)findViewById(R.id.mainTextViewBIOCLASSS)).setText(s.biology.classSerial);

					((TextView)findViewById(R.id.mainTextViewTOTCLASSA)).setText(s.total.classAvg);
					((TextView)findViewById(R.id.mainTextViewTOTCLASSM)).setText(s.total.classMax);
					((TextView)findViewById(R.id.mainTextViewTOTSCHOOLSA)).setText(s.total.schoolAvg);
					((TextView)findViewById(R.id.mainTextViewTOTSCHOOLSM)).setText(s.total.schoolMax);
					((TextView)findViewById(R.id.mainTextViewTOTEXAMSA)).setText(s.total.examAvg);
					((TextView)findViewById(R.id.mainTextViewTOTEXAMSM)).setText(s.total.examMax);

					((TextView)findViewById(R.id.mainTextViewCHNCLASSA)).setText(s.chinese.classAvg);
					((TextView)findViewById(R.id.mainTextViewCHNCLASSM)).setText(s.chinese.classMax);
					((TextView)findViewById(R.id.mainTextViewCHNSCHOOLA)).setText(s.chinese.schoolAvg);
					((TextView)findViewById(R.id.mainTextViewCHNSCHOOLM)).setText(s.chinese.schoolMax);
					((TextView)findViewById(R.id.mainTextViewCHNEXAMA)).setText(s.chinese.examAvg);
					((TextView)findViewById(R.id.mainTextViewCHNEXAMM)).setText(s.chinese.examMax);

					((TextView)findViewById(R.id.mainTextViewMATCLASSA)).setText(s.math.classAvg);
					((TextView)findViewById(R.id.mainTextViewMATCLASSM)).setText(s.math.classMax);
					((TextView)findViewById(R.id.mainTextViewMATSCHOOLA)).setText(s.math.schoolAvg);
					((TextView)findViewById(R.id.mainTextViewMATSCHOOLM)).setText(s.math.schoolMax);
					((TextView)findViewById(R.id.mainTextViewMATEXAMA)).setText(s.math.examAvg);
					((TextView)findViewById(R.id.mainTextViewMATEXAMM)).setText(s.math.examMax);

					((TextView)findViewById(R.id.mainTextViewENGCLASSA)).setText(s.english.classAvg);
					((TextView)findViewById(R.id.mainTextViewENGCLASSM)).setText(s.english.classMax);
					((TextView)findViewById(R.id.mainTextViewENGSCHOOLA)).setText(s.english.schoolAvg);
					((TextView)findViewById(R.id.mainTextViewENGSCHOOLM)).setText(s.english.schoolMax);
					((TextView)findViewById(R.id.mainTextViewENGEXAMA)).setText(s.english.examAvg);
					((TextView)findViewById(R.id.mainTextViewENGEXAMM)).setText(s.english.examMax);

					((TextView)findViewById(R.id.mainTextViewPHYCLASSA)).setText(s.physics.classAvg);
					((TextView)findViewById(R.id.mainTextViewPHYCLASSM)).setText(s.physics.classMax);
					((TextView)findViewById(R.id.mainTextViewPHYSCHOOLA)).setText(s.physics.schoolAvg);
					((TextView)findViewById(R.id.mainTextViewPHYSCHOOLM)).setText(s.physics.schoolMax);
					((TextView)findViewById(R.id.mainTextViewPHYEXAMA)).setText(s.physics.examAvg);
					((TextView)findViewById(R.id.mainTextViewPHYEXAMM)).setText(s.physics.examMax);

					((TextView)findViewById(R.id.mainTextViewCHECLASSA)).setText(s.chemistry.classAvg);
					((TextView)findViewById(R.id.mainTextViewCHECLASSM)).setText(s.chemistry.classMax);
					((TextView)findViewById(R.id.mainTextViewCHESCHOOLA)).setText(s.chemistry.schoolAvg);
					((TextView)findViewById(R.id.mainTextViewCHESCHOOLM)).setText(s.chemistry.schoolMax);
					((TextView)findViewById(R.id.mainTextViewCHEEXAMA)).setText(s.chemistry.examAvg);
					((TextView)findViewById(R.id.mainTextViewCHEEXAMM)).setText(s.chemistry.examMax);

					((TextView)findViewById(R.id.mainTextViewBIOCLASSA)).setText(s.biology.classAvg);
					((TextView)findViewById(R.id.mainTextViewBIOCLASSM)).setText(s.biology.classMax);
					((TextView)findViewById(R.id.mainTextViewBIOSCHOOLA)).setText(s.biology.schoolAvg);
					((TextView)findViewById(R.id.mainTextViewBIOSCHOOLM)).setText(s.biology.schoolMax);
					((TextView)findViewById(R.id.mainTextViewBIOEXAMA)).setText(s.biology.examAvg);
					((TextView)findViewById(R.id.mainTextViewBIOEXAMM)).setText(s.biology.examMax);

					d1.cancel();
				}
			});
	}
}
