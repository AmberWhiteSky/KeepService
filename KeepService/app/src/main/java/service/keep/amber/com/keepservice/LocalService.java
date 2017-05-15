package service.keep.amber.com.keepservice;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by punisher on 2017/5/15.
 */

public class LocalService extends Service {
    private Mybinder mybinder;
    private  MyConn myConn;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mybinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mybinder   =new Mybinder();
        if (myConn==null){
            myConn=new MyConn();
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        LocalService.this.bindService(new Intent(LocalService.this,RemoteService.class),myConn, Context.BIND_IMPORTANT);
    }


    class Mybinder extends ProcessService.Stub {
        @Override
        public String getServiceName() throws RemoteException {
            return "LocalService";
        }
    }



    class  MyConn implements ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("info","远程服务连接成功");

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(LocalService.this,"远程服务被杀死",Toast.LENGTH_LONG).show();
            //启动远程服务
            LocalService.this.startService(new Intent(LocalService.this,RemoteService.class));
            //绑定远程服务
            LocalService.this.bindService(new Intent(LocalService.this,RemoteService.class),myConn,Context.BIND_IMPORTANT);


        }
    }
}
