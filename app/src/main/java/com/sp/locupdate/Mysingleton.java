package com.sp.locupdate;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class
Mysingleton {
  public static   Mysingleton mysingleton;
      Context context;
    private RequestQueue requestQueue;



        private Mysingleton(Context context){
        this.context=context;
        requestQueue=getRequestque();


        }

    private RequestQueue getRequestque() {

if(requestQueue==null)
{
    requestQueue= Volley.newRequestQueue(context.getApplicationContext());
}


        return requestQueue;
        }

   public  static synchronized Mysingleton getMinstance(Context context){
            if(mysingleton==null){
                mysingleton=new Mysingleton(context);
            }
            return  mysingleton;
   }

   public <T>void addToRequestQue(Request<T>request){
            getRequestque().add(request);
   }


}
