package com.glm.app.fragment;

import java.util.ArrayList;
import java.util.List;

import com.glm.bean.ConfigTrainer;
import com.glm.bean.ExerciseManipulate;
import com.glm.trainer.R;
import com.glm.utils.ExerciseUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class WorkoutMapFragment extends Fragment {
	private ConfigTrainer oConfigTrainer;
	private GoogleMap oMapView=null;
	
	//private List<Overlay> mapOverlays;
	
	//private MapController oMapController;
	//private GeoPoint TrackPoints [];
	//private TrackOverlay oTrack;
	private Context mContext;
	private View rootView;
	private int mIDWorkout=0;
	/**
	 * 
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	public WorkoutMapFragment() {
		
	}

	@Override
	public void onDestroyView() {
		
		super.onDestroyView();
		try {
	        SupportMapFragment fragment = (SupportMapFragment) getActivity()
	                                          .getSupportFragmentManager().findFragmentById(
	                                        		  R.id.gmapv2);
	        if (fragment != null) getFragmentManager().beginTransaction().remove(fragment).commit();

	    } catch (IllegalStateException e) {
	        //handle this situation because you are necessary will get 
	        //an exception here :-(
	    }
		rootView=null;
		oMapView=null;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		Log.v(this.getClass().getCanonicalName(), "onCreateView MAP Fragment");
		((ViewGroup) container.getParent()).removeView(rootView);
		
		try {
			rootView = inflater.inflate(R.layout.maps_v2,
					container, false);
			Log.v(this.getClass().getCanonicalName(), "inflate OK MAP Fragment");
			//getActivity().getFragmentManager().findFragmentById(id)
			
			oMapView = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.gmapv2)).getMap();
			
			/*SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
			FragmentTransaction fragmentTransaction =
			             getChildFragmentManager().beginTransaction();
			     fragmentTransaction.add(R.id.gmapv2, mMapFragment);
			     fragmentTransaction.commit(); 
			
			oMapView= mMapFragment.getMap(); */
	    } catch (InflateException e) {
	        /* map is already there, just return view as it is */
	    	Log.e(this.getClass().getCanonicalName(),"InflateException");
	    	e.printStackTrace();
	    }catch (Exception e) {
			// TODO: handle exception
	    	Log.e(this.getClass().getCanonicalName(),"Exception");
	    	e.printStackTrace();
		}
		
		   
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		mContext=getActivity().getApplicationContext();
		GraphTask oTask = new GraphTask();
	    oTask.execute();
	 
	}
	
	public void setContext(Context context,int idWorkout) {
		mContext=context;	
		mIDWorkout=idWorkout;
	}
	
	class GraphTask extends AsyncTask<Void, Void, Void> {
		List<LatLng> aLatLng=null;
		@Override
		protected Void doInBackground(Void... params) {
			oConfigTrainer = ExerciseUtils.loadConfiguration(mContext);		
			ExerciseUtils.populateExerciseDetails(mContext, oConfigTrainer,  mIDWorkout);
			
			aLatLng = new ArrayList<LatLng>();
			
			for(int i=0;i<ExerciseManipulate.getWatchPoint().size();i++){
				aLatLng.add(ExerciseManipulate.getWatchPoint().get(i).getLatLong());
			}		    
			while(oMapView==null) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Log.e(this.getClass().getCanonicalName(), "Error during wait maps");
				}
			}
	        return null;
		}
		
		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(Void result) {		
			Polyline route = oMapView.addPolyline(new PolylineOptions()
			  .width(5)
			  .color(Color.BLUE)
			  .geodesic(true));
			route.setPoints(aLatLng);
			
		    oMapView.addMarker(new MarkerOptions()
	        .position(ExerciseManipulate.getWatchPoint().get(0).getLatLong())
	        .title("Start")); 
		    oMapView.addMarker(new MarkerOptions()
	        .position(ExerciseManipulate.getWatchPoint().get(ExerciseManipulate.getWatchPoint().size()-1).getLatLong())
	        .title("End "+ExerciseManipulate.getsTotalDistance())); 
		    
			CameraPosition newCamPos = new CameraPosition(ExerciseManipulate.getWatchPoint().get(ExerciseManipulate.getWatchPoint().size()/2).getLatLong(), 
                    12.5f, 
                    oMapView.getCameraPosition().tilt, //use old tilt 
                    oMapView.getCameraPosition().bearing); //use old bearing
			oMapView.animateCamera(CameraUpdateFactory.newCameraPosition(newCamPos), 4000, null);
			
			//oMapView.addPolyline((new PolylineOptions()).addAll(aLatLng));
			//oMapView.moveCamera(CameraUpdateFactory.newLatLng(ExerciseManipulate.getWatchPoint().get(0).getLatLong()));
		}
	}
}