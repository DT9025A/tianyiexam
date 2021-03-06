package org.FieldCongratulations.tydlk;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.io.*;
import java.util.regex.*;


public class MainActivity extends Activity 
{
	protected indexInfo info = null;
	protected Context context = this;
	protected Dialog d = null;
	protected boolean isInit = false;
	protected ConnectionChecker connectionChecker = null;

	private void showDialog(String text) {
		d = new Dialog(context);
		d.setContentView(R.layout.dialog);
		d.setTitle(text);
		d.show();
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
					showDialog("拉取考试信息");
					new Thread(new Runnable(){
							@Override
							public void run() {
								try {
									info = new indexInfo();
									final String[] result = info.indexExamResult();
									runOnUiThread(new Runnable(){
											@Override
											public void run() {
												((Button)findViewById(R.id.mainButtonSubmit)).setText(R.string.requestStudent);
												((Spinner)findViewById(R.id.mainSpinnerExam)).setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, result));
												Toast.makeText(context, "拉取完成,长按可重新拉取考试信息", Toast.LENGTH_SHORT).show();
											}
										});
									isInit = true;
								} catch (final IOException e) {
									runOnUiThread(new Runnable(){
											@Override
											public void run() {
												Toast.makeText(context, "拉取考试信息失败: 天一网站又炸了", Toast.LENGTH_SHORT).show();
											}
										});
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
					connectionChecker.refresh();
					if (!connectionChecker.isConnected) {
						Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
						return;
					}
					if (!isInit) {
						showDialog("拉取考试信息");
						new Thread(new Runnable(){
								@Override
								public void run() {
									try {
										info = new indexInfo();
										final String[] result = info.indexExamResult();
										runOnUiThread(new Runnable(){
												@Override
												public void run() {
													((Button)findViewById(R.id.mainButtonSubmit)).setText(R.string.requestStudent);
													((Spinner)findViewById(R.id.mainSpinnerExam)).setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, result));
													Toast.makeText(context, "拉取完成,长按可重新拉取考试信息", Toast.LENGTH_SHORT).show();
													d.cancel();
												}
											});
										isInit = true;
									} catch (final IOException e) {
										runOnUiThread(new Runnable(){
												@Override
												public void run() {
													Toast.makeText(context, "拉取考试信息失败: 天一网站又炸了", Toast.LENGTH_SHORT).show();
												}
											});
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
						d.cancel();
						return;
					}
					showDialog("请求考生数据");
					new Thread(new Runnable(){
							@Override
							public void run() {
								try {
									String userid = ((EditText)findViewById(R.id.mainEditTextNumber)).getText().toString();
									String examid = info.serial[((Spinner)findViewById(R.id.mainSpinnerExam)).getSelectedItemPosition()];
									if (userid.indexOf("clkid") != ~1) {
										//自定义clkid
										//格式:clkid:%d{m};%d{n}
										if (userid.indexOf(';') == -1 || userid.indexOf(';') != userid.lastIndexOf(';')) {
											Toast.makeText(context, "自定义格式错误", Toast.LENGTH_SHORT).show();
											return;
										}
										String[] data = userid.split(";");
										if (data[0].indexOf("clkid") != -1) {
											examid=data[0].substring(6);
											userid=data[1];
										}else{
											examid=data[1].substring(6);
											userid=data[0];
										}
									}
									if (!Pattern.matches("\\d+", userid)) {
										Toast.makeText(context, "考生号格式错误", Toast.LENGTH_SHORT).show();
										return;
									}

									info.requestResult(userid, examid);
									final subjects s = info.indexSubjects();

									runOnUiThread(new Runnable(){
											@Override
											public void run() {
												((TextView)findViewById(R.id.mainTextViewUserInfo)).setText(info.indexStudentInfo());
												if (info.isScience) {
													((TextView)findViewById(R.id.mainTextViewPHY)).setText("物理");
													((TextView)findViewById(R.id.mainTextViewCHE)).setText("化学");
													((TextView)findViewById(R.id.mainTextViewBIO)).setText("生物");
													((TextView)findViewById(R.id.mainTextViewPHY0)).setText("物理");
													((TextView)findViewById(R.id.mainTextViewCHE0)).setText("化学");
													((TextView)findViewById(R.id.mainTextViewBIO0)).setText("生物");
												} else {
													((TextView)findViewById(R.id.mainTextViewPHY)).setText("政治");
													((TextView)findViewById(R.id.mainTextViewCHE)).setText("历史");
													((TextView)findViewById(R.id.mainTextViewBIO)).setText("地理");
													((TextView)findViewById(R.id.mainTextViewPHY0)).setText("政治");
													((TextView)findViewById(R.id.mainTextViewCHE0)).setText("历史");
													((TextView)findViewById(R.id.mainTextViewBIO0)).setText("地理");
												}

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
												d.cancel();
											}
										});
								} catch (final IOException e) {
									runOnUiThread(new Runnable(){
											@Override
											public void run() {
												Toast.makeText(context, "查询失败: 天一网站又炸了", Toast.LENGTH_SHORT).show();
											}
										});
								} catch (final Exception e) {
									runOnUiThread(new Runnable(){
											@Override
											public void run() {
												Toast.makeText(context, "查询失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
												d.cancel();
												return;
											}
										});
								}
							}
						}).start();
				}
			});
	}
}
