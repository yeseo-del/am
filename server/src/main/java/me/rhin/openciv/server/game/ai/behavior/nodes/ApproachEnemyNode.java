package me.rhin.openciv.server.game.ai.behavior.nodes;

import me.rhin.openciv.server.game.ai.behavior.BehaviorStatus;
import me.rhin.openciv.server.game.ai.behavior.Node;
import me.rhin.openciv.server.game.map.tile.Tile;
import me.rhin.openciv.server.game.unit.Unit;

public class ApproachEnemyNode extends Node {

	private Unit unit;

	public ApproachEnemyNode(Unit unit) {
		super("ApproachEnemyNode");

		this.unit = unit;
	}

	@Override
	public void tick() {

		Unit targetUnit = null;

		for (Tile tile : unit.getObservedTiles()) {
			if (tile.getTopEnemyUnit(unit.getPlayerOwner()) != null)
				targetUnit = tile.getTopEnemyUnit(unit.getPlayerOwner());
		}

		if (targetUnit == null) {
			setStatus(BehaviorStatus.FAILURE);
			return;
		}

		unit.moveToTile(targetUnit.getStandingTile());
		setStatus(BehaviorStatus.SUCCESS);
	}

}