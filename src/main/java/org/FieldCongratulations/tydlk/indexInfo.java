package org.FieldCongratulations.tydlk;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

class request implements Callable<String>
{
	private URL url;

	public request(String _url) throws MalformedURLException {
		url = new URL(_url);
	}

	@Override
	public String call() throws Exception {
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		StringBuilder result = new StringBuilder();
		conn.setRequestMethod("GET");
		conn.connect();
		InputStream is = conn.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
        BufferedReader bufferReader = new BufferedReader(isr);
        String inputLine;
        while ((inputLine = bufferReader.readLine()) != null)
            result.append(inputLine + "\n");
		conn.disconnect();
		return result.toString();
	}
}

class StudentNotExistException extends Exception
{
	public StudentNotExistException(String msg) {
		super(msg);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}

	@Override
	public void printStackTrace() {
		super.printStackTrace();
	}
}

public class indexInfo
{
	private String resultPage = "";
	public boolean isScience = true;
	public String[] serial;
	public String[] names;

	public indexInfo() throws MalformedURLException,InterruptedException,ExecutionException {
		FutureTask<String> ft = new FutureTask<String>(new request(
														   "http://score.tydlk.cn/search/msingle"));
		new Thread(ft).start();
		resultPage = ft.get();
	}
	
	public void requestResult(String studentNumber, String exam) throws MalformedURLException,InterruptedException,ExecutionException,StudentNotExistException {
		FutureTask<String> ft = new FutureTask<String>(new request(String.format("http://score.tydlk.cn/search/msingle?clkid=%s&xuehao=%s", exam, studentNumber)));
		new Thread(ft).start();
		resultPage = ft.get();
		if(resultPage.contains("政治"))
			isScience = false;
		else
			isScience = true;
		if (resultPage.contains("学号不存在"))
			throw new StudentNotExistException("StudentNotExistException: Invalid student id. May be not participated in the exam ?");
	}

	public String[] indexExamResult() {
		Matcher matcher = Pattern.compile("clkid[\\w\\W]+?(\\s*<option value=\"\\d+\" >[\\w\\W]+?</option>*)+").matcher(resultPage);
		if (matcher.find()) {
			String _source = matcher.group();
			matcher = Pattern.compile("\"\\d+").matcher(_source);
			ArrayList<String> ar = new ArrayList<String>();
			while (matcher.find())
				ar.add(matcher.group().replaceAll("\"", ""));
			serial = ar.toArray(new String[ar.size()]);
			ar.clear();
			matcher = Pattern.compile(" >[\\w\\W]+?</").matcher(_source);
			while (matcher.find())
				ar.add(matcher.group().replaceAll("[\\s></]*", ""));
			names = ar.toArray(new String[ar.size()]);
			return names;
		}
		serial = new String[]{"0"};
		return new String[]{"无考试"};
	}

	public String indexStudentInfo() {
		Matcher matcher = Pattern.compile("info\">[\\w\\W]+?</div").matcher(resultPage);
		matcher.find();
		matcher = Pattern.compile("<span>[\\w\\W]+?</s").matcher(matcher.group());
		StringBuilder sb = new StringBuilder();
		while (matcher.find())
			sb.append(matcher.group().replaceAll("(<span>|</s)", "") + "  ");
		return sb.toString();
	}

