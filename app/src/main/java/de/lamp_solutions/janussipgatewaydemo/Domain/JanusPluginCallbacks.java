package de.lamp_solutions.janussipgatewaydemo.Domain;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.MediaStream;

import computician.janusclientapi.IJanusPluginCallbacks;
import computician.janusclientapi.IPluginHandleWebRTCCallbacks;
import computician.janusclientapi.JanusMediaConstraints;
import computician.janusclientapi.JanusPluginHandle;
import computician.janusclientapi.JanusSupportedPluginPackages;
import computician.janusclientapi.PluginHandleSendMessageCallbacks;
import de.lamp_solutions.janussipgatewaydemo.MainActivity;


/**
 * handle callbacks from janus server for sip plugin
 */
public class JanusPluginCallbacks implements IJanusPluginCallbacks {

    private static final String TAG = "JanusPluginCallbacks";


    /**
     * janus service class for handling callbacks during communication
     */
    private JanusPluginHandle handle = null;

    /**
     * user
     */
    public String user="";

    /**
     * user
     */
    public String name="";

    public String domain = "";


    public String password = "";


    public String proxy = "";

    public String target = "";


    /**
     * Activity initiating the call
     */
    private MainActivity callActivity;


    /**
     * register user
     */
    private void registerUsername() {
        if (handle != null) {
            JSONObject body = new JSONObject();
            JSONObject msg = new JSONObject();
            try {
                body.put("request", "register");
                body.put("username", "sip:" + user + "@" + domain);
                body.put("secret", password);
                body.put("display_name", name);
                body.put("proxy", proxy);

                msg.put("message", body);
            } catch (Exception ex) {

            }
            handle.sendMessage(new PluginHandleSendMessageCallbacks(msg));
            Log.v("test", "sent");
        }
    }

    /**
     * execute call
     */
    public void call(String _target) {
        target = _target;
        Log.v(TAG,"target:" + _target);
        if (handle != null) {
            Log.v(TAG,"target2:" + _target);

            handle.createOffer(new IPluginHandleWebRTCCallbacks() {
                @Override
                public void onSuccess(JSONObject obj) {
                    try {
                        JSONObject msg = new JSONObject();
                        JSONObject body = new JSONObject();
                        body.put("request", "call");
                        body.put("uri", target);


                        body.put("autoack", false);
                        msg.put("message", body);
                        msg.put("jsep", obj);
                        handle.sendMessage(new PluginHandleSendMessageCallbacks(msg));
                    } catch (Exception ex) {

                    }
                }

                @Override
                public JSONObject getJsep() {
                    return null;
                }

                @Override
                public JanusMediaConstraints getMedia() {
                    JanusMediaConstraints cons = new JanusMediaConstraints();
                    cons.setRecvAudio(true);
                    cons.setRecvVideo(false);
                    cons.setVideo(null);

                    cons.setSendAudio(true);
                    return cons;
                }

                @Override
                public Boolean getTrickle() {
                    return true;
                }

                @Override
                public void onCallbackError(String error) {

                }
            });
        }
    }

    @Override
    public void success(JanusPluginHandle pluginHandle) {
        Log.v(TAG,"handle:" + pluginHandle);
        handle = pluginHandle;
        registerUsername();

    }


    @Override
    public void onMessage(JSONObject msg, final JSONObject jsepLocal) {

        Log.v(TAG, "test1:" +  msg.toString());


        JSONObject event = null;
        try {
            event = msg.getJSONObject("result");

            Log.v(TAG, "test2:" +  event.toString());

            if(event.getString("event").equals("accepted")){
                //callActivity.stopRinging();
                Log.v(TAG,"result_:" + jsepLocal.toString());
                Log.v(TAG,"result2_:" + msg.toString());
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        if(jsepLocal != null)
        {
            handle.handleRemoteJsep(new IPluginHandleWebRTCCallbacks() {
                final JSONObject myJsep = jsepLocal;
                @Override
                public void onSuccess(JSONObject obj) {
                }

                @Override
                public JSONObject getJsep() {
                    return myJsep;
                }

                @Override
                public JanusMediaConstraints getMedia() {
                    return null;
                }

                @Override
                public Boolean getTrickle() {
                    return Boolean.FALSE;
                }

                @Override
                public void onCallbackError(String error) {

                }
            });
        }
    }

    @Override
    public void onLocalStream(MediaStream stream) {
       // stream.videoTracks.get(0).setEnabled(false);


    }

    @Override
    public void onRemoteStream(MediaStream stream) {
     //   stream.videoTracks.get(0).setEnabled(false);

    }

    @Override
    public void onDataOpen(Object data) {

    }

    @Override
    public void onData(Object data) {

    }

    @Override
    public void onCleanup() {
        Log.v(TAG,"cleanup");
    }

    @Override
    public JanusSupportedPluginPackages getPlugin() {
        return JanusSupportedPluginPackages.JANUS_SIP;
    }

    @Override
    public void onCallbackError(String error) {

    }

    @Override
    public void onDetached() {
        Log.v(TAG,"detached");

    }

    @Override
    public void onMedia() {
        Log.v(TAG,"onMedia is called");
    }

    @Override
    public void onHangup() {
        Log.v(TAG,"hangup");


    }

    public void setCallActivity(MainActivity callActivity) {
        this.callActivity = callActivity;
    }
}
