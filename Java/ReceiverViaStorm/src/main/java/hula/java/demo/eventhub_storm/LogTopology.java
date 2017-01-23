package hula.java.demo.eventhub_storm;

import java.io.FileReader;
import java.io.InputStream;
import java.util.Properties;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import com.microsoft.eventhubs.samples.EventCount;
import com.microsoft.eventhubs.spout.EventHubSpout;
import com.microsoft.eventhubs.spout.EventHubSpoutConfig;

public class LogTopology {
	protected EventHubSpoutConfig spoutConfig;
    protected int numWorkers;

    public static void main(String[] args) throws Exception
    {
    	LogTopology topology = new LogTopology();
    	topology.runScenario(args);
    }
    
    protected void readEHConfig(String[] args)throws Exception
    {
        Properties properties = new Properties();
        if (args.length > 1) {
        	properties.load(new FileReader(args[1]));
        } else {
        	InputStream config = EventCount.class.getClassLoader().getResourceAsStream("Config.properties");
        	properties.load(config);
        }

        String username = properties.getProperty("eventhubspout.username");
        String password = properties.getProperty("eventhubspout.password");
        String namespaceName = properties.getProperty("eventhubspout.namespace");
        String entityPath = properties.getProperty("eventhubspout.entitypath");
        String zkEndpointAddress = properties.getProperty("zookeeper.connectionstring"); // opt
        int partitionCount = Integer.parseInt(properties.getProperty("eventhubspout.partitions.count"));
        int checkpointIntervalInSeconds = Integer.parseInt(properties.getProperty("eventhubspout.checkpoint.interval"));
        int receiverCredits = Integer.parseInt(properties.getProperty("eventhub.receiver.credits")); // prefetch count (opt)
        
        System.out.println("Eventhub spout config: ");
        System.out.println("  partition count: " + partitionCount);
        System.out.println("  checkpoint interval: "+ checkpointIntervalInSeconds);
        System.out.println("  receiver credits: " + receiverCredits);

        spoutConfig = new EventHubSpoutConfig(username, password, 
        		namespaceName, entityPath, partitionCount, zkEndpointAddress,
        		checkpointIntervalInSeconds, receiverCredits);
        
        // NOTE: this is necessary to point to MOONCAKE
        spoutConfig.setTargetAddress("servicebus.chinacloudapi.cn");

        // set the number of workers to be the same as partition number.
        // the idea is to have a spout and a logger bolt co-exist in one
        // worker to avoid shuffling messages across workers in storm cluster.
        numWorkers = spoutConfig.getPartitionCount();

        if (args.length > 0) {
        	// set topology name so that sample Trident topology can use it as
			// stream name.
			spoutConfig.setTopologyName(args[0]);
		}
    }

    protected StormTopology buildTopology()
    {
    	TopologyBuilder topologyBuilder = new TopologyBuilder();

    	EventHubSpout eventHubSpout = new EventHubSpout(spoutConfig);
    	topologyBuilder.setSpout("EventHubsSpout", eventHubSpout,
    			spoutConfig.getPartitionCount()).setNumTasks(
    					spoutConfig.getPartitionCount());
    	topologyBuilder
    	.setBolt("LoggerBolt", new LoggerBolt(), spoutConfig.getPartitionCount())
    	.localOrShuffleGrouping("EventHubsSpout")
    	.setNumTasks(spoutConfig.getPartitionCount());
    	
    	return topologyBuilder.createTopology();
    }

    protected void runScenario(String[] args)throws Exception
    {
    	boolean runLocal = true;
    	readEHConfig(args);
    	StormTopology topology = buildTopology();
    	Config config = new Config();
    	config.setDebug(false);

    	if (runLocal) {
    		config.setMaxTaskParallelism(2);
    		LocalCluster localCluster = new LocalCluster();
    		localCluster.submitTopology("test", config, topology);
    		Thread.sleep(5000000);
    		localCluster.shutdown();
    	} else {
    		config.setNumWorkers(numWorkers);
    		StormSubmitter.submitTopology(args[0], config, topology);
    	}
    }
}
