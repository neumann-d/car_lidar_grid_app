package car.model.autonomos.modelcarlidargrid;

import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
        setDefaultMasterName("");
        setDefaultAppName("car_lidar_grid");

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        PixelGridView pixelGrid = new PixelGridView(this);
        pixelGrid.setNumColumns(16);
        pixelGrid.setNumRows(24);

        setContentView(pixelGrid);
    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        super.init(nodeMainExecutor);

        NodeConfiguration nodeConfiguration =
                NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress(), getMasterUri());

        nodeMainExecutor.execute(this, nodeConfiguration.setNodeName("android/car_lidar_grid_view"));
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("android/car_lidar_grid");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        NameResolver appNameSpace = getMasterNameSpace();

        String lidarTopic = appNameSpace.resolve("/scan").toString();

        Subscriber<LaserScan> subscriberLidar = connectedNode.newSubscriber(lidarTopic, sensor_msgs.LaserScan._TYPE);
        subscriberLidar.addMessageListener(new MessageListener<LaserScan>() {
            @Override
            public void onNewMessage(final sensor_msgs.LaserScan message) {



            }
        });
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
}
