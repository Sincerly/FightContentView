package com.sincerly.fightcontentview.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sincerly.fightcontentview.R;
import com.sincerly.fightcontentview.api.RetrofitApi;
import com.sincerly.fightcontentview.bean.ChartBean;
import com.sincerly.fightcontentview.bean.DataSource;
import com.sincerly.fightcontentview.bean.ResponseBean;
import com.sincerly.fightcontentview.bean.ResponseBean2;
import com.sincerly.fightcontentview.config.AppConfig;
import com.sincerly.fightcontentview.ui.DDTrendChart;
import com.sincerly.fightcontentview.ui.LottoTrendView;
import com.sincerly.fightcontentview.ui.TDialog;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/4/15 0015.
 */

public class FragmentB extends Fragment {
	private LottoTrendView mTrendView;
	private DDTrendChart mTrendChart;
	private final OkHttpClient client = new OkHttpClient();
	private boolean isFirst = true;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_a, container, false);
		start();
		initChart(view);
		return view;
	}

	private void initChart(View view) {
		this.mTrendView = (LottoTrendView) view.findViewById(R.id.ltv_trendView);
		this.mTrendChart = new DDTrendChart(getActivity(), this.mTrendView);
		this.mTrendView.setChart(this.mTrendChart);
		this.mTrendChart.setShowYilou(true);
		this.mTrendChart.setDrawLine(true);
		this.mTrendView.setFinish(new LottoTrendView.onFinish() {
			@Override
			public void end() {
				//Log.e("tag", "end");
				stop();
			}
		});
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message paramMessage) {
			super.handleMessage(paramMessage);
			switch (paramMessage.what) {
				case 0x01:
					if (getUserVisibleHint()) {
						//Log.e("tag", "收到10s");
						refresh();
					} else {
					}
//                    refresh();
//                    refresh();
					mHandler.sendEmptyMessageDelayed(0x01, 15000);
					break;
				case 0x02:
					ArrayList<ChartBean> c2 = new ArrayList<>();
					c2.addAll((ArrayList) paramMessage.obj);
//					mTrendView.setNowX(0.0f);
//					mTrendView.setNowY(0.0f);
					mTrendChart.updateData("新疆时时彩", "01", c2);
					if (isFirst) {
						//Log.e("tag", "发起10s");
						isFirst = false;
						mHandler.sendEmptyMessageDelayed(0x01, 15000);
					}
					break;
				case 0x03:
//					mTrendView.setNowX(0.0f);
//					mTrendView.setNowY(0.0f);
					mTrendChart.updateData("新疆时时彩", "01", (ArrayList<ChartBean>) charts);
					break;
				default:
					break;
			}
		}
	};

	private void refresh() {
		getText();
	}

	private void getText() {
		Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create())
				.baseUrl("http://data.917500.cn/");
		RetrofitApi retrofit = retrofitBuilder
				.client(new OkHttpClient())
				.build().create(RetrofitApi.class);


		retrofit2.Call<ResponseBody> call = retrofit.getText1("xjssc_2000.txt");
		call.enqueue(new retrofit2.Callback<ResponseBody>() {
			@Override
			public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
				try {
					ResponseBody is = response.body();
					InputStream inputStream = is.byteStream();

					File file = new File(AppConfig.PATH, "text2.txt");
					if (file.exists()) {
						file.delete();
					}
					file.createNewFile();
					FileOutputStream fos = new FileOutputStream(file);
					BufferedInputStream bis = new BufferedInputStream(inputStream);
					byte[] buffer = new byte[1024];
					int len;
					while ((len = bis.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
						fos.flush();
					}
					fos.close();
					bis.close();
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				parse();
			}

			@Override
			public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
				stop();
			}
		});
	}

	private List<DataSource> sources = new ArrayList<>();

	/**
	 * 读取数据  读取之后 最新数据在最后一行
	 */

	private void parse() {
		File file = new File(AppConfig.PATH, "text2.txt");
		sources.clear();
		BufferedReader reader = null;
		try {
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				String[] a = tempString.split(" ");
				DataSource d = new DataSource();
				d.setNo(a[0]);
				d.setLongNo(a[0]);
				d.setLine(line);
				d.setNum1(Integer.parseInt(a[1]));
				d.setNum2(Integer.parseInt(a[2]));
				d.setNum3(Integer.parseInt(a[3]));
				d.setNum4(Integer.parseInt(a[4]));
				d.setNum5(Integer.parseInt(a[5]));
				sources.add(d);
				line++;
			}
			reader.close();
			replace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 正序排序  读取之后 最新数据在最后一行
	 */
	private void replace() {
		if (isFirst) {
			parseToChartBean();
		} else {
			if (sources.size() > 0) {
				//获取文件中最新一条
				DataSource s = sources.get(sources.size() - 1);
				String sLongNo = s.getLongNo();
				chartBean = charts.get(charts.size() - 1);//获取视图显示的最后一个对象
				String no=chartBean.getLongNo();
				//得到视图中最底部那个数据 在2000条数据中的位置
				int index = 0;
				for (int i = 0; i < sources.size(); i++) {
					if (no.equals(sources.get(i).getLongNo())) {//数据源与当前视图数据源对比
						index = i;
//						1999 最新的
					}
				}
				if (index != sources.size() - 1) {//不是最后一个
					index = index + 1;
					for (int i = index; i < sources.size(); i++) {//最新的条数
						chartBean = charts.get(charts.size() - 1);//获取视图显示的最后一个对象
						DataSource q = sources.get(i);
						int[] n = new int[]{q.getNum1(), q.getNum2(), q.getNum3(), q.getNum4(), q.getNum5()};
						refershItem(q.getLongNo(), n);
					}
					//Log.e("tag", "当前有几条新数据" + (sources.size() - index));
					mHandler.sendEmptyMessage(0x03);
				} else {
					//Log.e("tag", "当前已最新");

//					index=index+1;

//					for (int i = index; i < sources.size(); i++) {//index 最新的条数
//						chartBean = charts.get(charts.size() - 1);
//						DataSource q = sources.get(i);
//						int[] n = new int[]{q.getNum1(), q.getNum2(), q.getNum3(), q.getNum4(), q.getNum5()};
//						refershItem(sLongNo, n);
//					}
//					Log.e("tag", "当前已最新 测试添加" + (sources.size() - index));
//					mHandler.sendEmptyMessage(0x03);
				}
			}
		}
	}

	/**
	 * 从旧往新解析
	 */
	ChartBean chartBean = null;

	/**
	 * 计算2000条数据  未排序
	 */
	private void parseToChartBean() {
		for (int i = 0; i < sources.size(); i++) {
			DataSource item = sources.get(i);
			ChartBean c = new ChartBean();
			int n1 = item.getNum1();
			int n2 = item.getNum2();
			int n3 = item.getNum3();
			int n4 = item.getNum4();
			int n5 = item.getNum5();
			c.setNumber(n1 + "" + n2 + "" + n3 + "" + n4 + "" + n5 + "");
			String no = item.getNo();
			if (no != null) {
				c.setNo(no.substring(no.length() - 3, no.length()));//期号
			} else {
				c.setNo("缺失");
			}
			c.setLongNo(item.getLongNo());
			/**
			 * 处理第一个数据
			 */
			ChartBean.Data1 data1 = new ChartBean.Data1();
			data1.setType(new int[10]);
			if (i == 0) {
				data1.setA0(1);
				data1.setA1(1);
				data1.setA2(1);
				data1.setA3(1);
				data1.setA4(1);
				data1.setA5(1);
				data1.setA6(1);
				data1.setA7(1);
				data1.setA8(1);
				data1.setA9(1);
				c.setData1(data1);
			} else {//处理上一个
				data1.setA0(parseValue(chartBean.getData1().getA0()));
				data1.setA1(parseValue(chartBean.getData1().getA1()));
				data1.setA2(parseValue(chartBean.getData1().getA2()));
				data1.setA3(parseValue(chartBean.getData1().getA3()));
				data1.setA4(parseValue(chartBean.getData1().getA4()));
				data1.setA5(parseValue(chartBean.getData1().getA5()));
				data1.setA6(parseValue(chartBean.getData1().getA6()));
				data1.setA7(parseValue(chartBean.getData1().getA7()));
				data1.setA8(parseValue(chartBean.getData1().getA8()));
				data1.setA9(parseValue(chartBean.getData1().getA9()));
				c.setData1(data1);

				resetData1Flag();
				calcData1(chartBean.getData1(), data1, n1, n2, n3, n4, n5);
			}

			/**
			 * 处理第二个数据
			 */

			chartCurrentBean = data1;

			ChartBean.Data2 data2 = new ChartBean.Data2();
			data2.setType(new int[6]);
			if (i == 0) {
				data2.setB1(1);
				data2.setB2(1);
				data2.setB3(1);
				data2.setB4(1);
				data2.setB5(1);
				data2.setB6(1);
				data2.setB6(1);
				c.setData2(data2);
			} else {//处理上一个
				data2.setB1(parseValue(chartBean.getData2().getB1()));
				data2.setB2(parseValue(chartBean.getData2().getB2()));
				data2.setB3(parseValue(chartBean.getData2().getB3()));
				data2.setB4(parseValue(chartBean.getData2().getB4()));
				data2.setB5(parseValue(chartBean.getData2().getB5()));
				data2.setB6(parseValue(chartBean.getData2().getB6()));
				c.setData2(data2);

				resetData2Flag();
				calcData2(chartBean.getData2(), data2, n1, n2, n3, n4, n5);
			}

			/**
			 * 处理第三个数据
			 */
			ChartBean.Data3 data3 = new ChartBean.Data3();
			if (i == 0) {
				data3.setC1(1);
				data3.setC2(1);
				data3.setC3(1);
				data3.setC4(1);
				data3.setC5(1);
				data3.setC6(1);
				c.setData3(data3);
			} else {//处理上一个
				data3.setC1(parseValue(chartBean.getData3().getC1()));
				data3.setC2(parseValue(chartBean.getData3().getC2()));
				data3.setC3(parseValue(chartBean.getData3().getC3()));
				data3.setC4(parseValue(chartBean.getData3().getC4()));
				data3.setC5(parseValue(chartBean.getData3().getC5()));
				data3.setC6(parseValue(chartBean.getData3().getC6()));
				c.setData3(data3);
				calcData3(chartBean.getData3(), data3, n1, n2, n3, n4, n5);
			}

			/**
			 * 处理第四个数据
			 */
			ChartBean.Data4 data4 = new ChartBean.Data4();
			if (i == 0) {
				data4.setD1(1);
				data4.setD2(1);
				data4.setD3(1);
				data4.setD4(1);
				data4.setD5(1);
				data4.setD6(1);
				c.setData4(data4);
			} else {//处理上一个
				data4.setD1(parseValue(chartBean.getData4().getD1()));
				data4.setD2(parseValue(chartBean.getData4().getD2()));
				data4.setD3(parseValue(chartBean.getData4().getD3()));
				data4.setD4(parseValue(chartBean.getData4().getD4()));
				data4.setD5(parseValue(chartBean.getData4().getD5()));
				data4.setD6(parseValue(chartBean.getData4().getD6()));
				c.setData4(data4);
				calcData4(chartBean.getData4(), data4, n1, n2, n3, n4, n5);
			}
			/**
			 * 处理第五个数据
			 */
			ChartBean.Data5 data5 = new ChartBean.Data5();
			if (i == 0) {
				data5.setE1(1);
				data5.setE2(1);
				data5.setE3(1);
				data5.setE4(1);
				data5.setE5(1);
				data5.setE6(1);
				c.setData5(data5);
			} else {//处理上一个
				data5.setE1(parseValue(chartBean.getData5().getE1()));
				data5.setE2(parseValue(chartBean.getData5().getE2()));
				data5.setE3(parseValue(chartBean.getData5().getE3()));
				data5.setE4(parseValue(chartBean.getData5().getE4()));
				data5.setE5(parseValue(chartBean.getData5().getE5()));
				data5.setE6(parseValue(chartBean.getData5().getE6()));
				c.setData5(data5);
				calcData5(chartBean.getData5(), data5, n1, n2, n3, n4, n5);
			}
			charts.add(c);
			chartBean = c;
		}


		List<ChartBean> list = new ArrayList<>();
		//把数据重新排序  从新到旧
//		for (int i = charts.size() - 1; i >= 0; i--) {
//			ChartBean bean = charts.get(i);
//			list.add(bean);
//		}
		for (int i = 0; i < charts.size(); i++) {
			if (i > 1499) {
				list.add(charts.get(i));
			}
			if (i == 1499) {
				chartBean = charts.get(i);
			}
		}
		charts.clear();
		charts.addAll(list);

		mHandler.sendMessage(mHandler.obtainMessage(0x02, charts));
	}


	/**
	 * 规则  1
	 */
	private List<ChartBean> charts = new ArrayList<>();
	///////////////////////////////////////////////////////////////////////////
	// 数据1
	///////////////////////////////////////////////////////////////////////////

	/**
	 * @param before
	 * @param data1
	 * @param n1
	 * @param n2
	 * @param n3
	 * @param n4
	 * @param n5
	 */
	private void calcData1(ChartBean.Data1 before, ChartBean.Data1 data1, int n1, int n2, int n3, int n4, int n5) {
		setValue(before, data1, n1);
		setValue(before, data1, n2);
		setValue(before, data1, n3);
		setValue(before, data1, n4);
		setValue(before, data1, n5);
	}

	/**
	 * @return
	 */
	private void addType(ChartBean.Data1 data, int index) {
		int[] type = data.getType();
		switch (index) {
			case 0:
				type[0] += 1;
				break;
			case 1:
				type[1] += 1;
				break;
			case 2:
				type[2] += 1;
				break;
			case 3:
				type[3] += 1;
				break;
			case 4:
				type[4] += 1;
				break;
			case 5:
				type[5] += 1;
				break;
			case 6:
				type[6] += 1;
				break;
			case 7:
				type[7] += 1;
				break;
			case 8:
				type[8] += 1;
				break;
			case 9:
				type[9] += 1;
				break;
		}
	}

	/**
	 * 12345  1空心圈 2蓝色实心  3绿色实心 4橙色实心  5正常
	 *
	 * @return
	 */
	private boolean change1 = false;
	private boolean change2 = false;
	private boolean change3 = false;
	private boolean change4 = false;
	private boolean change5 = false;
	private boolean change6 = false;
	private boolean change7 = false;
	private boolean change8 = false;
	private boolean change9 = false;
	private boolean change10 = false;

	private void resetData1Flag() {
		change1 = false;
		change2 = false;
		change3 = false;
		change4 = false;
		change5 = false;
		change6 = false;
		change7 = false;
		change8 = false;
		change9 = false;
		change10 = false;
	}

	private void setValue(ChartBean.Data1 before, ChartBean.Data1 data1, int n1) {
		if (n1 == 0) {
			data1.setA0(-1);
			addType(data1, 0);
			change1 = true;
		} else {
			if (!change1) {
				int b = before.getA0();
				if (b == -1) {
					b = 0;
				}
				data1.setA0(b + 1);
			}
		}
		if (n1 == 1) {
			data1.setA1(-1);
			addType(data1, 1);
			change2 = true;
		} else {
			if (!change2) {
				int b = before.getA1();
				if (b == -1) {
					b = 0;
				}
				data1.setA1(b + 1);
			}
		}
		if (n1 == 2) {
			data1.setA2(-1);
			addType(data1, 2);
			change3 = true;
		} else {
			if (!change3) {
				int b = before.getA2();
				if (b == -1) {
					b = 0;
				}
				data1.setA2(b + 1);
			}
		}
		if (n1 == 3) {
			data1.setA3(-1);
			addType(data1, 3);
			change4 = true;
		} else {
			if (!change4) {
				int b = before.getA3();
				if (b == -1) {
					b = 0;
				}
				data1.setA3(b + 1);
			}
		}
		if (n1 == 4) {
			data1.setA4(-1);
			addType(data1, 4);
			change5 = true;
		} else {
			if (!change5) {
				int b = before.getA4();
				if (b == -1) {
					b = 0;
				}
				data1.setA4(b + 1);
			}
		}
		if (n1 == 5) {
			data1.setA5(-1);
			addType(data1, 5);
			change6 = true;
		} else {
			if (!change6) {
				int b = before.getA5();
				if (b == -1) {
					b = 0;
				}
				data1.setA5(b + 1);
			}
		}
		if (n1 == 6) {
			data1.setA6(-1);
			addType(data1, 6);
			change7 = true;
		} else {
			if (!change7) {
				int b = before.getA6();
				if (b == -1) {
					b = 0;
				}
				data1.setA6(b + 1);
			}
		}
		if (n1 == 7) {
			data1.setA7(-1);
			addType(data1, 7);
			change8 = true;
		} else {
			if (!change8) {
				int b = before.getA7();
				if (b == -1) {
					b = 0;
				}
				data1.setA7(b + 1);
			}
		}
		if (n1 == 8) {
			data1.setA8(-1);
			addType(data1, 8);
			change9 = true;
		} else {
			if (!change9) {
				int b = before.getA8();
				if (b == -1) {
					b = 0;
				}
				data1.setA8(b + 1);
			}
		}
		if (n1 == 9) {
			data1.setA9(-1);
			addType(data1, 9);
			change10 = true;
		} else {
			if (!change10) {
				int b = before.getA9();
				if (b == -1) {
					b = 0;
				}
				data1.setA9(b + 1);
			}
		}
	}

	/**
	 * 获取出现次数
	 *
	 * @param d
	 * @param number
	 * @return
	 */

	private int getSameCount(ChartBean.Data1 d, int number) {
		int count = 0;
		if (0 == number) {
			count++;
		}
		if (1 == number) {
			count++;
		}
		if (2 == number) {
			count++;
		}
		if (3 == number) {
			count++;
		}
		if (4 == number) {
			count++;
		}
		if (5 == number) {
			count++;
		}
		if (6 == number) {
			count++;
		}
		if (7 == number) {
			count++;
		}
		if (8 == number) {
			count++;
		}
		return count;
	}

	///////////////////////////////////////////////////////////////////////////
	// 数据2
	///////////////////////////////////////////////////////////////////////////
	//组数  4个一样1个不一样  组5   2位
	//     3个一样2个一样    组10   2位
	//     3个一样2个不一样  组20   3位
	//     2个一样2个不一样  组30   3位
	//     2个一样3个不一样  组60   4位
	//     5个不一样         组120  5位

	/**
	 * 组
	 *
	 * @param before
	 * @param data1
	 * @param n1
	 * @param n2
	 * @param n3
	 * @param n4
	 * @param n5
	 */
	private ChartBean.Data1 chartCurrentBean;

	private void calcData2(ChartBean.Data2 before, ChartBean.Data2 data1, int n1, int n2, int n3, int n4, int n5) {
		//所有数字选中的数量   0 1 2 3 4 5 6 7 8 9
		int[] type = chartCurrentBean.getType();
		List<Integer> ll = new ArrayList<>();
		for (int i = 0; i < type.length; i++) {
			int a = type[i];//得到10个数字中的其中一个
			if (a != 0 && a != -1) {
				ll.add(type[i]);
			}
		}

		int b = before.getB1();
		if (b == -1) {
			b = 0;
		}
		int b2 = before.getB2();
		if (b2 == -1) {
			b2 = 0;
		}
		int b3 = before.getB3();
		if (b3 == -1) {
			b3 = 0;
		}
		int b4 = before.getB4();
		if (b4 == -1) {
			b4 = 0;
		}
		int b5 = before.getB5();
		if (b5 == -1) {
			b5 = 0;
		}
		int b6 = before.getB6();
		if (b6 == -1) {
			b6 = 0;
		}
		int t = -1;
		//组120
		if (ll.size() == 5) {
			data1.setB1((b + 1));
			data1.setB2((b2 + 1));
			data1.setB3((b3 + 1));
			data1.setB4((b4 + 1));
			data1.setB5((b5 + 1));
			data1.setB6(-1);
		}
		//组60
		if (ll.size() == 4) {
			data1.setB1((b + 1));
			data1.setB2((b2 + 1));
			data1.setB3((b3 + 1));
			data1.setB4((b4 + 1));
			data1.setB5(-1);
			data1.setB6((b6 + 1));
		}

		//组30与组20
		if (ll.size() == 3) {
			int max = 0;
			for (int i = 0; i < ll.size(); i++) {
				max = Math.max(ll.get(i), max);
			}
			if (max == 3) {//组20   3个一样
				data1.setB1((b + 1));
				data1.setB2((b2 + 1));
				data1.setB3(-1);
				data1.setB4((b4 + 1));
				data1.setB5((b5 + 1));
				data1.setB6((b6 + 1));
			} else {//组30  2个一样 2个不一样
				data1.setB1((b + 1));
				data1.setB2((b2 + 1));
				data1.setB3((b3 + 1));
				data1.setB4(-1);
				data1.setB5((b5 + 1));
				data1.setB6((b6 + 1));
			}
		}
		if (ll.size() == 2) {
			int max = 0;
			for (int i = 0; i < ll.size(); i++) {
				max = Math.max(ll.get(i), max);
			}
			if (max == 4) {//组5   4个一样
				data1.setB1(-1);
				data1.setB2((b2 + 1));
				data1.setB3((b3 + 1));
				data1.setB4((b4 + 1));
				data1.setB5((b5 + 1));
				data1.setB6((b6 + 1));
			} else {//组10  2个一样 3个一样
				data1.setB1((b + 1));
				data1.setB2(-1);
				data1.setB3((b3 + 1));
				data1.setB4((b4 + 1));
				data1.setB5((b5 + 1));
				data1.setB6((b6 + 1));
			}
		}
	}

	private boolean data2Change1 = false;
	private boolean data2Change2 = false;
	private boolean data2Change3 = false;
	private boolean data2Change4 = false;
	private boolean data2Change5 = false;
	private boolean data2Change6 = false;

	private void resetData2Flag() {
		data2Change1 = false;
		data2Change2 = false;
		data2Change3 = false;
		data2Change4 = false;
		data2Change5 = false;
		data2Change6 = false;
	}


	private void setValue2(ChartBean.Data2 before, ChartBean.Data2 data1, int n1) {
		if (n1 == 0) {
			data1.setB1(-1);
			addType2(data1, 0);
			data2Change1 = true;
		} else {
			if (!data2Change1) {
				int b = before.getB1();
				if (b == -1) {
					b = 0;
				}
				data1.setB1(b + 1);
			}
		}
		if (n1 == 1) {
			data1.setB2(-1);
			addType2(data1, 1);
			data2Change2 = true;
		} else {
			if (!data2Change2) {
				int b = before.getB2();
				if (b == -1) {
					b = 0;
				}
				data1.setB2(b + 1);
			}
		}
		if (n1 == 2) {
			data1.setB3(-1);
			addType2(data1, 2);
			data2Change3 = true;
		} else {
			if (!data2Change3) {
				int b = before.getB3();
				if (b == -1) {
					b = 0;
				}
				data1.setB3(b + 1);
			}
		}
		if (n1 == 3) {
			data1.setB4(-1);
			addType2(data1, 3);
			data2Change4 = true;
		} else {
			if (!data2Change4) {
				int b = before.getB4();
				if (b == -1) {
					b = 0;
				}
				data1.setB4(b + 1);
			}
		}
		if (n1 == 4) {
			data1.setB5(-1);
			addType2(data1, 4);
			data2Change5 = true;
		} else {
			if (!data2Change5) {
				int b = before.getB5();
				if (b == -1) {
					b = 0;
				}
				data1.setB5(b + 1);
			}
		}
		if (n1 == 5) {
			data1.setB6(-1);
			addType2(data1, 5);
			data2Change6 = true;
		} else {
			if (!data2Change6) {
				int b = before.getB6();
				if (b == -1) {
					b = 0;
				}
				data1.setB6(b + 1);
			}
		}
	}

	/**
	 * @return
	 */
	private void addType2(ChartBean.Data2 data, int index) {
		int[] type = data.getType();
		switch (index) {
			case 0:
				type[0] += 1;
				break;
			case 1:
				type[1] += 1;
				break;
			case 2:
				type[2] += 1;
				break;
			case 3:
				type[3] += 1;
				break;
			case 4:
				type[4] += 1;
				break;
			case 5:
				type[5] += 1;
				break;
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// 数据3
	///////////////////////////////////////////////////////////////////////////
	//0路号码   0369一样的个数
	int[] ii = new int[]{0, 3, 6, 9};

	private void calcData3(ChartBean.Data3 before, ChartBean.Data3 data1, int n1, int n2, int n3, int n4, int n5) {
		int index = 0;
		for (int j = 0; j < ii.length; j++) {
			if (n1 == ii[j]) {
				index++;
			}
			if (n2 == ii[j]) {
				index++;
			}
			if (n3 == ii[j]) {
				index++;
			}
			if (n4 == ii[j]) {
				index++;
			}
			if (n5 == ii[j]) {
				index++;
			}
		}
		switch (index) {
			case 0:
				data1.setC1(-1);
				data1.setC2(parseValue(before.getC2()));
				data1.setC3(parseValue(before.getC3()));
				data1.setC4(parseValue(before.getC4()));
				data1.setC5(parseValue(before.getC5()));
				data1.setC6(parseValue(before.getC6()));
				break;
			case 1:
				data1.setC1(parseValue(before.getC1()));
				data1.setC2(-1);
				data1.setC3(parseValue(before.getC3()));
				data1.setC4(parseValue(before.getC4()));
				data1.setC5(parseValue(before.getC5()));
				data1.setC6(parseValue(before.getC6()));
				break;
			case 2:
				data1.setC1(parseValue(before.getC1()));
				data1.setC2(parseValue(before.getC2()));
				data1.setC3(-1);
				data1.setC4(parseValue(before.getC4()));
				data1.setC5(parseValue(before.getC5()));
				data1.setC6(parseValue(before.getC6()));
				break;
			case 3:
				data1.setC1(parseValue(before.getC1()));
				data1.setC2(parseValue(before.getC2()));
				data1.setC3(parseValue(before.getC3()));
				data1.setC4(-1);
				data1.setC5(parseValue(before.getC5()));
				data1.setC6(parseValue(before.getC6()));
				break;
			case 4:
				data1.setC1(parseValue(before.getC1()));
				data1.setC2(parseValue(before.getC2()));
				data1.setC3(parseValue(before.getC3()));
				data1.setC4(parseValue(before.getC4()));
				data1.setC5(-1);
				data1.setC6(parseValue(before.getC6()));
				break;
			case 5:
				data1.setC1(parseValue(before.getC1()));
				data1.setC2(parseValue(before.getC2()));
				data1.setC3(parseValue(before.getC3()));
				data1.setC4(parseValue(before.getC4()));
				data1.setC5(parseValue(before.getC4()));
				data1.setC6(-1);
				break;
		}
	}

	private int parseValue(int params) {
		int c = 1;
		if (params == -1) {
			return c;
		}
		return ++params;
	}


	///////////////////////////////////////////////////////////////////////////
	// 数据4
	///////////////////////////////////////////////////////////////////////////
	//1路号码   0369一样的个数
	int[] i2 = new int[]{1, 4, 7};

	private void calcData4(ChartBean.Data4 before, ChartBean.Data4 data1, int n1, int n2, int n3, int n4, int n5) {
		int index = 0;
		for (int j = 0; j < i2.length; j++) {
			if (n1 == i2[j]) {
				index++;
			}
			if (n2 == i2[j]) {
				index++;
			}
			if (n3 == i2[j]) {
				index++;
			}
			if (n4 == i2[j]) {
				index++;
			}
			if (n5 == i2[j]) {
				index++;
			}
		}
		switch (index) {
			case 0:
				data1.setD1(-1);
				data1.setD2(parseValue2(before.getD2()));
				data1.setD3(parseValue2(before.getD3()));
				data1.setD4(parseValue2(before.getD4()));
				data1.setD5(parseValue2(before.getD5()));
				data1.setD6(parseValue2(before.getD6()));
				break;
			case 1:
				data1.setD1(parseValue2(before.getD1()));
				data1.setD2(-1);
				data1.setD3(parseValue2(before.getD3()));
				data1.setD4(parseValue2(before.getD4()));
				data1.setD5(parseValue2(before.getD5()));
				data1.setD6(parseValue2(before.getD6()));
				break;
			case 2:
				data1.setD1(parseValue2(before.getD1()));
				data1.setD2(parseValue2(before.getD2()));
				data1.setD3(-1);
				data1.setD4(parseValue2(before.getD4()));
				data1.setD5(parseValue2(before.getD5()));
				data1.setD6(parseValue2(before.getD6()));
				break;
			case 3:
				data1.setD1(parseValue2(before.getD1()));
				data1.setD2(parseValue2(before.getD2()));
				data1.setD3(parseValue2(before.getD3()));
				data1.setD4(-1);
				data1.setD5(parseValue2(before.getD5()));
				data1.setD6(parseValue2(before.getD6()));
				break;
			case 4:
				data1.setD1(parseValue2(before.getD1()));
				data1.setD2(parseValue2(before.getD2()));
				data1.setD3(parseValue2(before.getD3()));
				data1.setD4(parseValue2(before.getD4()));
				data1.setD5(-1);
				data1.setD6(parseValue2(before.getD6()));
				break;
			case 5:
				data1.setD1(parseValue2(before.getD1()));
				data1.setD2(parseValue2(before.getD2()));
				data1.setD3(parseValue2(before.getD3()));
				data1.setD4(parseValue2(before.getD4()));
				data1.setD5(parseValue2(before.getD4()));
				data1.setD6(-1);
				break;
		}
	}

	private int parseValue2(int params) {
		int c = 1;
		if (params == -1) {
			return c;
		}
		return ++params;
	}

	///////////////////////////////////////////////////////////////////////////
	// 数据5
	///////////////////////////////////////////////////////////////////////////
	//1路号码   258一样的个数
	int[] i3 = new int[]{2, 5, 8};

	private void calcData5(ChartBean.Data5 before, ChartBean.Data5 data1, int n1, int n2, int n3, int n4, int n5) {
		int index = 0;
		for (int j = 0; j < i3.length; j++) {
			if (n1 == i3[j]) {
				index++;
			}
			if (n2 == i3[j]) {
				index++;
			}
			if (n3 == i3[j]) {
				index++;
			}
			if (n4 == i3[j]) {
				index++;
			}
			if (n5 == i3[j]) {
				index++;
			}
		}
		switch (index) {
			case 0:
				data1.setE1(-1);
				data1.setE2(parseValue2(before.getE2()));
				data1.setE3(parseValue2(before.getE3()));
				data1.setE4(parseValue2(before.getE4()));
				data1.setE5(parseValue2(before.getE5()));
				data1.setE6(parseValue2(before.getE6()));
				break;
			case 1:
				data1.setE1(parseValue2(before.getE1()));
				data1.setE2(-1);
				data1.setE3(parseValue2(before.getE3()));
				data1.setE4(parseValue2(before.getE4()));
				data1.setE5(parseValue2(before.getE5()));
				data1.setE6(parseValue2(before.getE6()));
				break;
			case 2:
				data1.setE1(parseValue2(before.getE1()));
				data1.setE2(parseValue2(before.getE2()));
				data1.setE3(-1);
				data1.setE4(parseValue2(before.getE4()));
				data1.setE5(parseValue2(before.getE5()));
				data1.setE6(parseValue2(before.getE6()));
				break;
			case 3:
				data1.setE1(parseValue2(before.getE1()));
				data1.setE2(parseValue2(before.getE2()));
				data1.setE3(parseValue2(before.getE3()));
				data1.setE4(-1);
				data1.setE5(parseValue2(before.getE5()));
				data1.setE6(parseValue2(before.getE6()));
				break;
			case 4:
				data1.setE1(parseValue2(before.getE1()));
				data1.setE2(parseValue2(before.getE2()));
				data1.setE3(parseValue2(before.getE3()));
				data1.setE4(parseValue2(before.getE4()));
				data1.setE5(-1);
				data1.setE6(parseValue2(before.getE6()));
				break;
			case 5:
				data1.setE1(parseValue2(before.getE1()));
				data1.setE2(parseValue2(before.getE2()));
				data1.setE3(parseValue2(before.getE3()));
				data1.setE4(parseValue2(before.getE4()));
				data1.setE5(parseValue2(before.getE4()));
				data1.setE6(-1);
				break;
		}
	}


	///////////////////////////////////////////////////////////////////////////
	// 刷新
	///////////////////////////////////////////////////////////////////////////

	/**
	 * time : 1523886999887
	 * current : {"periodNumber":95,"periodDate":"20180416095","awardTime":"2018-04-16 21:50:00","awardNumbers":"4,9,7,0,3"}
	 * next : {"periodNumber":96,"periodDate":"20180416096","awardTime":"2018-04-16 22:00:00","awardTimeInterval":200000,"delayTimeInterval":15}
	 * awardState : false
	 */

	private void refershItem(String n, int[] number) {
		if (charts == null) {
			return;
		}
		if (charts.size() < 0) {
			return;
		}

		ChartBean c = new ChartBean();
		int n1 = number[0];
		int n2 = number[1];
		int n3 = number[2];
		int n4 = number[3];
		int n5 = number[4];
		c.setNumber(n1 + "" + n2 + "" + n3 + "" + n4 + "" + n5 + "");
		String no = n;
//		if (charts.get(charts.size()-1).getLongNo().equals(no)) {//与当前不符合
//			Log.e("tag","号码已最新");
//			return;
//		}
		if (no != null) {
			c.setNo(no.substring(no.length() - 3, no.length()));//期号
		} else {
			c.setNo("期号缺失");
		}
		c.setLongNo(n);
		/**
		 * 处理第一个数据
		 */
		ChartBean.Data1 data1 = new ChartBean.Data1();
		data1.setType(new int[10]);

		data1.setA0(parseValue(chartBean.getData1().getA0()));
		data1.setA1(parseValue(chartBean.getData1().getA1()));
		data1.setA2(parseValue(chartBean.getData1().getA2()));
		data1.setA3(parseValue(chartBean.getData1().getA3()));
		data1.setA4(parseValue(chartBean.getData1().getA4()));
		data1.setA5(parseValue(chartBean.getData1().getA5()));
		data1.setA6(parseValue(chartBean.getData1().getA6()));
		data1.setA7(parseValue(chartBean.getData1().getA7()));
		data1.setA8(parseValue(chartBean.getData1().getA8()));
		data1.setA9(parseValue(chartBean.getData1().getA9()));
		c.setData1(data1);
		resetData1Flag();
		calcData1(chartBean.getData1(), data1, n1, n2, n3, n4, n5);
		/**
		 * 处理第二个数据
		 */

		chartCurrentBean = data1;
		ChartBean.Data2 data2 = new ChartBean.Data2();
		data2.setType(new int[6]);
		//处理上一个
		data2.setB1(parseValue(chartBean.getData2().getB1()));
		data2.setB2(parseValue(chartBean.getData2().getB2()));
		data2.setB3(parseValue(chartBean.getData2().getB3()));
		data2.setB4(parseValue(chartBean.getData2().getB4()));
		data2.setB5(parseValue(chartBean.getData2().getB5()));
		data2.setB6(parseValue(chartBean.getData2().getB6()));
		c.setData2(data2);
		resetData2Flag();
		calcData2(chartBean.getData2(), data2, n1, n2, n3, n4, n5);

		/**
		 * 处理第三个数据
		 */
		ChartBean.Data3 data3 = new ChartBean.Data3();
		//处理上一个
		data3.setC1(parseValue(chartBean.getData3().getC1()));
		data3.setC2(parseValue(chartBean.getData3().getC2()));
		data3.setC3(parseValue(chartBean.getData3().getC3()));
		data3.setC4(parseValue(chartBean.getData3().getC4()));
		data3.setC5(parseValue(chartBean.getData3().getC5()));
		data3.setC6(parseValue(chartBean.getData3().getC6()));
		c.setData3(data3);
		calcData3(chartBean.getData3(), data3, n1, n2, n3, n4, n5);

		/**
		 * 处理第四个数据
		 */
		ChartBean.Data4 data4 = new ChartBean.Data4();
		//处理上一个
		data4.setD1(parseValue(chartBean.getData4().getD1()));
		data4.setD2(parseValue(chartBean.getData4().getD2()));
		data4.setD3(parseValue(chartBean.getData4().getD3()));
		data4.setD4(parseValue(chartBean.getData4().getD4()));
		data4.setD5(parseValue(chartBean.getData4().getD5()));
		data4.setD6(parseValue(chartBean.getData4().getD6()));
		c.setData4(data4);
		calcData4(chartBean.getData4(), data4, n1, n2, n3, n4, n5);
		/**
		 * 处理第五个数据
		 */
		ChartBean.Data5 data5 = new ChartBean.Data5();
		data5.setE1(1);
		data5.setE2(1);
		data5.setE3(1);
		data5.setE4(1);
		data5.setE5(1);
		data5.setE6(1);
		c.setData5(data5);
		//处理上一个
		data5.setE1(parseValue(chartBean.getData5().getE1()));
		data5.setE2(parseValue(chartBean.getData5().getE2()));
		data5.setE3(parseValue(chartBean.getData5().getE3()));
		data5.setE4(parseValue(chartBean.getData5().getE4()));
		data5.setE5(parseValue(chartBean.getData5().getE5()));
		data5.setE6(parseValue(chartBean.getData5().getE6()));
		c.setData5(data5);
		calcData5(chartBean.getData5(), data5, n1, n2, n3, n4, n5);
		charts.remove(0);//删除最旧一个
		chartBean = charts.get(charts.size() - 1);
		charts.add(c);
	}


	///////////////////////////////////////////////////////////////////////////
	// 动画
	///////////////////////////////////////////////////////////////////////////
	FrameLayout parent;
	ImageView image;
	Animation rotation;

	private void start() {
//		showNewDialog();
	}

	private void stop() {
//		if (parent != null && parent.getVisibility() == View.VISIBLE) {
//			parent.setVisibility(View.GONE);
//		}
//		d2.dismiss();
	}

	private TDialog d2;

	private void showNewDialog() {
		d2 = new TDialog(getActivity());
		d2.setCanceledOnTouchOutside(false);
//		changeWindow(d2);
		d2.show();
	}

//	@Override
//	public void setUserVisibleHint(boolean isVisibleToUser) {
//		super.setUserVisibleHint(isVisibleToUser);
//		Log.e("tag",isVisibleToUser+"A:");
//		if(isVisibleToUser&&isFirst){
//			getText();
//		}
//	}

	@Override
	public void onResume() {
		super.onResume();
		isFirst = true;
		mHandler.removeMessages(0x01);
		getText();
	}
}