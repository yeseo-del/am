package me.rhin.openciv.server.game.ai.behavior;

/**
 * A non-spec behavior tree node that runs ALL child nodes under it. Always
 * returns SUCCESS.
 * 
 * @author Ryan
 *
 */
public class SmoothNode extends Node {

	public SmoothNode(String name) {
		super(name);
	}

	@Override
	public void tick() {
		for (Node childNode : childNodes) {
			childNode.tick();
		}

		setStatus(BehaviorStatus.SUCCESS);
	}
}