	public subjects indexSubjects() {
		subjects _sub = new subjects();
		Matcher matcher = Pattern.compile("<td>(<span class=\"f_org\">)?[\\d.]+").matcher(resultPage);
		Matcher matcherNumber = Pattern.compile("[\\d.]+").matcher("");
		for (int i = 0;i <= 3 && matcher.find();i++) {
			matcherNumber.reset(matcher.group());
			matcherNumber.find();
			switch (i) {
				case 0:
					_sub.total.score = matcherNumber.group();
					break;
				case 1:
					_sub.total.classSerial = matcherNumber.group();
					break;
				case 2:
					_sub.total.schoolSerial = matcherNumber.group();
					break;
				case 3:
					_sub.total.examSerial = matcherNumber.group();
			}
		}
		for (int i = 0;i <= 3 && matcher.find();i++) {
			matcherNumber.reset(matcher.group());
			matcherNumber.find();
			switch (i) {
				case 0:
					_sub.chinese.score = matcherNumber.group();
					break;
				case 1:
					_sub.chinese.classSerial = matcherNumber.group();
					break;
				case 2:
					_sub.chinese.schoolSerial = matcherNumber.group();
					break;
				case 3:
					_sub.chinese.examSerial = matcherNumber.group();
			}
		}
		for (int i = 0;i <= 3 && matcher.find();i++) {
			matcherNumber.reset(matcher.group());
			matcherNumber.find();
			switch (i) {
				case 0:
					_sub.math.score = matcherNumber.group();
					break;
				case 1:
					_sub.math.classSerial = matcherNumber.group();
					break;
				case 2:
					_sub.math.schoolSerial = matcherNumber.group();
					break;
				case 3:
					_sub.math.examSerial = matcherNumber.group();
			}
		}
		for (int i = 0;i <= 3 && matcher.find();i++) {
			matcherNumber.reset(matcher.group());
			matcherNumber.find();
			switch (i) {
				case 0:
					_sub.english.score = matcherNumber.group();
					break;
				case 1:
					_sub.english.classSerial = matcherNumber.group();
					break;
				case 2:
					_sub.english.schoolSerial = matcherNumber.group();
					break;
				case 3:
					_sub.english.examSerial = matcherNumber.group();
			}
		}
		for (int i = 0;i <= 3 && matcher.find();i++) {
			matcherNumber.reset(matcher.group());
			matcherNumber.find();
			switch (i) {
				case 0:
					_sub.physics.score = matcherNumber.group();
					break;
				case 1:
					_sub.physics.classSerial = matcherNumber.group();
					break;
				case 2:
					_sub.physics.schoolSerial = matcherNumber.group();
					break;
				case 3:
					_sub.physics.examSerial = matcherNumber.group();
			}
		}
		for (int i = 0;i <= 3 && matcher.find();i++) {
			matcherNumber.reset(matcher.group());
			matcherNumber.find();
			switch (i) {
				case 0:
					_sub.chemistry.score = matcherNumber.group();
					break;
				case 1:
					_sub.chemistry.classSerial = matcherNumber.group();
					break;
				case 2:
					_sub.chemistry.schoolSerial = matcherNumber.group();
					break;
				case 3:
					_sub.chemistry.examSerial = matcherNumber.group();
			}
		}
		for (int i = 0;i <= 3 && matcher.find();i++) {
			matcherNumber.reset(matcher.group());
			matcherNumber.find();
			switch (i) {
				case 0:
					_sub.biology.score = matcherNumber.group();
					break;
				case 1:
					_sub.biology.classSerial = matcherNumber.group();
					break;
				case 2:
					_sub.biology.schoolSerial = matcherNumber.group();
					break;
				case 3:
					_sub.biology.examSerial = matcherNumber.group();
			}
		}
		for (int i = 0;i < 3 && matcher.find();i++) {
			matcherNumber.reset(matcher.group());
			matcherNumber.find();
			switch (i) {
				case 0:
					continue;
				case 1:
					_sub.total.classAvg = matcherNumber.group();
					break;
				case 2:
					_sub.total.classMax = matcherNumber.group();
					break;
			}
		}
		for (int i = 0;i < 3 && matcher.find();i++) {
			matcherNumber.reset(matcher.group());
			matcherNumber.find();
			switch (i) {
				case 0:
					continue;
				case 1:
					_sub.chinese.classAvg = matcherNumber.group();
					break;
				case 2:
					_sub.chinese.classMax = matcherNumber.group();
					break;
			}
		}
		for (int i = 0;i < 3 && matcher.find();i++) {
			matcherNumber.reset(matcher.group());
			matcherNumber.find();
			switch (i) {
				case 0:
					continue;
				case 1:
					_sub.math.classAvg = matcherNumber.group();
					break;
				case 2:
					_sub.math.classMax = matcherNumber.group();
					break;
			}
		}
		for (int i = 0;i < 3 && matcher.find();i++) {
			matcherNumber.reset(matcher.group());
			matcherNumber.find();
			switch (i) {
				case 0:
					continue;
				case 1:
					_sub.english.classAvg = matcherNumber.group();
					break;
				case 2:
					_sub.english.classMax = matcherNumber.group();
					break;
			}
		}
		for (int i = 0;i < 3 && matcher.find();i++) {
			matcherNumber.reset(matcher.group());
			matcherNumber.find();
			switch (i) {
				case 0:
					continue;
				case 1:
					_sub.physics.classAvg = matcherNumber.group();
					break;
				case 2:
					_sub.physics.classMax = matcherNumber.group();
					break;
			}
		}
		for (int i = 0;i < 3 && matcher.find();i++) {
			matcherNumber.reset(matcher.group());
			matcherNumber.find();
			switch (i) {
				case 0:
					continue;
				case 1:
					_sub.chemistry.classAvg = matcherNumber.group();
					break;
				case 2:
					_sub.chemistry.classMax = matcherNumber.group();
					break;
			}
		}
		for (int i = 0;i < 3 && matcher.find();i++) {
			matcherNumber.reset(matcher.group());
			matcherNumber.find();
			switch (i) {
				case 0:
					continue;
				case 1:
					_sub.biology.classAvg = matcherNumber.group();
					break;
				case 2:
					_sub.biology.classMax = matcherNumber.group();
					break;
			}
		}
		for (int i = 0;i < 3 && matcher.find();i++) {
			matcherNumber.reset(matcher.group());
			matcherNumber.find();
			switch (i) {
				case 0:
					continue;
				case 1:
					_sub.total.schoolAvg = matcherNumber.group();
					break;
				case 2:
					_sub.total.schoolMax = matcherNumber.group();
					break;
			}
		}
		for (int i = 0;i < 3 && matcher.find();i++) {
			matcherNumber.reset(matcher.group());
			matcherNumber.find();
			switch (i) {
				case 0:
					continue;
				case 1:
					_sub.chinese.schoolAvg = matcherNumber.group();
					break;
				case 2:
					_sub.chinese.schoolMax = matcherNumber.group();
					break;
			}
		}
		for (int i = 0;i < 3 && matcher.find();i++) {
			matcherNumber.reset(matcher.group());
			matcherNumber.find();
			switch (i) {
				case 0:
					continue;
				case 1:
					_sub.math.schoolAvg = matcherNumber.group();
					break;
				case 2:
					_sub.math.schoolMax = matcherNumber.group();
					break;
			}
		}
		for (int i = 0;i < 3 && matcher.find();i++) {
			matcherNumber.reset(matcher.group());
			matcherNumber.find();
			switch (i) {
				case 0:
					continue;
				case 1:
					_sub.english.schoolAvg = matcherNumber.group();
					break;
				case 2:
					_sub.english.schoolMax = matcherNumber.group();
					break;
			}
		}
		for (int i = 0;i < 3 && matcher.find();i++) {
			matcherNumber.reset(matcher.group());
			matcherNumber.find();
			switch (i) {
				case 0:
					continue;
				case 1:
					_sub.physics.schoolAvg = matcherNumber.group();
					break;
				case 2:
					_sub.physics.schoolMax = matcherNumber.group();
					break;
			}
		}
		for (int i = 0;i < 3 && matcher.find();i++) {
			matcherNumber.reset(matcher.group());
			matcherNumber.find();
			switch (i) {
				case 0:
					continue;
				case 1:
					_sub.chemistry.schoolAvg = matcherNumber.group();
					break;
				case 2:
					_sub.chemistry.schoolMax = matcherNumber.group();
					break;
			}
		}
		for (int i = 0;i < 3 && matcher.find();i++) {
			matcherNumber.reset(matcher.group());
			matcherNumber.find();
			switch (i) {
				case 0:
					continue;
				case 1:
					_sub.biology.schoolAvg = matcherNumber.group();
					break;
				case 2:
					_sub.biology.schoolMax = matcherNumber.group();
					break;
			}
		}
		for (int i = 0;i < 3 && matcher.find();i++) {
			matcherNumber.reset(matcher.group());
			matcherNumber.find();
			switch (i) {
				case 0:
					continue;
				case 1:
					_sub.total.examAvg = matcherNumber.group();
					break;
				case 2:
					_sub.total.examMax = matcherNumber.group();
					break;
			}
		}
		for (int i = 0;i < 3 && matcher.find();i++) {
			matcherNumber.reset(matcher.group());
			matcherNumber.find();
			switch (i) {
				case 0:
					continue;
				case 1:
					_sub.chinese.examAvg = matcherNumber.group();
					break;
				case 2:
					_sub.chinese.examMax = matcherNumber.group();
					break;
			}
		}
		for (int i = 0;i < 3 && matcher.find();i++) {
			matcherNumber.reset(matcher.group());
			matcherNumber.find();
			switch (i) {
				case 0:
					continue;
				case 1:
					_sub.math.examAvg = matcherNumber.group();
					break;
				case 2:
					_sub.math.examMax = matcherNumber.group();
					break;
			}
		}
		for (int i = 0;i < 3 && matcher.find();i++) {
			matcherNumber.reset(matcher.group());
			matcherNumber.find();
			switch (i) {
				case 0:
					continue;
				case 1:
					_sub.english.examAvg = matcherNumber.group();
					break;
				case 2:
					_sub.english.examMax = matcherNumber.group();
					break;
			}
		}
		for (int i = 0;i < 3 && matcher.find();i++) {
			matcherNumber.reset(matcher.group());
			matcherNumber.find();
			switch (i) {
				case 0:
					continue;
				case 1:
					_sub.physics.examAvg = matcherNumber.group();
					break;
				case 2:
					_sub.physics.examMax = matcherNumber.group();
					break;
			}
		}
		for (int i = 0;i < 3 && matcher.find();i++) {
			matcherNumber.reset(matcher.group());
			matcherNumber.find();
			switch (i) {
				case 0:
					continue;
				case 1:
					_sub.chemistry.examAvg = matcherNumber.group();
					break;
				case 2:
					_sub.chemistry.examMax = matcherNumber.group();
					break;
			}
		}
		for (int i = 0;i < 3 && matcher.find();i++) {
			matcherNumber.reset(matcher.group());
			matcherNumber.find();
			switch (i) {
				case 0:
					continue;
				case 1:
					_sub.biology.examAvg = matcherNumber.group();
					break;
				case 2:
					_sub.biology.examMax = matcherNumber.group();
					break;
			}
		}
		return _sub;
	}
}

class indexResult
{
	public String score = "";
	public String classSerial = "";
	public String schoolSerial = "";
	public String examSerial = "";
	public String classAvg = "";
	public String classMax = "";
	public String schoolAvg = "";
	public String schoolMax = "";
	public String examAvg = "";
	public String examMax = "";
}

class subjects
{
	public indexResult total;
	public indexResult chinese;
	public indexResult math;
	public indexResult english;
	public indexResult physics;
	public indexResult chemistry;
	public indexResult biology;

	public subjects() {
		total = new indexResult();
		chinese = new indexResult();
		math = new indexResult();
		english = new indexResult();
		physics = new indexResult(); //物理/政治
		biology = new indexResult(); //生物/地理
		chemistry = new indexResult(); //化学/历史
	}
}
