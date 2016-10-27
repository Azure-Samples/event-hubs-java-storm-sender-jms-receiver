package hula.java.demo.eventhub_storm;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class LoggerBolt extends BaseRichBolt {
	private OutputCollector collector;
	private static final Logger logger = LoggerFactory.getLogger(LoggerBolt.class);
	
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		

	}

	public void execute(Tuple input) {
		String value = input.getString(0);
		logger.info("Tuple value: " + value);
		
		collector.ack(input);

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub

	}

}
