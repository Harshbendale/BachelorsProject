package com.example.harshal.bepro;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    int flag = 0;
    private Button stat;
    private TextView seat;

    private GoogleMap mMap;
    //-----------------------
    public static String wifiModuleIp = "192.168.43.78";
    public static int wifiModulePort = 21567;
    static final int MESSAGE_READ = 1;
    SendRcv sendRcv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        stat = (Button)findViewById(R.id.btnstat);
        seat = (TextView)findViewById(R.id.status);

//        stat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                seat.setText("to be decided");
//            }
//        });

        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(18.49411633, 73.83634999);
        mMap.addMarker(new MarkerOptions().position(sydney).title("You are here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17));

        stat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Socket_AsyncTask gj = new Socket_AsyncTask();
                gj.execute();
            }
        });
    }

    //----------------------------------------------------------------------------------------------

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

            //TextView t = (TextView)findViewById(R.id.txt);
            switch(message.what){
                case MESSAGE_READ:
                    byte[] readBuff = (byte[])message.obj;
                    String tempMsg = new String(readBuff,0,message.arg1);
                    //t.setText(tempMsg);       SET MARKERS HERE
                    String[] arrOfStr = tempMsg.split("@",3);
                    double d = Double.parseDouble(arrOfStr[0]);
                    double d1 = Double.parseDouble(arrOfStr[1]);
                    seat.setText(arrOfStr[2]);
                    mMap.clear();
                    LatLng syd = new LatLng(d,d1);
                    mMap.addMarker(new MarkerOptions().position(syd).title("Current location of Bus"));
                    if(d==18.493735 && d1==73.83587){
                        flag = 1;
                    }
                    if(flag!=1){
                        LatLng sydney = new LatLng(18.493735, 73.83587);
                        mMap.addMarker(new MarkerOptions().position(sydney).title("You are here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    }

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(syd, 17));

                    break;
            }
            return true;
        }
    });

    private class SendRcv extends Thread{
        private Socket socket;
        private InputStream inputStream;

        public SendRcv(Socket skt){
            socket=skt;
            try {
                inputStream=socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run(){
            byte[] buffer = new byte[1024];
            int bytes;
            while(socket!=null){
                try {
                    bytes = inputStream.read(buffer);
                    if(bytes>0){
                        handler.obtainMessage(MESSAGE_READ,bytes,-1,buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class Socket_AsyncTask extends AsyncTask<Void, Void, Void> {

        Socket socket;
        @Override
        protected Void doInBackground(Void... params) {
            try{
                InetAddress inetAddress = InetAddress.getByName(MapsActivity.wifiModuleIp);
                socket = new java.net.Socket(inetAddress,MapsActivity.wifiModulePort);
                sendRcv= new SendRcv(socket);
                sendRcv.start();
            }catch (UnknownHostException e){e.printStackTrace();}catch (IOException e){e.printStackTrace();}
            return null;
        }
    }
}
