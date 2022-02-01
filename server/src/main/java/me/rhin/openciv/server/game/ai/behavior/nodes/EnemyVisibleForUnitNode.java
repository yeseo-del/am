package me.rhin.openciv.server.game.ai.behavior.nodes;

import me.rhin.openciv.server.game.ai.behavior.BehaviorStatus;
import me.rhin.openciv.server.game.ai.behavior.UnitNode;
import me.rhin.openciv.server.game.map.tile.Tile;
import me.rhin.openciv.server.game.unit.Unit;

public class EnemyVisibleForUnitNode extends UnitNode {

	public EnemyVisibleForUnitNode(Unit unit, String name) {
		super(unit, name);
	}

	@Override
	public void tick() {

		for (Tile tile : unit.getObservedTiles()) {
			// If the tile contains enemy unit or city...
			if (tile.getEnemyAttackableEntity(unit.getPlayerOwner()) != null) {
				setStatus(BehaviorStatus.SUCCESS);
				return;
			}
		}

		setStatus(BehaviorStatus.FAILURE);
	}

}