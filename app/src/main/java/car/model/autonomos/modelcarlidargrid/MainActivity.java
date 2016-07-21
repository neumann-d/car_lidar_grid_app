package car.model.autonomos.modelcarlidargrid;

import android.graphics.drawable.BitmapDrawable;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.github.rosjava.android_remocons.common_tools.apps.RosAppActivity;

import org.ros.address.InetAddressFactory;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.namespace.NameResolver;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;
import org.ros.node.topic.Subscriber;

import sensor_msgs.LaserScan;

public class MainActivity extends RosAppActivity implements NodeMain {

    public MainActivity() {
        super("MainActivity", "MainActivity");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setDefaultMasterName(getString(R.string.default_robot));
        setDefaultAppName(getString(R.string.default_app));
        setDashboardResource(R.id.LidarGridView);
        setMainWindowResource(R.layout.activity_main);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout grid = (LinearLayout) findViewById(R.id.LidarGridView);



        PixelGridView pixelGrid = new PixelGridView(this);
        pixelGrid.setNumColumns(16);
        pixelGrid.setNumRows(24);

        grid.addView(pixelGrid);

        // setContentView(pixelGrid);

        // TODO Tricky solution to the StrictMode; the recommended way is by using AsyncTask
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("android/car_lidar_grid");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        super.onStart();

        NameResolver appNameSpace = getMasterNameSpace();

        String lidarTopic = appNameSpace.resolve("/scan").toString();

        Subscriber<LaserScan> subscriberLidar = connectedNode.newSubscriber(lidarTopic, sensor_msgs.LaserScan._TYPE);
        subscriberLidar.addMessageListener(new MessageListener<LaserScan>() {
            @Override
            public void onNewMessage(final sensor_msgs.LaserScan message) {
                // handle lidar message
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onShutdown(Node node) {

    }

    @Override
    public void onShutdownComplete(Node node) {

    }

    @Override
    public void onError(Node node, Throwable throwable) {

    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        super.init(nodeMainExecutor);

        NodeConfiguration nodeConfiguration =
                NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress(), getMasterUri());

        nodeMainExecutor.execute(this, nodeConfiguration.setNodeName("android/video_view"));
    }
}
