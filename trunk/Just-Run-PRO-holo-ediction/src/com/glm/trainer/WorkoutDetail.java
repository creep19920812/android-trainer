package com.glm.trainer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.glm.app.fragment.WorkoutDetailFragment;
import com.glm.app.fragment.WorkoutGraphFragment;
import com.glm.app.fragment.WorkoutMapFragment;



import com.glm.app.fragment.WorkoutMusicFragment;
import com.glm.app.fragment.WorkoutNoteFragment;
import com.glm.bean.ConfigTrainer;
import com.glm.bean.ExerciseManipulate;
import com.glm.bean.Music;
import com.glm.utils.ExerciseUtils;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class WorkoutDetail extends FragmentActivity implements
		ActionBar.TabListener {
	private ConfigTrainer oConfigTrainer;
	
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	private WorkoutDetailFragment oWorkoutDetail=null;
	private WorkoutGraphFragment  oWorkoutGraph=null;
	private WorkoutMapFragment    oWorkoutMap=null;
	private WorkoutMusicFragment  oWorkoutMusic=null;
	private WorkoutNoteFragment   oWorkoutNote=null;
	private int mIDWorkout=0;
	private ArrayList<Music> mMusic;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		if(extras !=null){
			mIDWorkout = extras.getInt("exercise_id");
		}        

		setContentView(R.layout.activity_new_main);

		oWorkoutDetail= new WorkoutDetailFragment();
		oWorkoutGraph= new WorkoutGraphFragment();
		oWorkoutMap= new WorkoutMapFragment();
		oWorkoutMusic = new WorkoutMusicFragment();
		oWorkoutNote= new WorkoutNoteFragment();
		
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
        // Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOffscreenPageLimit(4);
		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
        
	}

	@Override
    public void onBackPressed() {
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), HistoryList.class);
		startActivity(intent);
		finish();
    }    
	@Override
	public void onResume() {
		Bundle extras = getIntent().getExtras();
		if(extras !=null){
			mIDWorkout = extras.getInt("exercise_id");
		} 
		if(oWorkoutDetail!=null) oWorkoutDetail.setContext(WorkoutDetail.this,mIDWorkout);
		if(oWorkoutGraph!=null) oWorkoutGraph.setContext(WorkoutDetail.this,mIDWorkout);
		if(oWorkoutMusic!=null) oWorkoutMusic.setContext(WorkoutDetail.this,mIDWorkout);
		ExeriseTask oTask = new ExeriseTask();
	    oTask.execute();
	    
		super.onResume();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			
			switch(position) {
			case 0:
				//DETAIL
				
				oWorkoutDetail.setContext(WorkoutDetail.this,mIDWorkout);
				return oWorkoutDetail;
			case 1:
				//GRAPH
				
				oWorkoutGraph.setContext(WorkoutDetail.this,mIDWorkout);
				return oWorkoutGraph;
			case 2:
				//Maps
				
				oWorkoutMap.setContext(WorkoutDetail.this,mIDWorkout);
				return oWorkoutMap;
			case 3:
				//Music
				
				oWorkoutMusic.setContext(WorkoutDetail.this,mIDWorkout);
				return oWorkoutMusic;
				
			case 4:
				//Note
				
				oWorkoutNote.setContext(WorkoutDetail.this,mIDWorkout);
				return oWorkoutNote;	
			}
			
			return null;
		}

		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.exercise_dett).toUpperCase(l);
			case 1:
				return getString(R.string.exercise_graph).toUpperCase(l);
			case 2:
				return getString(R.string.maps).toUpperCase(l);
			case 3:
				return getString(R.string.listening_song).toUpperCase(l);
			case 4:
				return getString(R.string.note).toUpperCase(l);
			}
			return null;
		}
	}
	
	class ExeriseTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			oConfigTrainer = ExerciseUtils.loadConfiguration(getApplicationContext());		
			//Chiamo il tackAsink
			ExerciseUtils.populateExerciseDetails(getApplicationContext(), oConfigTrainer, mIDWorkout);
		    //Log.v(this.getClass().getCanonicalName(),"IDExercide: " +ExerciseManipulate.getiIDExercise()+" - "+ExerciseManipulate.getsTotalDistance());
			mMusic=ExerciseUtils.getMusicWorkout(getApplicationContext(), oConfigTrainer,  mIDWorkout);
			oWorkoutDetail.oConfigTrainer=oConfigTrainer;
		    
	        
	        return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {			
			SimpleDateFormat dfm = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			
			oWorkoutDetail.txtDate.setText(dfm.format(ExerciseManipulate.getdDateTimeStart()));
			oWorkoutDetail.oTxt_Time.setText(ExerciseManipulate.getsTotalTime());
			oWorkoutDetail.oTxt_Distance.setText(ExerciseManipulate.getsTotalDistance());
			oWorkoutDetail.oTxt_AVGSpeed.setText(ExerciseManipulate.getsAVGSpeed());
			oWorkoutDetail.oTxt_AVGPace.setText(ExerciseManipulate.getsMinutePerDistance());
			oWorkoutDetail.oTxt_MAXSpeed.setText(ExerciseManipulate.getsMAXSpeed());
			oWorkoutDetail.oTxt_MAXPace.setText(ExerciseManipulate.getsMAXMinutePerDistance());
			oWorkoutDetail.oTxt_Step.setText(ExerciseManipulate.getsStepCount());
			
			if(ExerciseManipulate.getsStepCount().compareToIgnoreCase("0")==0){
				oWorkoutDetail.oTxt_Step.setVisibility(View.GONE);
			}
			
			
			oWorkoutDetail.oTxt_Kalories.setText(ExerciseManipulate.getsCurrentCalories());
			    if(oConfigTrainer!=null){
			    	if(oConfigTrainer.isbCardioPolarBuyed()){
			    		if(ExerciseManipulate.getiMAXBpm()==0){
			    			oWorkoutDetail.oTxt_MaxBpm.setText("n.d.");
			    		}else{
			    			Log.i(this.getClass().getCanonicalName(),"MaxBPM"+ExerciseManipulate.getiMAXBpm());
			    			oWorkoutDetail.oTxt_MaxBpm.setText(String.valueOf(ExerciseManipulate.getiMAXBpm()));	
			    		}
			    		if(ExerciseManipulate.getiAVGBpm()==0){
			    			oWorkoutDetail.oTxt_AvgBpm.setText("n.d.");
			    		}else{
			    			oWorkoutDetail.oTxt_AvgBpm.setText(String.valueOf(ExerciseManipulate.getiAVGBpm()));
			    		}	    	    	    		
			    	}else{
			    		oWorkoutDetail.oLLDectails.removeView(oWorkoutDetail.oMaxBpm);
			    		oWorkoutDetail.oLLDectails.removeView(oWorkoutDetail.oAvgBpm); 	    		
			    	}
			    }
			oWorkoutMusic.addMusic(mMusic);
		}
	}
}
