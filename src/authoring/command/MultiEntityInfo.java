package authoring.command;

import java.util.ArrayList;
import java.util.List;

import authoring.networking.Packet;

/**
 * Info needed to communicate information about multiple entities across a
 * server. This can be used to send multiple EntityInfo objects across a server
 * on a single packet.
 * 
 * @author jimmy
 *
 */
public class MultiEntityInfo<T extends Packet> extends Packet
{
	private static final long serialVersionUID = 871085261882852024L;
	private List<T> multiInfo;

	public MultiEntityInfo(List<T> multiInfo)
	{
		this.multiInfo = new ArrayList<T>();
		this.multiInfo.addAll(multiInfo);
	}

	public List<T> getInfo()
	{
		return multiInfo;
	}
}
