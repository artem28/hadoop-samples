package com.sample.udf;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.log4j.Logger;

@Description(name = "NetMaskAddressTopIntUDF", value = "returns long top value according to net mask, where x is netmask (STRING)", extended = "SELECT netMaskAddressTopIntUDF('21.12.12.12/12') from foo limit 1;")
public class NetMaskAddressTopIntUDF extends NetMaskAddressLowIntUDF {
	private final static Logger log = Logger
			.getLogger(NetMaskAddressTopIntUDF.class.getName());

	public LongWritable evaluate(Text input) {
		try {
			if (input == null)
				return null;
			String str = input.toString();
			int pos = str.indexOf("/");
			String addresskStr = validateAddress(str, pos);
			int bitsNumber = validateMask(str, pos);
			long ipAddressLong = getLong(addresskStr);
			long netMask = getNetMaskLong(bitsNumber);

			return new LongWritable(ipAddressLong | netMask);
		} catch (Exception e) {
			log.error("Error  in NetMaskAddressTopIntUDF evaluate with param:"
					+ input, e);
			return null;
		}
	}

	static protected long getNetMaskLong(int bitsNumber) {
		return (1 << (32 - bitsNumber)) - 1;
	}

	static protected int validateMask(String input, int pos) {
		String maskStr = input;
		if (pos > 0) {
			return Integer.parseInt(maskStr.substring(pos + 1));
		}
		return 32;
	}
}