package utility;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.apache.log4j.Logger;

import config.Constants;

public class NetWorkUtils {
	private final static Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);

	/**
	 * @return the local ip address, which is not a loopback address.
	 */
	public static String getLocalIpAddress() {
		try {
			InetAddress address = InetAddress.getLocalHost();
			if (address.isLoopbackAddress()) {
				Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
				while (networkInterfaces.hasMoreElements()) {
					NetworkInterface ni = networkInterfaces.nextElement();
					Enumeration<InetAddress> addresses = ni.getInetAddresses();
					while (addresses.hasMoreElements()) {
						address = addresses.nextElement();

						if (!address.isLinkLocalAddress() && !address.isLoopbackAddress()
								&& (address instanceof Inet4Address)) {
							return address.getHostAddress();
						}
					}
				}

				LOG.warn("Your hostname, " + InetAddress.getLocalHost().getHostName() + " resolves to"
						+ " a loopback address: " + address.getHostAddress() + ", but we couldn't find any"
						+ " external IP address!");
			}

			return address.getHostAddress();
		} catch (IOException e) {
			LOG.error(e);
		}
		return null;
	}
}
