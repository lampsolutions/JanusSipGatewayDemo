package de.lamp_solutions.janussipgatewaydemo.Domain;

import android.content.Context;
import android.opengl.EGLContext;
import android.util.Log;

import computician.janusclientapi.JanusServer;
import de.lamp_solutions.janussipgatewaydemo.MainActivity;


/**
 * gateway server to send calls to janus gateway using
 * webrtc. The janus server sends the calls to
 * a sipserver
 */
public class SipGateway {

    private static final String TAG = "SipGateway";

    /**
     * url to janus server
     */
    private String janus_uri = null;


    /**
     * janus server
     */
    public JanusServer janusServer;

    public JanusPluginCallbacks callback;


    /**
     * set url to janus server
     * @param uri
     */
    public void setJanusUri(String uri){
        Log.v(TAG,"janusUrl set to:" + uri);
        janus_uri = uri;
    }

    public void configureCredentials(String proxy, String identity, String domain, String name, String secret){
        callback = new JanusPluginCallbacks();
        callback.proxy=proxy;
        callback.user=identity;
        callback.domain=domain;
        callback.name=name;
        callback.password=secret;

    }

    /**
     * initializing media context for streaming
     * currently only audio channel is used
     *
     * @param context
     * @param audio
     * @param video
     * @param videoHwAcceleration
     * @param eglContext
     * @return
     */
    public boolean initializeMediaContext(Context context, boolean audio, boolean video, boolean videoHwAcceleration, EGLContext eglContext, MainActivity callActivity){
        JanusGlobalCallbacks globalCallbacks = new JanusGlobalCallbacks ();

        globalCallbacks.pluginCallbacks=callback;

        globalCallbacks.callActivity= callActivity;
        globalCallbacks.sipgateway=this;
        globalCallbacks.janus_uri=janus_uri;

        janusServer = new JanusServer(
                globalCallbacks
        );

        Boolean contextInitialized = janusServer.initializeMediaContext(context, audio, video, videoHwAcceleration, eglContext);
        if(contextInitialized){
            Log.v("test","janusServer media context initialized");
        }
        else{
            Log.v("test","janusServer media context not initialized");

        }
        return contextInitialized;
    }

    /**
     * register user and start call
     */
    public void Start() {

        Log.v(TAG,"starting");
        janusServer.Connect();
    }

    public void call(String target){
        callback.call(target);

    }


}
