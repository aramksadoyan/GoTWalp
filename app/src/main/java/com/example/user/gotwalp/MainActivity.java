package com.example.user.gotwalp;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	//1 Make a note of your new app ID. You'll need to add it to your app's source code to run AdMob.
	//ca-app-pub-6630049462645854~6334922802
	//Create an ad unit to display ads in your app.
	//If your app is published to Google Play or the App Store, remember to come back to link your app.


//	Banner	ca-app-pub-3940256099942544/6300978111
//	Interstitial	ca-app-pub-3940256099942544/1033173712
//	Rewarded Video	ca-app-pub-3940256099942544/5224354917
//	Native Advanced	ca-app-pub-3940256099942544/2247696110
//	Native Express	(Small template): ca-app-pub-3940256099942544/2793859312
//			(Large template): ca-app-pub-3940256099942544/2177258514


	//for test app id ca-app-pub-3940256099942544~3347511713;

	private AdView adView = null;

	private String testBannerAd = "ca-app-pub-3940256099942544/6300978111";

	private LinearLayout imageContainer = null;
	private TextView textView = null;
	private List<Bitmap> bitmaps = new ArrayList<>();
	private ArrayList<String[]> moreAppParams = new ArrayList<>();

	private int currentPos = 0;
	private int bitMapSize = 0;
	private int espectedCount = -2;
	private Button pre, ok, next, more = null;
	private ProgressBar progressBar = null;
	private WallpaperManager wallpaperManager = null;
	private InterstitialAd interstitialAd;
	private DatabaseReference databaseReference;
	private boolean wantsToOpen = false;

	public ArrayList<InfoParams> infoParamses = new ArrayList<>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		imageContainer = findViewById(R.id.image_container);
		adView = findViewById(R.id.adView);
		pre = findViewById(R.id.left);
		ok = findViewById(R.id.ok);
		next = findViewById(R.id.right);
		more = findViewById(R.id.more);
		progressBar = findViewById(R.id.progress_bar);
		enableClick();
		textView = findViewById(R.id.textView1);
		wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
		databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://wallpapers-63650.firebaseio.com/");
		databaseReference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				Log.d("dwd", "ondataSetChanged");
				Map<String, Map> map = (Map<String, Map>) dataSnapshot.getValue();
				moreAppParams.clear();
				espectedCount = map.size();
				for (int i = 0; i < espectedCount; i++) {
					final Map map1 = map.get("params" + String.valueOf(i));
					String[] moreAppParamsInner = {"", "", ""};
					moreAppParamsInner[0] = String.valueOf(map.get("params" + String.valueOf(i)).get("action"));
					moreAppParamsInner[1] = String.valueOf(map1.get("title"));
					moreAppParamsInner[2] = String.valueOf(map1.get("url"));
					moreAppParams.add(moreAppParamsInner);

					final Bitmap bitmap = null;
					final int finalI = i;
					final int finalI1 = i;
					Glide.with(MainActivity.this).load(String.valueOf(map1.get("url")))
							.asBitmap()
							.into(new SimpleTarget<Bitmap>(50,50) {
								@Override
								public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
									InfoParams infoParams = new InfoParams();
									infoParams.setAction(String.valueOf(map1.get("action")));
									infoParams.setImageBitmap(resource);
									infoParams.setTitle(String.valueOf(map1.get("title")));
									infoParamses.add(infoParams);
									if (wantsToOpen && espectedCount-1 == finalI1){
										progressBar.setVisibility(View.GONE);
										openMoreActivity();
									}

								}
							});
				}


			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				Log.d("dwd", "onError ");
			}
		});
		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
		interstitialAd.loadAd(new AdRequest.Builder().build());
		final AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				//TODO REMOVE FOR RELEASE
				.addTestDevice("98020A4CA653CBDED5EB3A03818ED4B1")
				.build();

		adView.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				super.onAdLoaded();

				//Log.d("dwd", "onAdLoaded");
			}

			@Override
			public void onAdClosed() {
				super.onAdClosed();
				Log.d("dwd", "onAdClosed");

			}

			@Override
			public void onAdFailedToLoad(int i) {
				super.onAdFailedToLoad(i);
				Log.d("dwd", "onAdFailedToLoad");

			}

			@Override
			public void onAdLeftApplication() {
				super.onAdLeftApplication();
			}

			@Override
			public void onAdOpened() {
				super.onAdOpened();
				Log.d("dwd", "onAdOpened");

			}
		});

		interstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				// Code to be executed when an ad finishes loading.
				//Log.d("dwd", "loaded IN");

			}

			@Override
			public void onAdFailedToLoad(int errorCode) {
				// Code to be executed when an ad request fails.
				Log.d("dwd", "failedToLoad IN");

			}

			@Override
			public void onAdOpened() {
				// Code to be executed when the ad is displayed.
				Log.d("dwd", "ad opened IN");

			}

			@Override
			public void onAdLeftApplication() {
				// Code to be executed when the user has left the app.
				Log.d("dwd", "onLeftppli IN");

			}

			@Override
			public void onAdClosed() {
				// Code to be executed when when the interstitial ad is closed.
				Log.d("dwd", "onAd Closed IN");
				interstitialAd.loadAd(new AdRequest.Builder().build());

			}

		});

		adView.loadAd(adRequest);


	}

	private void initViewByPos(int i) {
		ImageView imageView = new ImageView(getApplicationContext());
		imageView.setImageBitmap(bitmaps.get(i));
		ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		imageView.setLayoutParams(params);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageContainer.removeAllViews();
		imageContainer.addView(imageView);
		currentPos = i;
		textView.setText(String.valueOf(i));

	}


	@Override
	public void onPause() {
		if (adView != null) {
			adView.pause();
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (adView != null) {
			adView.resume();
		}
	}

	@Override
	public void onDestroy() {
		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}


	private void dissmisClick() {
		pre.setOnClickListener(null);
		ok.setOnClickListener(null);
		next.setOnClickListener(null);
		more.setOnClickListener(null);
	}

	private void enableClick() {
		pre.setOnClickListener(this);
		ok.setOnClickListener(this);
		next.setOnClickListener(this);
		more.setOnClickListener(this);
	}

	private void openMoreActivity() {
		Intent intent = new Intent(MainActivity.this, MoreActivity.class);
		intent.putExtra("moreAppParms", infoParamses);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.more:
				if (isConnected(this)) {
					if (!infoParamses.isEmpty()) {
						progressBar.setVisibility(View.GONE);
						openMoreActivity();
					} else {
						wantsToOpen = true;
						progressBar.setVisibility(View.VISIBLE);
					}

				} else {
					Toast.makeText(this, R.string.no_network, Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.left:
				initViewByPos(currentPos - (currentPos == 0 ? (-bitMapSize + 1) : 1));
				break;
			case R.id.ok:
				progressBar.setVisibility(View.VISIBLE);
				dissmisClick();
				Thread t = new Thread(new Runnable() {
					public void run() {
						try {
							wallpaperManager.setBitmap(bitmaps.get(currentPos));
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(MainActivity.this, "walpaper has changed", Toast.LENGTH_SHORT).show();
									progressBar.setVisibility(View.INVISIBLE);
									enableClick();
									if (interstitialAd.isLoaded()) {
										interstitialAd.show();
									}
								}
							});

						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				t.start();
				dissmisClick();
				break;
			case R.id.right:
				if (currentPos == bitMapSize - 1) {
					currentPos = 0;
					initViewByPos(currentPos);
				} else {
					initViewByPos(currentPos + 1);
				}
				break;
		}
	}


	public static boolean isConnected(Context context) {
		if (context != null) {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm != null) {
				NetworkInfo nInfo = cm.getActiveNetworkInfo();
				return nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
			}
		}
		return false;
	}

}